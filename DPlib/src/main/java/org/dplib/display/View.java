package org.dplib.display;

import org.dplib.analyse.Model;
import org.dplib.analyse.Result;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Created by george on 05/08/16.
 */
public class View
{
    private final Model model;
    private Editor editor;
    private final Terminal terminal = new Terminal(new JTextArea());
    private JButton solveBtn = new JButton();
    private CustomChartPanel executionTimeChartPanel;
    private CustomChartPanel outputChartPanel;
    private DefaultCategoryDataset tutorTimeDataset;
    private DefaultCategoryDataset tutorOutputDataset;

    public View(Model model, String editorText)
    {
        editor = new Editor(new JTextArea(), editorText);
        this.model = model;
    }

    public void createAndShowGUI()
    {

//        button
        solveBtn.setText("Solve");
        JPanel btnPanel = new JPanel();
        btnPanel.add(solveBtn);

//        executionTimeGraph
        tutorTimeDataset = createDataset(model, "time");
        JFreeChart executionTimeChart = ChartFactory.createLineChart(
                "Execution Time",
                null,
                "Time (ms)",
                tutorTimeDataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false);

        executionTimeChartPanel = new CustomChartPanel(executionTimeChart);

//        instructionCountGraph
        tutorOutputDataset = createDataset(model, "output");
        JFreeChart outputChart = ChartFactory.createLineChart(
                "Output",
                "Input",
                "Value",
                tutorOutputDataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false);

        outputChartPanel = new CustomChartPanel(outputChart);

//        panels
        JPanel ioPanel = createIOPanel(editor, terminal);
        JPanel graphPanel = createGraphPanel(btnPanel, executionTimeChartPanel, outputChartPanel);
        JPanel mainPanel = createMainPanel(ioPanel, graphPanel);

//        frame
        JFrame frame = new JFrame("dplib");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(800, 600));
        frame.setContentPane(mainPanel);
        frame.setResizable(true);
        frame.pack();
        frame.setLocationByPlatform(true);
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
        gbc.weightx = 0.5;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(graphPanel, gbc);
        return mainPanel;
    }

    private JPanel createGraphPanel(JPanel btnPanel, JPanel executionTimeChartPanel, JPanel outputChartPanel)
    {
        JPanel graphPanel = new JPanel();
        GridBagLayout graphPanelLayout = new GridBagLayout();
        graphPanel.setLayout(graphPanelLayout);
        graphPanel.setBorder(new EtchedBorder());

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc. gridy = 0;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.BOTH;
        graphPanel.add(btnPanel, gbc);

        gbc.gridx = 0;
        gbc. gridy = 1;
        gbc.weightx = 1;
        gbc.weighty = 5;
        gbc.fill = GridBagConstraints.BOTH;
        graphPanel.add(executionTimeChartPanel, gbc);

        gbc.gridx = 0;
        gbc. gridy = 2;
        gbc.weightx = 1;
        gbc.weighty = 5;
        gbc.fill = GridBagConstraints.BOTH;
        graphPanel.add(outputChartPanel, gbc);
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
        gbc.weighty = 0.8;
        gbc.fill = GridBagConstraints.BOTH;
        ioPanel.add(terminalScrollPane, gbc);
        return ioPanel;
    }


    private DefaultCategoryDataset createDataset(Model model, String type)
    {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for(int i = 0; i < model.getResults().size(); i++)
        {
            Result r = model.getResults().get(i);

            if("time".equals(type))
            {
                dataset.addValue(r.getExecutionTime(), "Tutor", String.valueOf(i));
            }
            else if("output".equals(type))
            {
                dataset.addValue(Long.parseLong(r.getOutput()), "Tutor", String.valueOf(i));
            }
        }

        return dataset;
    }

    public void addSolveBtnListener(ActionListener actionListener)
    {
        solveBtn.addActionListener(actionListener);
    }

    public String getEditorText()
    {
        return editor.getText();
    }

    public void setExecutionTimeGraph(CategoryDataset dataset)
    {
        executionTimeChartPanel.getChart().getCategoryPlot().setDataset(1, dataset);
    }

    public void setOutputGraph(CategoryDataset dataset)
    {
        outputChartPanel.getChart().getCategoryPlot().setDataset(1, dataset);
    }

    public void setTutorTimeData(DefaultCategoryDataset tutorTimeDataset)
    {
        this.tutorTimeDataset = tutorTimeDataset;
    }

    public void setTutorOutputData(DefaultCategoryDataset tutorOutputDataset)
    {
        this.tutorOutputDataset = tutorOutputDataset;
    }
}
