package org.dplib;

import org.apache.commons.io.FilenameUtils;
import org.dplib.exception.AnalysisException;
import org.dplib.exception.CompileException;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


// generates .class files and analyses performance
class ClassAnalyser
extends SubProcess
{

    // analyse the compiled .class file for performance
    List<Result> analyse(File classFile, String[][] inputs)
    throws AnalysisException
    {
        if (!classFile.exists() || classFile.length() == 0)
        {
            throw new AnalysisException(".class file does not exist or is empty.");
        }

        List<Result> results = new ArrayList<>();

        for(String[] input : inputs)
        {
            List<String> commands = new ArrayList<>();
            commands.add("java");
            commands.add(FilenameUtils.removeExtension(classFile.getName()));
            commands.addAll(Arrays.asList(input));

            String output;
            long start, end;
            Process p;

            try
            {
                start = System.currentTimeMillis();
                p = subProcess(classFile.getParentFile(), commands);
                redirectInputStream(p.getErrorStream());
                if(p.waitFor() != 0) throw new AnalysisException("Program did not execute correctly - check code (and inputs if modeling a problem).");
                end = System.currentTimeMillis();
                output = fetchOutput(p.getInputStream());
            }
            catch (IOException | InterruptedException e) { throw new AnalysisException(e); }

            if ((end - start) < 0) { throw new Error("Execution time should not be less than 0."); }

            Result result = new Result();
            result.setInput(input);
            result.setOutput(output);
            result.setExecutionTime(end - start);
            results.add(result);
        }

        return results;
    }

    private String fetchOutput(InputStream inputStream)
    throws IOException
    {
        InputStreamReader isr = new InputStreamReader(inputStream);
        BufferedReader br = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null) { sb.append(line); }
        if(sb.length() == 0) { throw new IOException("Output is empty."); }

        isr.close();
        br.close();

        return sb.toString();
    }

}
