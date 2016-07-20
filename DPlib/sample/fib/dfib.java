import java.util.List;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.MemoryType;

public class dfib {

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

       /* Add the previous 2 numbers in the series
         and store it */

        f[i] = f[i-1] + f[i-2];
    }

    return f[n];
    }

    public static void main (String args[])
    {

        // List<MemoryPoolMXBean> memoryPoolMXBeans = ManagementFactory.getMemoryPoolMXBeans();
        //
        // for(MemoryPoolMXBean mp : memoryPoolMXBeans) {
        //     if(mp.getName().equals("Code Cache")) {
        //         MemoryUsage pu = mp.getUsage();
        //         System.out.println("[USAGE] " + pu.getUsed());
        //     }
        // }

        int n = 45;
        System.out.println("Computing DP solution...");
        System.out.println(dfib(n) + "\n");

        // memoryPoolMXBeans = ManagementFactory.getMemoryPoolMXBeans();
        //
        // for(MemoryPoolMXBean mp : memoryPoolMXBeans) {
        //     if(mp.getName().equals("Code Cache")) {
        //         MemoryUsage pu = mp.getUsage();
        //         System.out.println("[USAGE] " + pu.getUsed());
        //     }
        // }

    }
}
