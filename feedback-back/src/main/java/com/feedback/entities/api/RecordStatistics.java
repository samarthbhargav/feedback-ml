package com.feedback.entities.api;

import com.feedback.entities.RecordStats;

import java.util.List;


/**
 * Created by Samarth Bhargav on 7/7/15.
 */
public class RecordStatistics
{
    private List<RecordStats> stats;


    public List<RecordStats> getStats()
    {
        return stats;
    }


    public void setStats( List<RecordStats> stats )
    {
        this.stats = stats;
    }


    @Override
    public String toString()
    {
        return "RecordStatistics{" +
            "stats=" + stats +
            '}';
    }
}
