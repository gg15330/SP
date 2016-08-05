package org.ggraver.DPlib;

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
        System.out.println("Swing app EDT: " + SwingUtilities.isEventDispatchThread());

//        editorPanel components
        JTextArea editorTextArea = new JTextArea();
        editorTextArea.setText("Your code here");

//      add editor text area to editor scroll pane
        JScrollPane editorScrollPane = new JScrollPane(editorTextArea);
        editorScrollPane.setPreferredSize(new Dimension(300, 300));

//        add components to editorPanel
        JPanel editorPanel = new JPanel();
        editorPanel.setLayout(new BorderLayout());
        editorPanel.setBorder(new EtchedBorder());
        editorPanel.add(editorScrollPane, BorderLayout.CENTER);

//        terminalPanel components
        JTextArea terminalTextArea = new JTextArea();
        terminalTextArea.setText("Terminal output");

//      add terminalTextArea to terminalScrollPane
        JScrollPane terminalScrollPane = new JScrollPane(terminalTextArea);

//        add components to terminalPanel
        JPanel terminalPanel = new JPanel();
        terminalPanel.setLayout(new BorderLayout());
        terminalPanel.setBorder(new EtchedBorder());
        terminalPanel.add(terminalScrollPane, BorderLayout.CENTER);

//        btnPanel components
        JButton solveBtn = new JButton("Solve");

//        add components to btnPanel
        JPanel btnPanel = new JPanel();
        btnPanel.add(solveBtn);

//        graphPanel components
        JTextArea graph1 = new JTextArea();
        JTextArea graph2 = new JTextArea();
        graph1.setText("Graph 1");
        graph2.setText("Graph 2");

//        add components to graphPanel
        JPanel graphPanel = new JPanel();
        BoxLayout graphPanelLayout = new BoxLayout(graphPanel, BoxLayout.Y_AXIS);
        graphPanel.setLayout(graphPanelLayout);
        graphPanel.setBorder(new EtchedBorder());
        graphPanel.add(btnPanel);
        graphPanel.add(graph1);
        graphPanel.add(graph2);

//        add editorPanel and terminalPanel to ioPanel
        JPanel ioPanel = new JPanel();
        BoxLayout ioPanelLayout = new BoxLayout(ioPanel, BoxLayout.Y_AXIS);
        ioPanel.setLayout(ioPanelLayout);
        ioPanel.setBorder(new EtchedBorder());
        ioPanel.add(editorPanel);
        ioPanel.add(terminalPanel);

//        set layout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(new EtchedBorder());
        mainPanel.add(graphPanel, BorderLayout.EAST);
        mainPanel.add(ioPanel, BorderLayout.CENTER);

        JFrame frame = new JFrame("Test gui");
        frame.add(mainPanel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(800, 600));
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
