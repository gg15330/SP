import java.lang.*;
import java.io.*;
import java.util.*;

// open a file and print it to the console
public class Parser {

	public static void main(String[] args) {
		if(args.length != 1) {
			System.err.println("Incorrect number of arguments.\n");
			System.exit(1);
		}
		else {
			File file = new File("./" + args[0]);
			System.out.println("Opening file: " + file + "\n");
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
			}
		}
	}

}