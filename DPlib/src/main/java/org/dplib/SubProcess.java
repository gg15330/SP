package org.dplib;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by george on 16/08/16.
 */
public interface SubProcess
{
    default Process subProcess(File dir, String... commands)
    throws IOException, InterruptedException
    {
        ProcessBuilder build = new ProcessBuilder(commands);
        build.directory(dir);
        Process p = build.start();
        return p;
    }

    default Process subProcess(File dir, List<String> commands)
    throws IOException, InterruptedException
    {
        ProcessBuilder build = new ProcessBuilder(commands);
        build.directory(dir);
        Process p = build.start();
        return p;
    }
}
