package org.ggraver.DPlib;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;

/**
 * Created by george on 05/08/16.
 */
class SwingDisplay
{

    void createAndShowGUI()
    {
//        text scroll panes
        JScrollPane editorScrollPane = createTextAreaWithScrollPane("Editor");
        JScrollPane terminalScrollPane = createTextAreaWithScrollPane("Terminal");

//        button
        JButton solveBtn = new JButton();
        solveBtn.setText("Solve");

//        executionTimeGraph
        Dimension chartDimension = new Dimension(200, 300);
        ChartPanel executionTimeChartPanel = createChart("Execution Time",
                                                    null,
                                                    "Time (ms)",
                                                    createDataset(),
                                                    PlotOrientation.VERTICAL,
                                                    chartDimension);

//        instructionCountGraph
        ChartPanel instructionCountChartPanel = createChart("Instruction",
                                                            null,
                                                            "Instructions executed",
                                                            createDataset(),
                                                            PlotOrientation.VERTICAL,
                                                            chartDimension);

//        ioPanel
        JPanel ioPanel = createIOPanel(editorScrollPane, terminalScrollPane);

//        graphPanel
        JPanel graphPanel = createGraphPanel(solveBtn, executionTimeChartPanel, instructionCountChartPanel);

//        mainPanel
        JPanel mainPanel = createMainPanel(ioPanel, graphPanel);

        JFrame frame = new JFrame("Test gui");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(800, 600));
        frame.setContentPane(mainPanel);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel createMainPanel(JPanel ioPanel, JPanel graphPanel)
    {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc. gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(ioPanel, gbc);

        gbc.gridx = 1;
        gbc. gridy = 0;
        gbc.weightx = 0.25;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(graphPanel, gbc);
        return mainPanel;
    }

    private JPanel createGraphPanel(JButton solveBtn, JPanel executionTimeChartPanel, JPanel instructionCountChartPanel)
    {
        JPanel graphPanel = new JPanel();
        BoxLayout graphPanelLayout = new BoxLayout(graphPanel, BoxLayout.Y_AXIS);
        graphPanel.setLayout(graphPanelLayout);
        graphPanel.setBorder(new EtchedBorder());
        graphPanel.add(solveBtn);
        graphPanel.add(executionTimeChartPanel);
        graphPanel.add(instructionCountChartPanel);
        return graphPanel;
    }

    private JPanel createIOPanel(JScrollPane editorScrollPane, JScrollPane terminalScrollPane)
    {
        JPanel ioPanel = new JPanel();
        ioPanel.setLayout(new GridBagLayout());
        ioPanel.setBorder(new EtchedBorder());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc. gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        ioPanel.add(editorScrollPane, gbc);

        gbc.gridx = 0;
        gbc. gridy = 1;
        gbc.weightx = 0;
        gbc.weighty = 0.5;
        gbc.fill = GridBagConstraints.BOTH;
        ioPanel.add(terminalScrollPane, gbc);
        return ioPanel;
    }

    private ChartPanel createChart(String name,
                                   String xLabel,
                                   String yLabel,
                                   CategoryDataset dataSet,
                                   PlotOrientation orientation,
                                   Dimension size)
    {
        JFreeChart jFreeChart = ChartFactory.createBarChart(
                name,
                xLabel,
                yLabel,
                dataSet,
                orientation,
                true,
                true,
                false);
        ChartPanel chartPanel = new ChartPanel(jFreeChart);
        chartPanel.setPreferredSize(size);
        return chartPanel;
    }

    private JScrollPane createTextAreaWithScrollPane(String text)
    {
        JTextArea jTextArea = new JTextArea();
        jTextArea.setLineWrap(true);
        jTextArea.setText(text);
        JScrollPane scrollPane = new JScrollPane(jTextArea);
        scrollPane.setPreferredSize(new Dimension(1, 1));
        return scrollPane;
    }


    private CategoryDataset createDataset()
    {
        final String model = "Model";
        final String user = "User";
        final String value = "";
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(150000000 , model, value);
        dataset.addValue(140000000 , user , value);
        return dataset;
    }

}
