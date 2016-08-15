package org.DPlib;

import java.io.IOException;
import java.io.OutputStream;

import javax.swing.*;

// sourced from http://www.codejava.net/java-se/swing/redirect-standard-output-streams-to-jtextarea

public class CustomOutputStream extends OutputStream {

    private JTextArea textArea;

    public CustomOutputStream(JTextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void write(int b) throws IOException {
        textArea.append(String.valueOf((char)b));
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }

}