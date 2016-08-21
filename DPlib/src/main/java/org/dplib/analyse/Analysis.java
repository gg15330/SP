package org.dplib.analyse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by george on 18/08/16.
 */

// stores ProblemType as determined in SourceAnalyser and List of Results from ClassAnalyser
public class Analysis
implements Serializable
{

    private ProblemType problemType;
    private List<Result> results;

    public ProblemType getProblemType() { return problemType; }
    public void setProblemType(ProblemType problemType) { this.problemType = problemType; }

    public List<Result> getResults() { return results; }
    public void setResults(List<Result> results) { this.results = results; }

}
