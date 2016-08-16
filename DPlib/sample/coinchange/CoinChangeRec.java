// adapted from http://www.geeksforgeeks.org/find-minimum-number-of-coins-that-make-a-change/

// A Naive recursive JAVA program to find minimum of coins
// to make a given change V
class CoinChangeRec
{
    // m is size of coins array (number of different coins)
    static int coinChangeRec(int coins[], int m, int V)
    {
        // base case
        if (V == 0) return 0;

        // Initialize result
        int res = Integer.MAX_VALUE;

        // Try every coin that has smaller value than V
        for (int i=0; i<m; i++)
        {
            if (coins[i] <= V)
            {
                int sub_res = coinChangeRec(coins, m, V-coins[i]);

                // Check for INT_MAX to avoid overflow and see if
                // result can minimized
                if (sub_res != Integer.MAX_VALUE && sub_res + 1 < res)
                    res = sub_res + 1;
            }
        }
        return res;
    }

    public static void main(String args[])
    {
        int[] coins = new int[args.length - 1];

        for(int i = 0; i < coins.length; i++)
        {
            coins[i] = Integer.parseInt(args[i]);
        }

        int m = coins.length;
        int V = Integer.parseInt(args[args.length - 1]);
        System.out.println(coinChangeRec(coins, m, V));
    }

}/* This code is contributed by Rajat Mishra */