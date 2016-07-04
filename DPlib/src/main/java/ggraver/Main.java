package ggraver;

//overall program control
public class Main {

    public static void main(String[] args) {

        Main cp = new Main();
        SourceAnalyser sa = new SourceAnalyser();

        try {
            sa.analyseSource();
            // analyseClass();
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println();
        }

    }

}
