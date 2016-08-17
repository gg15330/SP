package org.dplib;

import java.io.*;
import java.util.List;

/**
 * Created by george on 16/08/16.
 */
public abstract class SubProcess
{
    Process subProcess(File dir, String... commands)
    throws IOException
    {
        ProcessBuilder build = new ProcessBuilder(commands);
        build.directory(dir);
        Process p = build.start();
        return p;
    }

    Process subProcess(File dir, List<String> commands)
    throws IOException
    {
        return subProcess(dir, commands.toArray(new String[commands.size()]));
    }

//    adapted from http://www.javaworld.com/article/2071275/core-java/when-runtime-exec---won-t.html?page=2
    void redirectInputStream(InputStream inputStream)
    {
        Runnable runnable = () ->
        {
            try
            {
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = br.readLine()) != null)
                {
                    System.out.println(line);
                }
                br.close();
            }
            catch (IOException e) { throw new Error(e); }
        };
        new Thread(runnable).start();
    }

}