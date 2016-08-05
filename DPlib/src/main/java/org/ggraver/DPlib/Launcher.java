package org.ggraver.DPlib;

import org.ggraver.DPlib.Exception.AnalysisException;
import org.ggraver.DPlib.Exception.ModelingException;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Properties;

import javax.swing.*;

import static javafx.application.Application.launch;

// overall program control
public class Launcher
{

//    public static void main(String args[])
//    {
//        launch(GUIApp.class, args);
//

//    }

    private void createAndShowGUI()
    {
        System.out.println("Swing app EDT: " + SwingUtilities.isEventDispatchThread());

        JButton btn2 = new JButton("button 2");
        JButton btn3 = new JButton("button 3");
        JButton btn4 = new JButton("button 4");
        JButton btn5 = new JButton("button 5");

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(btn2, BorderLayout.EAST);
        mainPanel.add(btn3, BorderLayout.WEST);
        mainPanel.add(btn4, BorderLayout.SOUTH);
        mainPanel.add(btn5, BorderLayout.CENTER);

        JFrame frame = new JFrame("Test gui");
        frame.add(mainPanel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        frame.setPreferredSize(new Dimension(800, 600));
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args)
    {
//        String jre = System.getProperty("java.runtime.name");
//        if (jre.equals("Java(TM) SE Runtime Environment"))
//        {
//            launch(GUIApp.class, args);
//        }
//        else if (jre.equals("OpenJDK Runtime Environment"))
//        {
//
//        }

        Launcher launcher = new Launcher();
        javax.swing.SwingUtilities.invokeLater(launcher::createAndShowGUI);
        try
        {
            new CommandLineApp().start(args);
            System.out.println("Command line app EDT: " + SwingUtilities.isEventDispatchThread());
        }
        catch (IOException | AnalysisException | ModelingException ex)
        {
            new IO().exit(ex);
            System.exit(1);
        }
    }

}
