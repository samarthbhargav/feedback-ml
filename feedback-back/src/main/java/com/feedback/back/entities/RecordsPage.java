package com.feedback.back.entities;

import java.util.List;


/**
 * Created by hduser on 30/6/15.
 */
public class RecordsPage
{
    private int skip;
    private int limit;
    private List<Record> records;


    public int getSkip()
    {
        return skip;
    }


    public void setSkip( int skip )
    {
        this.skip = skip;
    }


    public int getLimit()
    {
        return limit;
    }


    public void setLimit( int limit )
    {
        this.limit = limit;
    }


    public List<Record> getRecords()
    {
        return records;
    }


    public void setRecords( List<Record> records )
    {
        this.records = records;
    }
}
