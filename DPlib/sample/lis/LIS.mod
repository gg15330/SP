�� sr org.DPlib.ModelP��L���	 [ callingMethodBodyt [Ljava/lang/String;L callingMethodDeclarationt Ljava/lang/String;L 	classNameq ~ L descriptionq ~ L methodToAnalyseDeclarationq ~ L resultst Ljava/util/List;xpur [Ljava.lang.String;��V��{G  xp   t *String[] argsStrings = args[0].split(" ");t &int[] A = new int[argsStrings.length];t ]for (int i = 0; i < argsStrings.length; i++) {
    A[i] = Integer.parseInt(argsStrings[i]);
}t int l = lisDP(A, (A.length));t System.out.println(l);t &public static void main(String[] args)t LISt Problem descriptiont *private static int lisDP(int[] arr, int n)sr java.util.ArrayListx����a� I sizexp   w   sr org.DPlib.Resulta�Ǧ�	 J executionTime[ inputq ~ L outputq ~ xp       Buq ~    
t 10t 22t 9t 33t 21t 50t 41t 60t 80t 4t 1sq ~        Duq ~    
t 45t 1t 32t 2t 6t 345t 6t 5t 4t 4t 1sq ~        Duq ~    
t 2t 6t 5t 34t 77t 4t 22t 4t 67t 68t 1x