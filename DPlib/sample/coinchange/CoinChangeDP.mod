�� sr org.dplib.Model�d��'m L callingMethodBodyt Ljava/lang/String;L callingMethodDeclarationq ~ L 	classNameq ~ L descriptionq ~ L methodToAnalyseDeclarationq ~ L resultst Ljava/util/List;L typet Lorg/dplib/ProblemType;xpt{
    int[] arr = new int[args.length - 1];
    for (int i = 0; i < arr.length; i++) {
        arr[i] = Integer.parseInt(args[i]);
    }
    int m = arr.length;
    int V = Integer.parseInt(args[args.length - 1]);
    System.out.println(coinChangeDP(arr, m, V));
}t &public static void main(String[] args)t CoinChangeDPt Problem descriptiont .static int coinChangeDP(int S[], int m, int n)sr java.util.ArrayListx����a� I sizexp   w   sr org.dplib.Result%�:���j J executionTime[ inputt [Ljava/lang/String;L outputq ~ xp       Mur [Ljava.lang.String;��V��{G  xp   t 9t 6t 5t 1t 11t 6sq ~        Luq ~    t 25t 10t 5t 30t 5x~r org.dplib.ProblemType          xr java.lang.Enum          xpt 	ITERATIVE