package com.feedback.back.entities;

import java.util.List;


/**
 * Created by hduser on 30/6/15.
 */
public class DatasetStatistics
{
    private List<DatasetStats> datasetStatistics;


    public List<DatasetStats> getDatasetStatistics()
    {
        return datasetStatistics;
    }


    public void setDatasetStatistics( List<DatasetStats> datasetStatistics )
    {
        this.datasetStatistics = datasetStatistics;
    }
}
