// adapted from http://www.geeksforgeeks.org/dynamic-programming-set-7-coin-change/
class CoinChangeDP {

    static int coinChangeDP( int S[], int m, int n )
    {
        int i, j, x, y;

        // We need n+1 rows as the table is consturcted in bottom up manner using
        // the base case 0 value case (n = 0)
        int[][] table = new int[n+1][m];

        // Fill the enteries for 0 value case (n = 0)
        for (i=0; i<m; i++)
            table[0][i] = 1;

        // Fill rest of the table enteries in bottom up manner
        for (i = 1; i < n+1; i++)
        {
            for (j = 0; j < m; j++)
            {
                // Count of solutions including S[j]
                x = (i-S[j] >= 0)? table[i - S[j]][j]: 0;

                // Count of solutions excluding S[j]
                y = (j >= 1)? table[i][j-1]: 0;

                // total count
                table[i][j] = x + y;
            }
        }
        return table[n][m-1];
    }

    // Driver program to test above function
    public static void main(String[] args)
    {

        int[] arr = new int[args.length - 1];

        for(int i = 0; i < arr.length; i++)
        {
            arr[i] = Integer.parseInt(args[i]);
        }

        int m = arr.length;
        int V = Integer.parseInt(args[args.length - 1]);
        System.out.println(coinChangeDP(arr, m, V));

    }

}