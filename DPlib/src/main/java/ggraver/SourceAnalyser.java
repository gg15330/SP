package ggraver;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;
import java.util.HashMap;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

// analyses source code for invalid solutions/errors
public class SourceAnalyser {

    private File file;
    private String methodName;
    private Map<String, BlockStmt> methodList;

    public SourceAnalyser(File file, String methodName) {

        this.file = file;
        this.methodName = methodName;
        methodList = new HashMap<String, BlockStmt>();

    }

    // analyse the user-submitted .java file for code errors/invalid solutions (e.g. recursion)
    public void analyse() throws Exception {

        FileInputStream fis = new FileInputStream(file);
        CompilationUnit cu = JavaParser.parse(fis);
        fis.close();
        // MethodVis mv = new MethodVis();
        MV mv = new MV();
        mv.visit(cu, null);

        for(Map.Entry<String, BlockStmt> entry : methodList.entrySet()) {
            String name = entry.getKey();
            BlockStmt body = entry.getValue();
            // System.out.println("Name: " + name + "\nBody: " + body);
        }

        checkMethodExists();
        // mv.checkMethodExists(methodName);

    }

    private void checkMethodExists() throws Exception {

        for(String name : methodList.keySet()) {
            if(name.equals(methodName)) {
                return;
            }
        }

        throw new Exception("Required method \"" + methodName + "\" does not exist. " +
        "\nPlease check source file.\n");

    }

    private class MV extends VoidVisitorAdapter<Object> {

        // get information from all methods in the source file
        @Override
        public void visit(MethodDeclaration n, Object arg) {

            methodList.put(n.getName(), n.getBody());

        }

    }

}
