package org.ggraver.DPlib;

import com.thoughtworks.xstream.XStream;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

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

        dir = this.file.getParentFile();
        checkDir(dir);
    }

    private void checkValidFile(File file, String extension)
    throws IOException
    {
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
    }

    private void checkDir(File dir)
    throws IOException
    {
        if(!dir.exists() || !dir.isDirectory())
        {
            throw new IOException("Invalid directory: " + dir.getPath());
        }
    }

    public void serializeModel(Model model)
    {
        try
        {
            File modelFile = new File(dir, (FilenameUtils.removeExtension(file.getName()) + ".mod"));
            System.out.println("File path: " + file.getAbsolutePath());
            FileOutputStream fos = new FileOutputStream(modelFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(model);
            oos.close();
            fos.close();
        }catch(IOException i)
        {
            i.printStackTrace();
        }
    }

    public String[] parseInputTextFile(File f)
    throws IOException
    {
        FileReader fr = new FileReader(f);
        BufferedReader br = new BufferedReader(fr);
        List<String> lines = new ArrayList<>();
        String line;

        while((line = br.readLine()) != null)
        {
            lines.add(line);
        }
        return lines.toArray(new String[lines.size()]);
    }

    public File getFile()
    {
        return file;
    }

    public File createJavaFile(String editorText)
    throws IOException
    {
        File javaFile = new File(dir, "temp.java");
        javaFile.deleteOnExit();
        FileWriter fileWriter = new FileWriter(javaFile);
        fileWriter.write(editorText);
        fileWriter.close();
        return javaFile;
    }

    public String getFileAsString()
    throws IOException
    {
        FileInputStream fis = new FileInputStream(file);
        String fileAsString =  IOUtils.toString(fis, "UTF-8");
        fis.close();
        return fileAsString;
    }
}
