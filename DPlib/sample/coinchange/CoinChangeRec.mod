�� sr org.dplib.Modelļ�ى� [ callingMethodBodyt [Ljava/lang/String;L callingMethodDeclarationt Ljava/lang/String;L 	classNameq ~ L descriptionq ~ L methodToAnalyseDeclarationq ~ L resultst Ljava/util/List;L typet Lorg/dplib/ProblemType;xpur [Ljava.lang.String;��V��{G  xp   t 'int[] coins = new int[args.length - 1];t vfor (int i = 0; i < coins.length; i++) {
    coins[i] = Integer.parseInt(args[i]);
    System.out.println(coins[i]);
}t int m = coins.length;t 0int V = Integer.parseInt(args[args.length - 1]);t /System.out.println(coinChangeRec(coins, m, V));t &public static void main(String args[])t CoinChangeRect Problem descriptiont 3static int coinChangeRec(int coins[], int m, int V)sr java.util.ArrayListx����a� I sizexp   w   sr org.dplib.Result%�:���j J executionTime[ inputq ~ L outputq ~ xp       Auq ~    
t 10t 22t 9t 33t 21t 50t 41t 60t 80t 4t 102293321504160802147483647sq ~        Duq ~    
t 45t 1t 32t 2t 6t 345t 6t 5t 4t 4t 45132263456541sq ~        Auq ~    
t 2t 6t 5t 34t 77t 4t 22t 4t 67t 3t 26534774224672147483647x~r org.dplib.ProblemType          xr java.lang.Enum          xpt 	RECURSIVE