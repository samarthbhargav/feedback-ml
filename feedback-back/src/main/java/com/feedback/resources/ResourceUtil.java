package com.feedback.resources;

import com.feedback.entities.api.ErrorEntity;

import javax.ws.rs.core.Response;


/**
 * Created by Samarth Bhargav on 8/7/15.
 */
public class ResourceUtil
{
    public static final Response buildErrorEntity( String message, Response.Status status )
    {
        return Response.status( status ).entity( new ErrorEntity( message, status.getStatusCode() ) ).build();
    }

}
