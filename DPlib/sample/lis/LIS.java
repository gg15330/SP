import java.util.*;
import java.lang.*;

public class LIS {

    private static int[] arr;
    private static int max_ref;
    private static int[] solution;

    public static void main(String[] args) {

        int[] A = new int[args.length];

        for(int i = 0; i < args.length; i++)
        {
            A[i] = Integer.parseInt(args[i]);
        }

        int l = lisDP(A, (A.length));
        System.out.println(l);

    }

    private static int lisDP(int[] arr, int n) {
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
