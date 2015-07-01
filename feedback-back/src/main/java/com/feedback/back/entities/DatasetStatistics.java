package com.feedback.back.entities;

import java.util.List;


/**
 * Created by hduser on 30/6/15.
 */
public class DatasetStatistics
{
    private List<DatasetStatistic> datasetStatistics;


    public List<DatasetStatistic> getDatasetStatistics()
    {
        return datasetStatistics;
    }


    public void setDatasetStatistics( List<DatasetStatistic> datasetStatistics )
    {
        this.datasetStatistics = datasetStatistics;
    }
}
