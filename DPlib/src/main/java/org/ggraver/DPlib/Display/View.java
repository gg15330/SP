package org.ggraver.DPlib.Display;

import org.ggraver.DPlib.CustomOutputStream;
import org.ggraver.DPlib.Result;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.PrintStream;

/**
 * Created by george on 05/08/16.
 */
class View
{
    private JTextArea editor = new JTextArea();
    private JTextArea terminal = new JTextArea();
    private JButton solveBtn = new JButton();
    private ChartPanel executionTimeChartPanel;
    private ChartPanel instructionCountChartPanel;

    void createAndShowGUI()
    {
//        editor
        JScrollPane editorScrollPane = createTextAreaWithScrollPane(editor, true);

//        terminal with output redirected from System.out and System.err
        JScrollPane terminalScrollPane = createTextAreaWithScrollPane(terminal, false);
        PrintStream terminalPrintStream = new PrintStream(new CustomOutputStream(terminal));
        System.setOut(terminalPrintStream);
        System.setErr(terminalPrintStream);

//        button
        solveBtn.setText("Solve");
        JPanel btnPanel = new JPanel();
        btnPanel.add(solveBtn);

//        executionTimeGraph
        executionTimeChartPanel = createChart("Execution Time",
                                                    null,
                                                    "Time (ms)",
                                                    createDataset(0, 0),
                                                    PlotOrientation.VERTICAL);
        executionTimeChartPanel.setPreferredSize(new Dimension(1, 1));

//        instructionCountGraph
        instructionCountChartPanel = createChart("Instructions",
                                                            null,
                                                            "Instructions (millions)",
                                                            createDataset(0, 0),
                                                            PlotOrientation.VERTICAL);
        instructionCountChartPanel.setPreferredSize(new Dimension(1, 1));

//        panels
        JPanel ioPanel = createIOPanel(editorScrollPane, terminalScrollPane);
        JPanel graphPanel = createGraphPanel(btnPanel, executionTimeChartPanel, instructionCountChartPanel);
        JPanel mainPanel = createMainPanel(ioPanel, graphPanel);

//        frame
        JFrame frame = new JFrame("DPlib");
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
        gbc.weightx = 0.25;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(graphPanel, gbc);
        return mainPanel;
    }

    private JPanel createGraphPanel(JPanel btnPanel, JPanel executionTimeChartPanel, JPanel instructionCountChartPanel)
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
        graphPanel.add(instructionCountChartPanel, gbc);
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

    private ChartPanel createChart(String name,
                                   String xLabel,
                                   String yLabel,
                                   CategoryDataset dataSet,
                                   PlotOrientation orientation)
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
        return chartPanel;
    }

    private JScrollPane createTextAreaWithScrollPane(JTextArea jTextArea, boolean editable)
    {
        jTextArea.setLineWrap(false);
        jTextArea.setTabSize(2);
        jTextArea.setEditable(editable);
        JScrollPane scrollPane = new JScrollPane(jTextArea);
        scrollPane.setPreferredSize(new Dimension(1, 1));

        return scrollPane;
    }

    private CategoryDataset createDataset(long modelVal, long userVal)
    {
        final String model = "Model";
        final String user = "User";
        final String value = "";
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(modelVal , model, value);
        dataset.addValue(userVal , user , value);
        return dataset;
    }

    void addSolveBtnListener(ActionListener actionListener)
    {
        solveBtn.addActionListener(actionListener);
    }

    String getEditorText()
    {
        return editor.getText();
    }

    void setEditorText(String s)
    {
        editor.setText(s);
    }

    void setExecutionTimeGraph(long modelExecutionTime, long userExecutionTime)
    {
        executionTimeChartPanel.getChart().getCategoryPlot().setDataset(createDataset(modelExecutionTime, userExecutionTime));
    }

    void setInstructionCountGraph(long modelInstructionCount, long userInstructionCount)
    {
        instructionCountChartPanel.getChart().getCategoryPlot().setDataset(createDataset(modelInstructionCount / 1000000, userInstructionCount / 1000000));
    }
}
