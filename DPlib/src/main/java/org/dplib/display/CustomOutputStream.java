package org.dplib.display;

import javax.swing.*;
import java.io.IOException;
import java.io.OutputStream;

// sourced from http://www.codejava.net/java-se/swing/redirect-standard-output-streams-to-jtextarea

// writes the OutputStream to the supplied JTextArea
class CustomOutputStream extends OutputStream {

    private JTextArea textArea;

    CustomOutputStream(JTextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void write(int b) throws IOException {
        textArea.append(String.valueOf((char)b));
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }

}