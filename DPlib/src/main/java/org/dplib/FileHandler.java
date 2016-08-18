package org.dplib;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.dplib.analyse.Model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by george on 01/08/16.
 */
public class FileHandler
{

    File locateFile(String filePath, String extension)
    throws IOException
    {
        File file = new File(filePath);
        if (!file.exists())
        {
            throw new IOException("Could not find file: " + file.getPath());
        }
        if(file.isDirectory())
        {
            throw new IOException("Specified file \"" + file.getPath()  +
            "\" is a directory. Please specify a valid file.");
        }
        if(!FilenameUtils.getExtension(file.getPath()).equals(extension))
        {
            throw new IOException("Input file \"" + file.getPath() +
            "\" does not match required file extension \"" + extension + "\".");
        }
        return file;
    }

    public File createTempJavaFile(String className, String editorText)
    throws IOException
    {
        File javaFile = new File("temp.java");
        javaFile.deleteOnExit();
        FileWriter fileWriter;

        try
        {
            fileWriter = new FileWriter(javaFile);
            fileWriter.write(editorText);
            fileWriter.close();
        }
        catch (IOException e)
        {
            throw new IOException("Could not create temp java file.");
        }

        return javaFile;
    }

    public String[][] parseInputTextFile(File f)
    throws IOException
    {
        FileReader fr = new FileReader(f);
        BufferedReader br = new BufferedReader(fr);
        List<String[]> lines = new ArrayList<>();
        String line;

        while((line = br.readLine()) != null)
        {
            String[] inputs = line.split(" ");
            lines.add(inputs);
        }
        return lines.toArray(new String[lines.size()][]);
    }

    public String sourceFileToString(File source)
    throws IOException
    {
        return FileUtils.readFileToString(source, "UTF-8");
    }

    public int serializeModel(Model model, File dir)
    {
        File modelFile = new File(dir, (model.getClassName() + ".mod"));
        ObjectOutputStream oos;
        try
        {
            FileOutputStream fos = new FileOutputStream(modelFile);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(model);
            oos.close();
            fos.close();
        }
        catch (IOException e)
        {
            throw new Error(e);
        }
        return 0;
    }

    public Model deserializeModelFile(File modelFile)
    {
        Model model;
        try
        {
            FileInputStream fis = new FileInputStream(modelFile);
            ObjectInputStream in = new ObjectInputStream(fis);
            model = (Model)in.readObject();
            in.close();
            fis.close();
        }
        catch (IOException | ClassNotFoundException e)
        {
            throw new Error(e);
        }
        return model;
    }

}
