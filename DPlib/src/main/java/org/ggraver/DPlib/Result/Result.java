package org.ggraver.DPlib.Result;

/**
 * Created by george on 02/08/16.
 */
public abstract class Result
{

    ResultType resultType;
    protected String expected = "";
    protected String actual = "";

    public ResultType getResultType()
    {
        return resultType;
    }

    public String getExpected()
    {
        return expected;
    }

    public String getActual()
    {
        return actual;
    }

}
