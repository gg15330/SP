package ggraver;

import java.io.File;

import com.github.javaparser.ParseException;

//overall program control
public class Main {

    private File file;
    private SourceAnalyser sa;

    public Main(String file) {

        this.file = new File(file);

        if(!this.file.exists() || this.file.isDirectory()) {
            System.err.println("\nInvalid file name: " + file + "\n");
            System.exit(1);
        }

    }

    // handle command line arguments
    public static void main(String[] args) {

        if(args.length != 1) {
            System.out.println("\nUsage: java -jar DPLib-1.0-SNAPSHOT.jar <path/to.file.java>\n");
            System.exit(1);
        }

        Main main = new Main(args[0]);
        main.run();

    }

    public void run() {

        sa = new SourceAnalyser(file, "Dynamic");

        try {
            sa.analyse();
            // analyseClass();
        }
        catch(Exception e) {
            e.printStackTrace();
            System.out.println();
        }

    }

}
