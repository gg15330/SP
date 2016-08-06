package org.ggraver.DPlib;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;

/**
 * Created by george on 05/08/16.
 */
class SwingDisplay
{

    void createAndShowGUI()
    {
        System.out.println("Swing app EDT: " + SwingUtilities.isEventDispatchThread());
        GridBagConstraints gbc = new GridBagConstraints();

//        editorTextArea
        JTextArea editor = new JTextArea();
        editor.setLineWrap(true);
        editor.setText("Your code here");
        editor.setBackground(Color.LIGHT_GRAY);
        JScrollPane editorScrollPane = new JScrollPane(editor);
        editorScrollPane.setPreferredSize(new Dimension(1, 1));
        editorScrollPane.setBackground(Color.ORANGE);

//        terminalTextArea
        JTextArea terminal = new JTextArea();
        terminal.setLineWrap(true);
        terminal.setText("Terminal");
        terminal.setBackground(Color.LIGHT_GRAY);
        JScrollPane terminalScrollPane = new JScrollPane(terminal);
        terminalScrollPane.setPreferredSize(new Dimension(1, 1));
        terminalScrollPane.setBackground(Color.GREEN);

//        ioPanel
        JPanel ioPanel = new JPanel();
        ioPanel.setBackground(Color.BLUE);
        ioPanel.setLayout(new GridBagLayout());

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

//        button
        JButton solveBtn = new JButton();
        solveBtn.setText("Solve");

        JTextArea executionTimeGraph = new JTextArea();
        executionTimeGraph.setText("Execution time");
        executionTimeGraph.setBorder(new EtchedBorder());

        JTextArea instructionCountGraph = new JTextArea();
        instructionCountGraph.setText("Instruction count");
        instructionCountGraph.setBorder(new EtchedBorder());

//        executionTimeGraph
        JFreeChart executionTimeChart = ChartFactory.createBarChart(
                "Test Chart",
                "Category",
                "Score",
                createDataset(),
                PlotOrientation.VERTICAL,
                true, true, false);
        ChartPanel executionTimeChartPanel = new ChartPanel(executionTimeChart);
        executionTimeChartPanel.setPreferredSize(new Dimension(200, 300));

//        instructionCountGraph
        JFreeChart instructionCountChart = ChartFactory.createBarChart(
                "Test Chart",
                "Category",
                "Score",
                createDataset(),
                PlotOrientation.VERTICAL,
                true, true, false);
        ChartPanel instructionCountChartPanel = new ChartPanel(instructionCountChart);
        instructionCountChartPanel.setPreferredSize(new Dimension(200, 300));

//        graphPanel
        JPanel graphPanel = new JPanel();
        BoxLayout graphPanelLayout = new BoxLayout(graphPanel, BoxLayout.Y_AXIS);
        graphPanel.setLayout(graphPanelLayout);
        graphPanel.add(solveBtn);
        graphPanel.add(executionTimeChartPanel);
        graphPanel.add(instructionCountChartPanel);

//        mainPanel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(Color.GRAY);

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

        JFrame frame = new JFrame("Test gui");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(800, 600));
        frame.setContentPane(mainPanel);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private CategoryDataset createDataset( )
    {
        final String fiat = "FIAT";
        final String audi = "AUDI";
        final String ford = "FORD";
        final String speed = "Speed";
        final String millage = "Millage";
        final String userrating = "User Rating";
        final String safety = "safety";
        final DefaultCategoryDataset dataset =
                new DefaultCategoryDataset( );

        dataset.addValue( 1.0 , fiat , speed );
        dataset.addValue( 3.0 , fiat , userrating );
        dataset.addValue( 5.0 , fiat , millage );
        dataset.addValue( 5.0 , fiat , safety );

        dataset.addValue( 5.0 , audi , speed );
        dataset.addValue( 6.0 , audi , userrating );
        dataset.addValue( 10.0 , audi , millage );
        dataset.addValue( 4.0 , audi , safety );

        dataset.addValue( 4.0 , ford , speed );
        dataset.addValue( 2.0 , ford , userrating );
        dataset.addValue( 3.0 , ford , millage );
        dataset.addValue( 6.0 , ford , safety );

        return dataset;
    }

}
