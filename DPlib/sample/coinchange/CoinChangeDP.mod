�� sr org.dplib.analyse.Model�tj�M�� L callingMethodBodyt Ljava/lang/String;L callingMethodDeclarationq ~ L 	classNameq ~ L descriptionq ~ L methodToAnalyseDeclarationq ~ L methodToAnalyseNameq ~ xr org.dplib.analyse.Analysis�WiE��� L problemTypet Lorg/dplib/analyse/ProblemType;L resultst Ljava/util/List;xp~r org.dplib.analyse.ProblemType          xr java.lang.Enum          xpt 	ITERATIVEsr java.util.ArrayListx����a� I sizexp   w   sr org.dplib.analyse.Resulto թ0O7 J executionTime[ inputt [Ljava/lang/String;L outputq ~ xp       ?ur [Ljava.lang.String;��V��{G  xp   t 9t 6t 5t 1t 11t 6sq ~        huq ~    t 25t 10t 5t 30t 5xt{
    int[] arr = new int[args.length - 1];
    for (int i = 0; i < arr.length; i++) {
        arr[i] = Integer.parseInt(args[i]);
    }
    int m = arr.length;
    int V = Integer.parseInt(args[args.length - 1]);
    System.out.println(coinChangeDP(arr, m, V));
}t &public static void main(String[] args)t CoinChangeDPt Problem descriptiont .static int coinChangeDP(int S[], int m, int n)t coinChangeDP