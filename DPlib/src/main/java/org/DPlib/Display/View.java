package org.DPlib.Display;

import org.DPlib.CustomOutputStream;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.PrintStream;

/**
 * Created by george on 05/08/16.
 */
public class View
{
    private JTextArea editor = new JTextArea();
    private JTextArea terminal = new JTextArea();
    private JButton solveBtn = new JButton();
    private ChartPanel executionTimeChartPanel;
    private ChartPanel outputChartPanel;
    private DefaultCategoryDataset tutorTimeData;
    private DefaultCategoryDataset tutorOutputData;

    void createAndShowGUI()
    {
//        editor
        Font font = new Font(null, Font.BOLD, 12);
        editor.setFont(font);
        editor.setForeground(Color.DARK_GRAY);
        JScrollPane editorScrollPane = createTextAreaWithScrollPane(editor, true, false);
        editorScrollPane.setBorder(new TitledBorder("Editor"));

//        terminal with output redirected from System.out and System.err
        terminal.setFont(font);
        terminal.setForeground(Color.DARK_GRAY);
        terminal.setBackground(Color.LIGHT_GRAY);
        JScrollPane terminalScrollPane = createTextAreaWithScrollPane(terminal, false, true);
        terminalScrollPane.setBorder(new TitledBorder("Console"));

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
                                                    tutorTimeData,
                                                    PlotOrientation.VERTICAL);
        executionTimeChartPanel.setPreferredSize(new Dimension(1, 1));

//        instructionCountGraph
        outputChartPanel = createChart("Output",
                                       "Input",
                                       "Value",
                                       tutorOutputData,
                                       PlotOrientation.VERTICAL);
        outputChartPanel.setPreferredSize(new Dimension(1, 1));

//        panels
        JPanel ioPanel = createIOPanel(editorScrollPane, terminalScrollPane);
        JPanel graphPanel = createGraphPanel(btnPanel, executionTimeChartPanel, outputChartPanel);
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

    private ChartPanel createChart(String name,
                                   String xLabel,
                                   String yLabel,
                                   CategoryDataset dataSet,
                                   PlotOrientation orientation)
    {
        JFreeChart jFreeChart = ChartFactory.createLineChart(
                name,
                xLabel,
                yLabel,
                dataSet,
                orientation,
                true,
                true,
                false);

        LineAndShapeRenderer renderer1 = new LineAndShapeRenderer();
        renderer1.setSeriesPaint(0, Color.RED);
        LineAndShapeRenderer renderer2 = new LineAndShapeRenderer();
        renderer2.setSeriesPaint(0, Color.GREEN);

        CategoryPlot plot = (CategoryPlot) jFreeChart.getPlot();
        plot.setRenderer(0, renderer1);
        plot.setRenderer(1, renderer2);

        ChartPanel chartPanel = new ChartPanel(jFreeChart);
        return chartPanel;
    }

    private JScrollPane createTextAreaWithScrollPane(JTextArea jTextArea, boolean editable, boolean wrap)
    {
        jTextArea.setLineWrap(wrap);
        jTextArea.setTabSize(2);
        jTextArea.setEditable(editable);
        JScrollPane scrollPane = new JScrollPane(jTextArea);
        scrollPane.setPreferredSize(new Dimension(1, 1));

        return scrollPane;
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

    public void setExecutionTimeGraph(CategoryDataset dataset)
    {
        executionTimeChartPanel.getChart().getCategoryPlot().setDataset(1, dataset);
    }

    public void setOutputGraph(CategoryDataset dataset)
    {
        outputChartPanel.getChart().getCategoryPlot().setDataset(1, dataset);
    }

    public void setTutorTimeData(DefaultCategoryDataset tutorTimeData)
    {
        this.tutorTimeData = tutorTimeData;
    }

    public void setTutorOutputData(DefaultCategoryDataset tutorOutputData)
    {
        this.tutorOutputData = tutorOutputData;
    }
}
