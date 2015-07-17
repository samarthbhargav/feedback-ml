package com.feedback.entities;

/**
 * Created by Samarth Bhargav on 7/7/15.
 */
public class RecordStats
{
    private String label;
    private long numberOfRecords;


    public String getLabel()
    {
        return label;
    }


    public void setLabel( String label )
    {
        this.label = label;
    }


    public long getNumberOfRecords()
    {
        return numberOfRecords;
    }


    public void setNumberOfRecords( long numberOfRecords )
    {
        this.numberOfRecords = numberOfRecords;
    }


    @Override
    public String toString()
    {
        return "RecordStatistics{" +
            "label='" + label + '\'' +
            ", numberOfRecords=" + numberOfRecords +
            '}';
    }
}
