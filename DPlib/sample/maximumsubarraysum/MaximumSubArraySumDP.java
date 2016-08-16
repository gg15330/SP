// sourced from http://algorithms.tutorialhorizon.com/dynamic-programming-maximum-subarray-problem/

import java.util.Arrays;

public class MaximumSubArraySumDP {

    //bottom up approach
    static int maximumSubArraySumDP(int [] a){
        int [] solution = new int[a.length+1];
        solution[0] = 0;

        for (int j = 1; j <solution.length ; j++) {
            solution[j] = Math.max(solution[j-1]+a[j-1],a[j-1]);
        }
        //this will print the solution matrix
        //System.out.println(Arrays.toString(solution));
        //now return the maximum in the solution array
        int result = solution[0];
        for (int j = 1; j <solution.length ; j++) {
            if(result<solution[j])
                result = solution[j];
        }

        return result;
    }

    public static void main(String[] args) {

        int[] A = new int[args.length];

        for(int i = 0; i < args.length; i++)
        {
            A[i] = Integer.parseInt(args[i]);
        }

        System.out.println( maximumSubArraySumDP(A));

    }



}