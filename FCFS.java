import java.util.Scanner;

public class FCFS {

    public static void main(String [] args)
    {
        Scanner input = new Scanner(System.in);
        
        int seek = 0;
        int diff = 0;
        int head = 0;
        int[] queue;

        System.out.println("Enter queue size: ");
        int qSize = Integer.parseInt(input.nextLine());

        queue = new int[qSize];

        System.out.println("Input Queue elements");
        
        for(int i = 0; i < qSize; i++)
        {
            queue[i] = Integer.parseInt(input.nextLine());
        }

        System.out.println("Enter initial head position");
        head  = Integer.parseInt(input.nextLine());

        queue[0] = head;

        for(int i = 0; i < qSize-1; i++)
        {
            diff = Math.abs(queue[i+1] - queue[i]);
            seek += diff;
            System.out.println("Move " +queue[i] + " to " + queue[i+1] + " with seek " + diff);

        }

        System.out.println("Total Amount of head movement is: " + seek);
        input.close();
    }
}