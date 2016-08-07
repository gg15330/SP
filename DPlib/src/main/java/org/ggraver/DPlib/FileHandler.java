package org.ggraver.DPlib;

import com.thoughtworks.xstream.XStream;
import org.apache.commons.io.FilenameUtils;

import java.io.*;

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
        System.out.println("File: " + file);
        this.file = new File(file);
        checkValidFile(this.file, ext);

        dir = this.file.getParentFile();
        checkDir(dir);
    }

    private void checkValidFile(File file, String extension)
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
    }

    public Model parseXML()
    throws IOException
    {
        XStream xStream = new XStream();
        File XML = new File(file.getParentFile() + "/model.xml");
        checkValidFile(XML, "xml");
        FileInputStream fis = new FileInputStream(XML);
        return (Model) xStream.fromXML(fis);
    }

    File getDir()
    {
        return dir;
    }

    public File getFile()
    {
        return file;
    }

}
