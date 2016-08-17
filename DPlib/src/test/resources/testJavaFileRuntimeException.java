class testJavaFileRuntimeException {

    static int fibDP(int n)
    {
        String s = String.valueOf(n);
        String s2 = s + "wefwf";
        int i = Integer.parseInt(s2);
        return i;
    }

    public static void main (String[] args)
    {
        int n = Integer.parseInt(args[0]);
        System.out.println(fibDP(n));
    }

}
