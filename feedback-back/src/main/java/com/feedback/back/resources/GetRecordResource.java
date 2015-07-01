package com.feedback.back.resources;

import com.feedback.back.dao.RecordDAO;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


/**
 * Created by hduser on 30/6/15.
 */
@Path ("") public class GetRecordResource
{
    final private RecordDAO recordDAO = new RecordDAO();


    @GET @Produces (MediaType.APPLICATION_JSON) @Path ("/record/{dataset}/{id}") public Response getRecord(
        @PathParam ("dataset") String dataset, @PathParam ("id") String id )
    {
        return Response.ok( recordDAO.getRecord( dataset, id ) ).build();
    }


    @GET @Produces (MediaType.APPLICATION_JSON) @Path ("/page/{dataset}/{skip}/{limit}") public Response getRecordPage(
        @PathParam ("dataset") String dataset, @PathParam ("id") String id, @PathParam ("skip") Integer skip,
        @PathParam ("limit") Integer limit )
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
