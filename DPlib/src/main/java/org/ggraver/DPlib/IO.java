package org.ggraver.DPlib;

import com.thoughtworks.xstream.XStream;
import org.apache.commons.io.FilenameUtils;
import org.ggraver.DPlib.Exception.AnalysisException;
import org.ggraver.DPlib.Exception.ModelingException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

// IO handling
class IO
{

    private File dir;

    IO()
    {

    }

    void checkArgs(String[] args)
    throws IOException
    {
        System.out.println("Num of args: " + args.length);
        if (args.length != 2)
        {
            throw new IOException(usage);
        }
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

    private void checkDir(File)
    throws IOException
    {
        if()
    }

    private void generateXML(Model m)
    {
        File XML = new File(sourceFile.getParentFile(), "model.xml");
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

    void fail(Exception e)
    {
        if (!(e instanceof AnalysisException) && !(e instanceof ModelingException))
        {
            e.printStackTrace();
            throw new Error("Expected either ModelingException or AnalysisException.");
        }
        e.printStackTrace();
        System.err.println(e.getMessage());
        System.exit(1);
    }

    private String usage = "\nUsage: java -jar DPLib-1.0-SNAPSHOT.jar " +
            "<path/to/file_to_model.java> <method_to_analyse>\n";

    private String analysisReport;

    public File getDir()
    {
        return dir;
    }

}
