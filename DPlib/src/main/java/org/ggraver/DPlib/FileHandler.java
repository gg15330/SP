package org.ggraver.DPlib;

import com.thoughtworks.xstream.XStream;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by george on 01/08/16.
 */
public class FileHandler
{

    private File sourceFile;
    private File dir;

    FileHandler(String file)
    throws IOException
    {
        sourceFile = new File(file);
        checkValidSourceFile(sourceFile, "java");

        dir = sourceFile.getParentFile();
        checkDir(dir);
    }

    private void checkValidSourceFile(File file, String extension)
    throws IOException
    {
        if (!file.exists()
                || file.isDirectory()
                || !FilenameUtils.getExtension(file.getPath()).equals(extension))
        {
            throw new IOException("Invalid input file: " + file.getPath());
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

    void generateXML(Model m)
    {
        File XML = new File(dir, "model.xml");
        XStream xstream = new XStream();
        FileOutputStream fos;
        try
        {
            fos = new FileOutputStream(XML);
            xstream.toXML(m, fos);
            fos.close();
        }
        catch (IOException e)
        {
            throw new Error("Could not serialize Model object to XML.");
        }
        if (!XML.exists())
        {
            throw new Error("XML file does not exist.");
        }
        System.out.println("Model file generated");
    }

    File getDir()
    {
        return dir;
    }

    File getSourceFile()
    {
        return sourceFile;
    }

}
