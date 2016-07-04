package ggraver;

import java.io.FileInputStream;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

// analyses source code for invalid solutions
public class SourceAnalyser {

    // analyse the user-submitted .java file for code errors/invalid solutions (e.g. recursion)
    public void analyseSource() throws Exception {

        // creates an input stream for the file to be parsed
        FileInputStream in;

        try {
            in = new FileInputStream("sample/FileToParse.java");
        }
        catch(Exception e) {
            in = null;
            e.printStackTrace();
        }

        CompilationUnit cu = null;

        // parse the file
        try {
            cu = JavaParser.parse(in);
            in.close();
        } catch(Exception e) {
            throw new Exception("Invalid .java file.");
        }

        MethodVis mv = new MethodVis();
        mv.visit(cu, null);

    }

}
