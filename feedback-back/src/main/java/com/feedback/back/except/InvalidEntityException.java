package com.feedback.back.except;

/**
 * Created by Samarth Bhargav on 12/7/15.
 */
public class InvalidEntityException extends Exception
{
    public InvalidEntityException()
    {
        super();
    }


    public InvalidEntityException( String message )
    {
        super( message );
    }


    public InvalidEntityException( String message, Throwable cause )
    {
        super( message, cause );
    }


    public InvalidEntityException( Throwable cause )
    {
        super( cause );
    }


    protected InvalidEntityException( String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace )
    {
        super( message, cause, enableSuppression, writableStackTrace );
    }
}
