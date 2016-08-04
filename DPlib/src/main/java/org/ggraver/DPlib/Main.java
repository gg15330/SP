package org.ggraver.DPlib;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import org.ggraver.DPlib.Exception.AnalysisException;
import org.ggraver.DPlib.Exception.ModelingException;

import java.io.IOException;
import java.util.List;

// overall program control
public class Main
extends Application
{

    private Result result;

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void init()
    {
        IO io = new IO();
        List<String> args = getParameters().getRaw();

        try
        {
            io.processArgs(args.toArray(new String[args.size()]));
            FileHandler fileHandler = new FileHandler(io.getFilePath(), "java");
            switch (io.getCommand()) {
                case "model":
                    Model model = new Modeler().model(fileHandler.getFile(), io.getMethodName());
                    fileHandler.generateXML(model);
                    break;
                case "solve":
                    model = fileHandler.parseXML();
                    Solver solver = new Solver();
                    result = solver.solve(model, fileHandler.getFile());
                    io.displayResult(result);
                    break;
                default: throw new Error("Invalid command: " + io.getCommand());
            }
        }
        catch (IOException | ModelingException | AnalysisException e)
        {
            io.exit(e);
            System.exit(1);
        }
    }

    @Override
    public void start(Stage graph)
    throws Exception
    {
//        Parent root = FXMLLoader.load(getClass().getResource("fxml_example.fxml"));

        graph = new Graph("Summary",
                          "Execution Time(ms)",
                          result.getExecutionTime().getKey().getKey(),
                          result.getExecutionTime().getKey().getValue());
        Stage graph2 = new Graph("Instruction summary",
                                 "Instruction count",
                                 result.getInstructionCount().getKey().getKey(),
                                 result.getInstructionCount().getKey().getValue());
        graph.show();
        graph2.show();
    }

}
