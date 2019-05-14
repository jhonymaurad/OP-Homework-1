import java.util.Arrays;
import java.util.Scanner;

public class SCAN 
{
    public static void main(String [] args)
    {
        Scanner input = new Scanner(System.in);
        int sum =0;
        int head;
        int [] queue;
        int max;
        int dloc = 0;  //location of disk(head) in array

        System.out.println("Enter number of disk locations: ");
        int qSize = Integer.parseInt(input.nextLine());

        queue = new int[qSize+1];
        System.out.println("Enter head position: ");
        head = Integer.parseInt(input.nextLine());

        System.out.println("Enter elements in disk queue: ");
        for(int i = 0; i < qSize; i++)
        {
            queue[i] = Integer.parseInt(input.nextLine());
        }

        queue[qSize] = head;
        qSize++;

        //sort disk locations queue
        Arrays.sort(queue);

        max = queue[qSize-1];

        for(int i = 0; i<qSize; i++)
        {
            if(head == queue[i]){
                dloc = i;
                break;
            }
        }

        for(int i = dloc; i>=0; i--)
        {
            System.out.print(queue[i] + " then -->");
        }

        System.out.print("0-->");

        for(int i=dloc+1; i<qSize; i++)
        {
            System.out.print(queue[i] + " then -->");
        }

        System.out.println();

        sum = head + max;
        System.out.println("Movement of total cylinders " + sum);

    }

}
