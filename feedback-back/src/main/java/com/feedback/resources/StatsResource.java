package com.feedback.resources;

import com.feedback.dao.MetaDataDAO;
import com.feedback.dao.RecordDAO;
import com.feedback.except.DatasetNotFoundException;
import com.feedback.except.InvalidEntityException;

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
    private final RecordDAO recordDAO = RecordDAO.getInstance();
    private final MetaDataDAO metaDataDAO = MetaDataDAO.getInstance();


    @Path ("/datasets")
    @GET
    @Produces (MediaType.APPLICATION_JSON)
    public Response getDatasetStatistics()
    {
        try {
            return Response.ok( this.metaDataDAO.getDatasetStatistics() ).build();
        } catch ( InvalidEntityException e ) {
            return ResourceUtil.buildErrorEntity( e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR );
        }
    }


    @Path ("/datasets/{dataset}")
    @GET
    @Produces (MediaType.APPLICATION_JSON)
    public Response getRecordStatistics(
        @PathParam ("dataset")
        String dataset )
    {
        try {
            return Response.ok( this.recordDAO.getRecordStatistics( dataset ) ).build();
        } catch ( DatasetNotFoundException e ) {
            return ResourceUtil.buildErrorEntity( e.getMessage(), Response.Status.NOT_FOUND );
        } catch ( InvalidEntityException e ) {
            return ResourceUtil.buildErrorEntity( e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR );
        }
    }
}
