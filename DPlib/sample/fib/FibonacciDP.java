// sourced from http://www.geeksforgeeks.org/program-for-nth-fibonacci-number/
import java.lang.management.ManagementFactory;

class FibonacciDP {

    static int fibDP(int n)
    {
        int f[] = new int[n+1];
        int i;
        f[0] = 0;
        f[1] = 1;

        for (i = 2; i <= n; i++)
        {
            f[i] = f[i-1] + f[i-2];
        }

        return f[n];
    }

    public static void main (String[] args)
    {
        System.out.println("THREADS " + ManagementFactory.getThreadMXBean().getThreadCount());
        int n = 45;
        System.out.println(fibDP(n));
    }

}
