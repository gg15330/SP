package org.dplib.display;

import oracle.jrockit.jfr.JFR;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;

import java.awt.*;

/**
 * Created by george on 19/08/16.
 */
public class CustomChartPanel
extends ChartPanel
{

    public CustomChartPanel(JFreeChart jFreeChart)
    {
        super(jFreeChart);

        LineAndShapeRenderer renderer1 = new LineAndShapeRenderer();
        renderer1.setSeriesPaint(0, Color.RED);
        LineAndShapeRenderer renderer2 = new LineAndShapeRenderer();
        renderer2.setSeriesPaint(0, Color.GREEN);

        CategoryPlot plot = (CategoryPlot) jFreeChart.getPlot();
        plot.setRenderer(0, renderer1);
        plot.setRenderer(1, renderer2);

        setPreferredSize(new Dimension(1, 1));
    }

}
