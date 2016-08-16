package org.dplib;

import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by george on 01/08/16.
 */
public class FileHandler
{

    private File file;
    private File dir;

    public FileHandler(String file, String ext)
    throws IOException
    {
        this.file = new File(file);
        checkValidFile(this.file, ext);

        this.dir = this.file.getParentFile();
    }

    private void checkValidFile(File file, String extension)
    throws IOException
    {
        if (!file.exists())
        {
            throw new IOException("Could not find file: " + file.getPath() + "." + extension);
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
    }

    public File createTempJavaFile(String editorText)
    throws IOException
    {
        File javaFile = new File(dir, "temp.java");
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

    public int serializeModel(Model model)
    {
        File modelFile = new File(dir, (FilenameUtils.removeExtension(file.getName()) + ".mod"));
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

    public Model deserializeModelFile()
    {
        Model model;
        try
        {
            FileInputStream fis = new FileInputStream(file);
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

    public File getFile()
    {
        return file;
    }

}