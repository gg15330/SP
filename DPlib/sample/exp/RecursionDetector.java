import java.util.Arrays;

public class RecursionDetector {

    private static boolean calledOnce;

    public static void main(String[] args) {
        calledOnce = false;
        System.out.println("Testing testFuncFlag...");
        long t1 = System.nanoTime();
        testFuncFlag();
        long t2 = System.nanoTime();
        long time = t2 -t1;
        System.out.println("\ntotal time: " + time + " ms\n");

        System.out.println("Testing testFuncStack...");
        t1 = System.nanoTime();
        testFuncStack();
        t2 = System.nanoTime();
        time = t2 -t1;
        System.out.println("\ntotal time: " + time + " ms\n");
    }

    public static void testFuncFlag() {
        if(calledOnce) {
            System.out.println("Recursion detected! Good job.\n");
            return;
        }

        System.out.println("Calling function...");
        calledOnce = true;
        testFuncFlag();
    }
 
    public static void testFuncStack() {
        // System.out.println("METHOD: " + method);
        // System.out.println("Stack trace: \n" + Arrays.toString(stacktrace));
        String method = Thread.currentThread().getStackTrace()[2].getMethodName();

        if(method.equals("testFuncStack")) {
            StackTraceElement[] st = Thread.currentThread().getStackTrace();
            int line = st[2].getLineNumber();
            System.out.println("Recursion detected at line " + line + "\n");
            System.out.println("Stack trace:\n" + Arrays.toString(st));
            return;
        }

        System.out.println("Calling function...");
        testFuncStack();
    }
}
