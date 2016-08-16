// sourced from http://www.geeksforgeeks.org/program-for-nth-fibonacci-number/
import java.util.List;
import java.util.ArrayList;

class FibonacciDPList {

    static int fibDP(int n)
    {
        List<Integer> f = new ArrayList<>();
        f.set(0, 0);
        f.set(1, 1);

        for (int i = 2; i <= n; i++)
        {
            f.set(i, (f.get(i-1) + f.get(i-2)));
        }

        return f.get(n);
    }

    public static void main (String[] args)
    {
        int n = Integer.parseInt(args[0]);
        System.out.println(fibDP(n));
    }

}
