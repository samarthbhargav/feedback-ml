package com.feedback.entities.api;

import com.feedback.entities.Record;

import java.util.List;


/**
 * Created by Samarth Bhargav on 30/6/15.
 */
public class RecordsPage
{
    private int skip;
    private int limit;
    private long totalNumberOfRecords;
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


    public long getTotalNumberOfRecords()
    {
        return totalNumberOfRecords;
    }


    public void setTotalNumberOfRecords( long totalNumberOfRecords )
    {
        this.totalNumberOfRecords = totalNumberOfRecords;
    }


    public List<Record> getRecords()
    {
        return records;
    }


    public void setRecords( List<Record> records )
    {
        this.records = records;
    }


    @Override
    public String toString()
    {
        return "RecordsPage{" +
            "skip=" + skip +
            ", limit=" + limit +
            ", totalNumberOfRecords=" + totalNumberOfRecords +
            ", records=" + records +
            '}';
    }
}
