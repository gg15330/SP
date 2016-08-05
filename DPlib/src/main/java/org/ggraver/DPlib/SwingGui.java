package org.ggraver.DPlib;

import javax.swing.*;

/**
 * Created by george on 05/08/16.
 */
public class SwingGui
extends JFrame
{
    private JPanel mainPanel;
    private JPanel editorPanel;
    private JPanel terminalPanel;
    private JPanel graphPanel;
    private JButton solveButton;
    private JTextArea editorTextArea;
    private JTextArea terminalOutputTextArea;

    SwingGui()
    {
        super("Swing GUI");
    }

    JPanel getMainPanel()
    {
        return mainPanel;
    }
}
