�� sr org.dplib.analyse.Model�tj�M�� L callingMethodBodyt Ljava/lang/String;L callingMethodDeclarationq ~ L 	classNameq ~ L descriptionq ~ L methodToAnalyseDeclarationq ~ L methodToAnalyseNameq ~ xr org.dplib.analyse.Analysis�WiE��� L problemTypet Lorg/dplib/analyse/ProblemType;L resultst Ljava/util/List;xp~r org.dplib.analyse.ProblemType          xr java.lang.Enum          xpt 	ITERATIVEsr java.util.ArrayListx����a� I sizexp   w   sr org.dplib.analyse.Resulto թ0O7 J executionTime[ inputt [Ljava/lang/String;L outputq ~ xp       >ur [Ljava.lang.String;��V��{G  xp   
t 10t 22t 9t 33t 21t 50t 41t 60t 80t 4t 6sq ~        Kuq ~    
t 45t 1t 32t 2t 6t 345t 6t 5t 4t 4t 4sq ~        xuq ~    
t 2t 6t 5t 34t 77t 4t 22t 4t 67t 68t 5xt �{
    int[] A = new int[args.length];
    for (int i = 0; i < args.length; i++) {
        A[i] = Integer.parseInt(args[i]);
    }
    int l = lisDP(A, (A.length));
    System.out.println(l);
}t &public static void main(String[] args)t LISDPt Problem descriptiont *private static int lisDP(int[] arr, int n)t lisDP