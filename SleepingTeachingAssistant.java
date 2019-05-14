import java.util.concurrent.Semaphore;
import java.util.Random;
	
public class SleepingTeachingAssistant {

	    public static void main(String[] args) 
	    {
	        // Number of students.
	        int numberofStudents = 5;
	        
	        // Create semaphores.
	        SignalSemaphore wakeup = new SignalSemaphore();
	        Semaphore chairs = new Semaphore(3);
	        Semaphore available = new Semaphore(1);
	        
	        
	        // Used for randomly generating program time.
	        Random studentWait = new Random();
	        
	        // Create student threads and start them.
	        for (int i = 0; i < numberofStudents; i++)
	        {
	            Thread student = new Thread(new Student(studentWait.nextInt(20), wakeup, chairs, available, i+1));
	            student.start();
	        }
	        
	        // Create and start TA Thread.
	        Thread ta = new Thread(new TeachingAssistant(wakeup, chairs, available));
	        ta.start();
	    }
	}


	// Semaphore implementation is used to "wakeup" the TA.
	class SignalSemaphore {
	    private boolean signal = false;

	    // Used to send the signal.
	    public synchronized void take() {
	        this.signal = true;
	        this.notify();
	    }

	    // Will wait until it receives a signal before continuing.
	    public synchronized void release() throws InterruptedException{
	        while(!this.signal) wait();
	        this.signal = false;
	    }
	}


	 //Student thread, it will alternate between programming and seeking help from the TA.
	
	class Student implements Runnable
	{       
	    // Time to program before asking for help (in seconds).
	    private int programTime;
	    
	    // Student number.
	    private int studentNum;

	    // Semaphore used to wakeup TA.
	    private SignalSemaphore wakeup;

	    // Semaphore used to wait in chairs outside office.
	    private Semaphore chairs;

	    // Mutex lock (binary semaphore) used to determine if TA is available.
	    private Semaphore available;

	    // A reference to the current thread.
	    private Thread t;

	    // Non-default constructor.
	    public Student(int program, SignalSemaphore w, Semaphore c, Semaphore a, int num)
	    {
	        programTime = program;    
	        wakeup = w;
	        chairs = c;
	        available = a;
	        studentNum = num;
	        t = Thread.currentThread();
	    }

	    
	     //The run method will infinitely loop between programming and asking for help until the thread is interrupted.
	    
	    @Override
	    public void run()
	    {
	        // Infinite loop.
	        while(true)
	        {
	            try
	            {
	               // Program first.
	               System.out.println("Student " + studentNum + " has started programming for " + programTime + " seconds.");
	               Thread.sleep(programTime * 1000);
	                
	               // Check to see if TA is available first.
	               System.out.println("Student " + studentNum + " is checking to see if TA is available.");
	               if (available.tryAcquire())
	               {
	                   try
	                   {
	                       // Wakeup the TA.
	                       wakeup.take();
	                       System.out.println("Student " + studentNum + " has woke up the TA.");
	                       System.out.println("Student " + studentNum + " has started working with the TA.");
	                       Thread.sleep(5000);
	                       System.out.println("Student " + studentNum + " has stopped working with the TA.");
	                   }
	                   catch (InterruptedException e)
	                   {
	                       continue;
	                   }
	                   finally
	                   {
	                       available.release();
	                   }
	               }
	               else
	               {
	                   // Check to see if any chairs are available.
	                   System.out.println("Student " + studentNum + " could not see the TA.  Checking for available chairs.");
	                   if (chairs.tryAcquire())
	                   {
	                       try
	                       {
	                           // Wait for TA to finish with other student.
	                           System.out.println("Student " + studentNum + " is sitting outside the office.  "
	                                   + "Student is #" + ((3 - chairs.availablePermits())) + " in line.");
	                           available.acquire();
	                           System.out.println("Student " + studentNum + " has started working with the TA.");
	                           t.sleep(5000);
	                           System.out.println("Student " + studentNum + " has stopped working with the TA.");
	                           available.release();
	                       }
	                       catch (InterruptedException e)
	                       {
	                           // Something bad happened.
	                           continue;
	                       }
	                   }
	                   else
	                   {
	                       System.out.println("Student " + studentNum + " could not see the TA and all chairs were taken.  Back to programming!");
	                   }
	               }
	            }
	            catch (InterruptedException e)
	            {
	                break;
	            }
	        }
	    }
	}

	class TeachingAssistant implements Runnable
	{
	    // Semaphore used to wakeup TA.
	    private SignalSemaphore wakeup;

	    // Semaphore used to wait in chairs outside office.
	    private Semaphore chairs;

	    // Mutex lock (binary semaphore) used to determine if TA is available.
	    private Semaphore available;

	    // A reference to the current thread.
	    private Thread t;
	    
	    public TeachingAssistant(SignalSemaphore w, Semaphore c, Semaphore a)
	    {
	        t = Thread.currentThread();
	        wakeup = w;
	        chairs = c;
	        available = a;
	    }
	    
	    @Override
	    public void run()
	    {
	        while (true)
	        {
	            try
	            {
	                System.out.println("No students waiting.  The TA is going to sleep.");
	                wakeup.release();
	                System.out.println("A student woke up the Teacher Assistant");
	                
	                t.sleep(5000);
	                
	                // If there are other students waiting.
	                if (chairs.availablePermits() != 3)
	                {
	                    do
	                    {
	                        t.sleep(5000);
	                        chairs.release();
	                    }
	                    while (chairs.availablePermits() != 3);                   
	                }
	            }
	            catch (InterruptedException e)
	            {
	                continue;
	            }
	        }
	    }
	
}
