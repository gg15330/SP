import java.util.List;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.MemoryType;

public class dfib {
    static Runtime runtime;
    static long memory;


    static int dfib(int n)
    {
        /* Declare an array to store Fibonacci numbers. */
    int f[] = new int[n+1];
    int i;
    /* 0th and 1st number of the series are 0 and 1*/
    f[0] = 0;
    f[1] = 1;

    for (i = 2; i <= n; i++)
    {

        memory = runtime.totalMemory() - runtime.freeMemory();

        // Calculate the used memory
        long memory = runtime.totalMemory() - runtime.freeMemory();
       /* Add the previous 2 numbers in the series
         and store it */

        f[i] = f[i-1] + f[i-2];
                System.out.println(memory);
    }

    return f[n];
    }

    public static void main (String args[])
    {

    // Get the Java runtime
    runtime = Runtime.getRuntime();

    int n = 45;
    System.out.println("Computing DP solution...");
    System.out.println(dfib(n) + "\n");

    System.out.println("Memory used: " + memory);

    }
}
