package org.dplib.display;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Created by george on 19/08/16.
 */
public class CustomScrollPane
extends JScrollPane
{

    private final JTextArea textArea;

    CustomScrollPane(JTextArea textArea,
                     Font font,
                     Color bg,
                     boolean wrap,
                     boolean editable,
                     String title)
    {
        super(textArea);
        this.textArea = textArea;
        textArea.setFont(font);
        textArea.setBackground(bg);
        textArea.setLineWrap(wrap);
        textArea.setTabSize(2);
        textArea.setEditable(editable);

        setPreferredSize(new Dimension(1, 1));
        setBorder(new TitledBorder(title));
    }

    CustomScrollPane(JTextArea textArea,
                     Font font,
                     Color bg,
                     boolean wrap,
                     boolean editable,
                     String title,
                     String text)
    {
        this(textArea, font, bg, wrap, editable, title);
        textArea.setText(text);
    }

    JTextArea getJTextArea() { return textArea; }
    String getText() { return textArea.getText(); }
}
