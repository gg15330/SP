class fib
{
    @Dynamic
    static int fib(int n)
    {
    if (n <= 1)
       return n;
    return fib(n-1) + fib(n-2);
    }

    static int dfib(int n)
    {
        @Dynamic
        /* Declare an array to store Fibonacci numbers. */
    int f[] = new int[n+1];
    int i;
    /* 0th and 1st number of the series are 0 and 1*/
    f[0] = 0;
    f[1] = 1;

    for (i = 2; i <= n; i++)
    {
       /* Add the previous 2 numbers in the series
         and store it */
        f[i] = f[i-1] + f[i-2];
    }

    return f[n];
    }

    public static void main (String args[])
    {

    @onething
    int n = 45;
    System.out.println("Computing recursive solution...");

    long t1 = System.nanoTime();
    System.out.println(fib(n) + "\n");
    long t2 = System.nanoTime();
    double sec = (double)((t2 - t1) / 1000000000.0);

    System.out.println("Recursive solution elapsed time: " + sec + "seconds\n");

    System.out.println("Computing DP solution...");

    t1 = System.nanoTime();
    System.out.println(dfib(n) + "\n");
    t2 = System.nanoTime();
    sec = (double)((t2 - t1) / 1000000000.0);

    System.out.println("DP solution elapsed time: " + sec + "seconds\n");

    }
}
