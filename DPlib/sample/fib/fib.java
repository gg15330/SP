import java.util.List;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.MemoryType;

class fib
{
    static Runtime runtime;
    static long memory;

    static int fib(int n)
    {

    // Get the Java runtime
    // Calculate the used memory
    memory = runtime.totalMemory() - runtime.freeMemory();

    if (n <= 1)
       return n;
    return fib(n-1) + fib(n-2);
    }

    public static void main (String args[])
    {

    runtime = Runtime.getRuntime();
    int n = 30;
    System.out.println("Computing recursive solution...");
    System.out.println(fib(n) + "\n");

    System.out.println("Memory used: " + memory);
    }
}
