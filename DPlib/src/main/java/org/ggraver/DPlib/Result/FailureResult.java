package org.ggraver.DPlib.Result;

/**
 * Created by george on 02/08/16.
 */
public class FailureResult
extends Result
{

    public FailureResult(ResultType failure, String expected, String actual)
    {
        if(resultType.equals(ResultType.PASS))
        {
            throw new Error("FailureResult object must not take ResultType.PASS.");
        }

        this.resultType = failure;
        this.expected = expected;
        this.actual = actual;
    }

}
