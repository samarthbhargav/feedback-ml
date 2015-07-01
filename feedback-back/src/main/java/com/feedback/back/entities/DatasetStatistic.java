package com.feedback.back.entities;

/**
 * Created by hduser on 30/6/15.
 */
public class DatasetStatistic
{

    private String dataset;
    private long numberOfRecords;


    public String getDataset()
    {
        return dataset;
    }


    public void setDataset( String dataset )
    {
        this.dataset = dataset;
    }


    public long getNumberOfRecords()
    {
        return numberOfRecords;
    }


    public void setNumberOfRecords( long numberOfRecords )
    {
        this.numberOfRecords = numberOfRecords;
    }


    @Override public String toString()
    {
        return "DatasetStatistic{" +
            "dataset='" + dataset + '\'' +
            ", numberOfRecords=" + numberOfRecords +
            '}';
    }
}
