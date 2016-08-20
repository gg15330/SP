package org.dplib.display;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;

import java.awt.*;

/**
 * Created by george on 19/08/16.
 */

// custom chart panel with separate colours for tutor and student data plots
class CustomChartPanel
extends ChartPanel
{

    CustomChartPanel(JFreeChart jFreeChart)
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
