package org.ggraver.DPlib;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
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
    public void start(Stage stage)
    throws Exception
    {
        stage.setTitle("Summary");

        CategoryAxis x = new CategoryAxis();
        x.setLabel("Execution time");
        NumberAxis y = new NumberAxis();
        y.setLabel("Time(ms)");

        BarChart<String, Number> barChart = new BarChart<>(x, y);
        barChart.setTitle("Program Performance");
        barChart.setCategoryGap(50.0);
        barChart.setBarGap(30.0);

        XYChart.Series modelSeries = createSeries("Model", result.getExecutionTime().getKey().getKey());
        XYChart.Series userSeries = createSeries("User", result.getExecutionTime().getKey().getValue());

        barChart.getData().addAll(modelSeries, userSeries);
        Scene scene = new Scene(barChart, 320, 240);
        stage.setScene(scene);
        stage.show();
    }

    private XYChart.Series createSeries(String name, long value)
    {
        XYChart.Series s = new XYChart.Series();
        s.setName(name);
        XYChart.Data<String, Long> data = new XYChart.Data<>("", value);
        s.getData().add(data);
        return s;
    }

}
