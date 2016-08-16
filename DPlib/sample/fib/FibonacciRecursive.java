// sourced from http://www.geeksforgeeks.org/program-for-nth-fibonacci-number/
class FibonacciRecursive
{
    static int fibRec(int n)
    {
        if (n <= 1)
        {
            return n;
        }
        return fibRec(n-1) + fibRec(n-2);
    }

    public static void main (String[] args)
    {
        int n = Integer.parseInt(args[0]);
        System.out.println(fibRec(n));
    }
}
