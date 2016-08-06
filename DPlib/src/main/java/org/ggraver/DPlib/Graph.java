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
extends BarChart
{
    Graph(CategoryAxis x, NumberAxis y,
          String title, String yLabel,
          long modelVal, long userVal)
    {
        super(x, y);
        this.setTitle(title);
        y.setLabel(yLabel);
        XYChart.Series model = createSeries("Model", modelVal);
        XYChart.Series user = createSeries("User", userVal);
        getData().addAll(model, user);
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