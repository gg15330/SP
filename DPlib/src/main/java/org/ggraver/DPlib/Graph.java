package org.ggraver.DPlib;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

/**
 * Created by george on 03/08/16.
 */
public class Graph
        extends Application
{

    void run()
    {
        launch();
    }

    @Override
    public void start(Stage stage)
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

        XYChart.Series model = createSeries("Model", 1000);
        XYChart.Series user = createSeries("User", 10000);

        barChart.getData().addAll(model, user);
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