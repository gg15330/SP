package org.ggraver.DPlib.Display;

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

/**
 * Created by george on 05/08/16.
 */
class SwingView
{
    private JTextArea editor = new JTextArea();
    private JTextArea terminal = new JTextArea();
    private JButton solveBtn = new JButton();
    private ChartPanel executionTimeChartPanel;
    private ChartPanel instructionCountChartPanel;

    void createAndShowGUI()
    {
        System.out.println("SwingView createAndShowGUI EDT: " + SwingUtilities.isEventDispatchThread());

//        text scroll panes
        JScrollPane editorScrollPane = createTextAreaWithScrollPane(editor, true);
        JScrollPane terminalScrollPane = createTextAreaWithScrollPane(terminal, false);

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

//        instructionCountGraph
        instructionCountChartPanel = createChart("Instructions",
                                                            null,
                                                            "Instructions (millions)",
                                                            createDataset(0, 0),
                                                            PlotOrientation.VERTICAL);

//        ioPanel
        JPanel ioPanel = createIOPanel(editorScrollPane, terminalScrollPane);

//        graphPanel
        JPanel graphPanel = createGraphPanel(btnPanel, executionTimeChartPanel, instructionCountChartPanel);

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

    private JPanel createGraphPanel(JPanel btnPanel, JPanel executionTimeChartPanel, JPanel instructionCountChartPanel)
    {
        JPanel graphPanel = new JPanel();
        BoxLayout graphPanelLayout = new BoxLayout(graphPanel, BoxLayout.Y_AXIS);
        graphPanel.setLayout(graphPanelLayout);
        graphPanel.setBorder(new EtchedBorder());
        graphPanel.add(btnPanel);
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
        gbc.weighty = 0.3;
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
        jTextArea.setLineWrap(true);
        jTextArea.setTabSize(4);
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

    void setTerminalText(String s) { terminal.setText(s); }

    void setExecutionTimeGraph(long modelExecutionTime, long userExecutionTime)
    {
        executionTimeChartPanel.getChart().getCategoryPlot().setDataset(createDataset(modelExecutionTime, userExecutionTime));
    }

    void setInstructionCountGraph(long modelInstructionCount, long userInstructionCount)
    {
        instructionCountChartPanel.getChart().getCategoryPlot().setDataset(createDataset(modelInstructionCount / 1000000, userInstructionCount / 1000000));
    }
}
