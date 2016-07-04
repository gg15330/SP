import java.util.*;
import java.lang.*;

public class LIS {

    private int[] arr;
    private int max_ref;
    private int[] solution;

    public static void main(String[] args) {
        LIS lis = new LIS();
        int[] A = { 10, 22, 9, 33, 21, 50, 41, 60, 80 };
        int[] B = lis.randarr();

        long t1 = System.nanoTime();
        System.out.println("l: " + Arrays.toString(B));
        int l = lis.rec(B, (B.length));
        System.out.println("Length of LIS = " + l);
        long t2 = System.nanoTime();
        double sec = (double)((t2 - t1) / 1000000000.0);
        System.out.println("Recursive solution elapsed time: " + sec + "seconds");
        
        t1 = System.nanoTime();
        System.out.println("l: " + Arrays.toString(B));
        l = lis.dyn(B, (B.length));
        System.out.println("Length of LIS = " + l);
        t2 = System.nanoTime();
        sec = (double)((t2 - t1) / 1000000000.0);
        System.out.println("Dynamic solution elapsed time: " + sec + " seconds");

    }

    // generate random number array
    private int[] randarr() {
        Random r = new Random();

        int[] rand = new int[20];
        for(int i = 0; i < 20; i++) {
            rand[i] = r.nextInt(20);
        }
        return rand;
    }

    // find recursive solution
    private int rec(int[] arr, int n)
    {
       // base case
       if (n == 1)
           return 1;

       // 'max_ending_here' is length of LIS ending with arr[n-1]
       int res, max_ending_here = 1;

        /* Recursively get all LIS ending with arr[0], arr[1] ...
           arr[n-2]. If   arr[i-1] is smaller than arr[n-1], and
           max ending with arr[n-1] needs to be updated, then
           update it */
        for (int i = 1; i < n; i++)
        {
            res = rec(arr, i);
            if (arr[i-1] < arr[n-1] && res + 1 > max_ending_here)
                max_ending_here = res + 1;
        }

        // Compare max_ending_here with the overall max. And
        // update the overall max if needed
        if (max_ref < max_ending_here)
           max_ref = max_ending_here;

        // Return length of LIS ending with arr[n-1]
        return max_ending_here;
   }

    private int dyn(int[] arr, int n) {
        int lis[] = new int[n];
        int i,j,max = 0;

        /* Initialize LIS values for all indexes */
         for ( i = 0; i < n; i++ )
            lis[i] = 1;

         /* Compute optimized LIS values in bottom up manner */
         for ( i = 1; i < n; i++ )
            for ( j = 0; j < i; j++ )
               if ( arr[i] > arr[j] && lis[i] < lis[j] + 1)
                  lis[i] = lis[j] + 1;

         /* Pick maximum of all LIS values */
         for ( i = 0; i < n; i++ )
            if ( max < lis[i] )
               max = lis[i];

          return max;
    }

}
