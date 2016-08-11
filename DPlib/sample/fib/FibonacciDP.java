// sourced from http://www.geeksforgeeks.org/program-for-nth-fibonacci-number/
import java.util.List;
import java.util.ArrayList;

class FibonacciDP {

    private List<String> strn = new ArrayList<>();

    static int fibDP(int n)
    {
        List<Integer> list = new ArrayList<>();
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
        int n = 45;
        System.out.println(fibDP(n));
    }

}
