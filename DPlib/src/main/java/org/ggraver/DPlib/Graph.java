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
class Graph
extends Stage
{

    Graph(String title, String yLabel, long modelVal, long userVal)
    {
        create(title, yLabel, modelVal, userVal);
    }

    void create(String title, String yLabel, long modelVal, long userVal)
    {
        this.setTitle(title);

        CategoryAxis x = new CategoryAxis();
        NumberAxis y = new NumberAxis();
        y.setLabel(yLabel);

        BarChart<String, Number> barChart = new BarChart<>(x, y);
        barChart.setTitle("Program Performance");
        barChart.setCategoryGap(50.0);
        barChart.setBarGap(30.0);

        XYChart.Series model = createSeries("Model", modelVal);
        XYChart.Series user = createSeries("User", userVal);

        barChart.getData().addAll(model, user);
        Scene scene = new Scene(barChart, 320, 240);
        this.setScene(scene);
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