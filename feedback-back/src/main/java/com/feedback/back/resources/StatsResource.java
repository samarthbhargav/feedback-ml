package com.feedback.back.resources;

import com.feedback.back.dao.RecordDAO;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


/**
 * Created by Samarth Bhargav on 30/6/15.
 */
@Path ("stats")
public class StatsResource
{
    RecordDAO recordDAO = RecordDAO.getInstance();


    @Path ("/datasets")
    @GET
    @Produces (MediaType.APPLICATION_JSON)
    public Response getDatasetStatistics()
    {
        return Response.ok( this.recordDAO.getDatasetStatistics() ).build();
    }


    @Path ("/datasets/{dataset}")
    @GET
    @Produces (MediaType.APPLICATION_JSON)
    public Response getRecordStatistics(
        @PathParam ("dataset")
        String dataset )
    {
        return Response.ok( this.recordDAO.getRecordStatistics( dataset ) ).build();
    }
}
