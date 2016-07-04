import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

// analyses student's solution to modelled problem and gives feedback accordingly
public class Solver {

    public static void main(String[] args) {
        Solver solver = new Solver();

        if(args.length != 2) {
            System.err.println("Incorrect number of arguments.\n");
            System.exit(1);
        }
        else {
            // solver.openFile(args[0]);
            solver.printMethod(args[0]);
        }
    }

    private void printFile(String f) {
        File file = openFile(f);
        try {
            Scanner s = new Scanner(file);
            // use hasNextLine instead of hasNext to account for whitespace
            while(s.hasNextLine()) {
                System.out.println(s.nextLine());
            }
            s.close();
        } catch(FileNotFoundException e) {
            System.err.println("File not found.\n");
            e.printStackTrace();
        } catch(Exception e) {
            System.err.println("Something else went wrong:");
            e.printStackTrace();
        }
    }

    private void printMethod(String f) {
        Class<fibonacci> fib = fibonacci.class;
        for (Method method : fib.getDeclaredMethods()) {
            System.out.println("Method name: " + method + "");
            System.out.println("Description: \n" + method.getCode() + "\n");
    		if (method.isAnnotationPresent(Test.class)) {
                System.out.println("Test found");
            }
        }
    }

    private File openFile(String f) {
        File file = new File(f);
        System.out.println("Opening file: " + file + "\n");
        return file;
    }
}
