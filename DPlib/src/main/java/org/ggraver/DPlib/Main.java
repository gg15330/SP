package org.ggraver.DPlib;

import java.io.File;
import java.io.FileNotFoundException;

import com.github.javaparser.ParseException;

//overall program control
public class Main {

    private File file;
    private String methodName;

    private Main(String file, String methodName) {

        this.file = new File(file);

        if (!this.file.exists() || this.file.isDirectory()) {
            System.err.println("\nInvalid file name: " + file + "\n");
            System.exit(1);
        }

        this.methodName = methodName;

    }

    // handle command line arguments
    public static void main(String[] args) {

        System.out.println("Starting up...");

        if (args.length != 2) {
            System.out.println("\nUsage: java -jar DPLib-1.0-SNAPSHOT.jar <path/to.file.java>\n");
            System.exit(1);
        }

        Main main = new Main(args[0], args[1]);
        main.run();

    }

    private void run() {

        try {
            SourceAnalyser sa = new SourceAnalyser();
            sa.parse(file);
            sa.analyse();
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
            System.err.println("\nFile not found.\n");
            System.exit(1);
        } catch (ParseException pe) {
            pe.printStackTrace();
            System.err.println("\nFile could not be parsed, please ensure file is a valid .java class.\n");
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            ClassAnalyser ca = new ClassAnalyser(file, methodName);
            ca.analyse();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
