package com.feedback.back.filters;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.util.Arrays;


/**
 * Created by hduser on 3/7/15.
 */
public class ResponseCorsFilter implements ContainerResponseFilter
{

    @Override
    public void filter( ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext )
        throws IOException
    {
        MultivaluedMap<String, String> map = containerResponseContext.getStringHeaders();
        map.put( "Access-Control-Allow-Origin", Arrays.asList( "*" ) );
        map.put( "Access-Control-Allow-Methods", Arrays.asList( "GET, POST, OPTIONS" ) );
    }
}