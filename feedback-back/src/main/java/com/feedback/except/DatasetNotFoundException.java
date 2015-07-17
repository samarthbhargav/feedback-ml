package com.feedback.except;

/**
 * Created by Samarth Bhargav on 8/7/15.
 */
public class DatasetNotFoundException extends Exception
{
    public DatasetNotFoundException()
    {
        super();
    }


    public DatasetNotFoundException( String message )
    {
        super( message );
    }


    public DatasetNotFoundException( String message, Throwable cause )
    {
        super( message, cause );
    }


    public DatasetNotFoundException( Throwable cause )
    {
        super( cause );
    }


    protected DatasetNotFoundException( String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace )
    {
        super( message, cause, enableSuppression, writableStackTrace );
    }
}
