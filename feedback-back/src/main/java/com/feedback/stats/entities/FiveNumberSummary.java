package com.feedback.stats.entities;

/**
 * Created by Samarth Bhargav on 16/7/15.
 */
public class FiveNumberSummary
{
    private Double max;
    private Double min;
    private Double mean;
    private Double median;
    private Double firstQuartile;
    private Double thirdQuartile;


    public Double getMax()
    {
        return max;
    }


    public void setMax( Double max )
    {
        this.max = max;
    }


    public Double getMin()
    {
        return min;
    }


    public void setMin( Double min )
    {
        this.min = min;
    }


    public Double getMean()
    {
        return mean;
    }


    public void setMean( Double mean )
    {
        this.mean = mean;
    }


    public Double getMedian()
    {
        return median;
    }


    public void setMedian( Double median )
    {
        this.median = median;
    }


    public Double getFirstQuartile()
    {
        return firstQuartile;
    }


    public void setFirstQuartile( Double firstQuartile )
    {
        this.firstQuartile = firstQuartile;
    }


    public Double getThirdQuartile()
    {
        return thirdQuartile;
    }


    public void setThirdQuartile( Double thirdQuartile )
    {
        this.thirdQuartile = thirdQuartile;
    }


    @Override
    public String toString()
    {
        return "FiveNumberSummary{" +
            "max=" + max +
            ", min=" + min +
            ", mean=" + mean +
            ", median=" + median +
            ", firstQuartile=" + firstQuartile +
            ", thirdQuartile=" + thirdQuartile +
            '}';
    }
}
