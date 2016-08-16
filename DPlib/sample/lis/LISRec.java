class LISRec {

    private static int[] arr;
    private static int max_ref;
    private static int[] solution;

    public static void main(String[] args) {

        int[] A = new int[args.length];

        for(int i = 0; i < args.length; i++)
        {
            A[i] = Integer.parseInt(args[i]);
        }

        int l = lisRec(A, (A.length));
        System.out.println(l);

    }

    private static int lisRec(int[] arr, int n) {
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
            res = lisRec(arr, i);
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

}