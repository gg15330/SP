package org.dplib.analyse;

import org.apache.commons.io.FilenameUtils;
import org.dplib.SubProcess;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


// generates List of Results for a given class file and inputs
public class ClassAnalyser
extends SubProcess
{

    public List<Result> analyse(File classFile, String[][] inputs)
    throws AnalysisException
    {
        if(classFile == null) throw new Error("Class file should not be null.");
        if(!classFile.exists()) { throw new AnalysisException(".class file does not exist."); }
        if(classFile.length() == 0) { throw new AnalysisException(".class file is empty."); }
        if(inputs == null) throw new Error("inputs should not be null.");
        if(inputs.length == 0) { throw new AnalysisException("Input array is empty."); }

        List<Result> results = new ArrayList<>();

        for(String[] input : inputs)
        {
            List<String> commands = buildCmdList(input, classFile.getName());
            results.add(computeResult(commands, input, classFile));
        }

        return results;
    }

    private Result computeResult(List<String> commands, String[] input, File classFile)
    throws AnalysisException
    {
        Process p;
        String output;
        long start, end;

        try
        {
            start = System.currentTimeMillis();
            p = subProcess(classFile.getParentFile(), commands);
            redirectInputStream(p.getErrorStream());
            if(p.waitFor() != 0) { throw new AnalysisException("Program did not execute correctly - check code (and inputs if modeling a problem)."); }
            end = System.currentTimeMillis();
        }
        catch (IOException | InterruptedException e)
        {
            throw new AnalysisException(e);
        }

        if ((end - start) < 0) { throw new Error("Execution time should not be less than 0."); }
        output = fetchOutput(p.getInputStream());

        return new Result(input, output, (end - start));
    }

    private List<String> buildCmdList(String[] input, String classFileName)
    {
        List<String> commands = new ArrayList<>();
        commands.add("java");
        commands.add(FilenameUtils.removeExtension(classFileName));
        commands.addAll(Arrays.asList(input));
        return commands;
    }

//    fetch the output of the program from the InputStream of the subprocess
    private String fetchOutput(InputStream inputStream)
    throws AnalysisException
    {
        InputStreamReader isr = new InputStreamReader(inputStream);
        BufferedReader br = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();
        String line;

        try
        {
            while ((line = br.readLine()) != null) { sb.append(line); }
            isr.close();
            br.close();
        }
        catch (IOException e) { throw new Error(e); }

        if(sb.length() == 0) { throw new AnalysisException("Output is empty."); }
        return sb.toString();
    }

}
