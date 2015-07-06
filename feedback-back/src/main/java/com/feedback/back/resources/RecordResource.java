package com.feedback.back.resources;

import com.feedback.back.dao.RecordDAO;
import com.feedback.back.entities.Record;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


/**
 * Created by hduser on 30/6/15.
 */
@Path ("")
@Api (value = "/", description = "Web Services to fetch records")
public class RecordResource
{
    final private RecordDAO recordDAO = RecordDAO.getInstance();


    @GET
    @Produces (MediaType.APPLICATION_JSON)
    @Path ("/record/{dataset}/{id}")
    @ApiOperation (value = "Get Records", notes = "Returns a record given the dataset and the record id", response = Record.class)
    public Response getRecord(
        @PathParam ("dataset")
        String dataset,
        @PathParam ("id")
        String id )
    {
        return Response.ok( recordDAO.getRecord( dataset, id ) ).build();
    }


    @POST
    @Consumes (MediaType.APPLICATION_JSON)
    @Path ("/record/{dataset}")
    public Response saveRecord(
        @PathParam ("dataset")
        String dataset, Record record )
    {
        this.recordDAO.save( record, dataset );
        return Response.ok().build();
    }


    @GET
    @Produces (MediaType.APPLICATION_JSON)
    @Path ("/page/{dataset}/{skip}/{limit}")
    public Response getRecordPage(
        @PathParam ("dataset")
        String dataset,
        @PathParam ("id")
        String id,
        @PathParam ("skip")
        Integer skip,
        @PathParam ("limit")
        Integer limit )
    {
        if ( skip == null ) {
            skip = 0;
        }
        if ( limit == null ) {
            limit = 100;
        }
        return Response.ok( recordDAO.getRecordsPage( dataset, skip, limit ) ).build();
    }
}
