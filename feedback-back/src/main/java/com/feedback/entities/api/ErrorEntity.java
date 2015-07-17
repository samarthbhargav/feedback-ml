package com.feedback.entities.api;

/**
 * Created by Samarth Bhargav on 8/7/15.
 */
public class ErrorEntity
{
    private String message;
    private int statusCode;


    public ErrorEntity()
    {

    }


    public ErrorEntity( String message, int statusCode )
    {
        this.message = message;
        this.statusCode = statusCode;
    }


    public String getMessage()
    {
        return message;
    }


    public void setMessage( String message )
    {
        this.message = message;
    }


    public int getStatusCode()
    {
        return statusCode;
    }


    public void setStatusCode( int statusCode )
    {
        this.statusCode = statusCode;
    }


    @Override
    public String toString()
    {
        return "ErrorEntity{" +
            "message='" + message + '\'' +
            ", statusCode=" + statusCode +
            '}';
    }
}
