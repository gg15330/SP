// adapted from https://stackoverflow.com/questions/7875380/recursive-fibonacci-memoization

// this file is for testing the determineProblemType() method in the SourceAnalyser class -
// it will not work if you run it
class FibonacciMemo {
    // Get Fibonacci with Memoization
    public static  int fibMem(int[] dictionary, int n) {
        if (dictionary == null) {
            dictionary = new int[n];
        }

        if (dictionary[n - 1] == 0) {
            if (n <= 2) {
                dictionary[n - 1] = n - 1;
            } else {
                dictionary[n - 1] = fibMem(dictionary, (n - 1)) + fibMem(dictionary, (n - 2));
            }
        }

        return dictionary[n - 1];
    }

    public static void main(String[] args)
    {
        int n = 5;
        int[] dictionary = new int[n];
        System.out.println(fibMem(dictionary, n));
    }
}