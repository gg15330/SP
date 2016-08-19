package org.dplib.display;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.PrintStream;

/**
 * Created by george on 19/08/16.
 */
public class Terminal
extends JScrollPane
{

    private final JTextArea terminal;

    public Terminal(JTextArea terminal)
    {
        super(terminal);
        this.terminal = terminal;
        Font font = new Font(null, Font.BOLD, 12);
        terminal.setFont(font);
        terminal.setForeground(Color.DARK_GRAY);
        terminal.setBackground(Color.LIGHT_GRAY);
        terminal.setLineWrap(true);
        terminal.setTabSize(2);
        terminal.setEditable(false);
        setPreferredSize(new Dimension(1, 1));
        setBorder(new TitledBorder("Console"));

        PrintStream terminalPrintStream = new PrintStream(new CustomOutputStream(terminal));
        System.setOut(terminalPrintStream);
        System.setErr(terminalPrintStream);
    }

    public String getText() { return terminal.getText(); }
    public void setText(String text) { terminal.setText(text); }
}
