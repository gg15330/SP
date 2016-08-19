package org.dplib.display;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Created by george on 19/08/16.
 */
public class Editor
extends JScrollPane
{

    private final JTextArea editor;

    public Editor(JTextArea editor, String editorText)
    {
        super(editor);
        this.editor = editor;
        Font font = new Font(null, Font.BOLD, 12);
        editor.setFont(font);
        editor.setForeground(Color.DARK_GRAY);
        editor.setLineWrap(false);
        editor.setTabSize(2);
        editor.setEditable(true);
        editor.setText(editorText);
        setPreferredSize(new Dimension(1, 1));
        setBorder(new TitledBorder("Editor"));
    }

    public String getText() { return editor.getText(); }
    public void setText(String text) { editor.setText(text); }

}
