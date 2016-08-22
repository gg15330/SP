// sourced from http://www.geeksforgeeks.org/program-for-nth-fibonacci-number/
class FibonacciStackOverflow
{
    static String fibRec(int n)
    {
        if(n <= 1) return "t";
        String s = fibRec(n-1) + fibRec(n-2);
        return "G";
    }

    public static void main (String[] args)
    {
        int n = Integer.parseInt(args[0]);
        System.out.println(fibRec(n));
    }
}
