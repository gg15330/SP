import java.util.List;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.MemoryType;

class fib
{
    static int fib(int n)
    {

    // Get the Java runtime

    if (n <= 1)
       return n;
    return fib(n-1) + fib(n-2);
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
        System.out.println("Computing recursive solution...");
        System.out.println(fib(n) + "\n");

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
