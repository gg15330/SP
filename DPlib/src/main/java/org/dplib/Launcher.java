package org.dplib;

import com.github.javaparser.ParseException;
import com.github.javaparser.ast.body.MethodDeclaration;
import org.dplib.analyse.*;
import org.dplib.compile.CompileException;
import org.dplib.compile.SourceCompiler;
import org.dplib.display.GUIController;
import org.dplib.io.IO;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static javafx.application.Application.launch;

// overall program control
public class Launcher
{

    private String command;
    private String javaFilePath;
    private String inputFilePath;
    private String modelFilePath;
    private String methodName;
    private final IO io = new IO();

    public static void main(String[] args)
    {
        Launcher launcher = new Launcher();
        launcher.processArgs(args);
        launcher.start();
    }

    private void start()
    {
        try
        {
            switch (command) {
                case "model":
                    FileHandler fileHandler = new FileHandler(javaFilePath, "java");
                    FileHandler inputFileHandler = new FileHandler(inputFilePath, "txt");
                    String[][] inputs = inputFileHandler.parseInputTextFile(inputFileHandler.getFile());

                    String sourceCode = fileHandler.parseSourceFile(fileHandler.getFile());

                    SourceAnalyser sa = new SourceAnalyser();
                    sa.parse(sourceCode);

                    MethodDeclaration methodToAnalyse = sa.locateMethod(methodName);
                    MethodDeclaration callingMethod = sa.locateMethod("main");

                    File classFile = new SourceCompiler().compile(fileHandler.getFile(), sa.getClassName());
                    List<Result> results = new ClassAnalyser().analyse(classFile, inputs);

                    Model model = new Modeler().model(sa.getClassName(),
                                                      methodToAnalyse,
                                                      callingMethod,
                                                      sa.determineProblemType(methodToAnalyse),
                                                      results);

                    io.displayResults(model.getResults());
                    System.out.println("Creating model file...");
                    fileHandler.serializeModel(model);
                    System.out.println("Model file created.");
                    break;
                case "solve":
                    new GUIController(modelFilePath).start();
                    break;
                default: throw new Error("Invalid command: " + command);
            }
        }
        catch (IOException | AnalysisException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
        catch (CompileException | ParseException e)
        {
            e.printStackTrace();
        }
    }

    private void processArgs(String[] args)
    {
        if(args.length == 4)
        {
            command = args[0];
            javaFilePath = args[1];
            inputFilePath = args[2];
            methodName = args[3];
            if(!command.equals("model"))
            {
                io.usage();
                System.exit(1);
            }
        }
        else if(args.length == 1)
        {
            command = "solve";
            modelFilePath = args[0];
        }
        else
        {
            io.usage();
            System.exit(1);
        }
    }

}
