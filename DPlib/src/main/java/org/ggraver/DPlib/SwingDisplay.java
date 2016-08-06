package org.ggraver.DPlib;

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

//        graphPanel
        JPanel graphPanel = new JPanel();
        BoxLayout graphPanelLayout = new BoxLayout(graphPanel, BoxLayout.Y_AXIS);
        graphPanel.setLayout(graphPanelLayout);
        graphPanel.add(solveBtn);
        graphPanel.add(executionTimeGraph);
        graphPanel.add(instructionCountGraph);

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
        frame.add(mainPanel);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
