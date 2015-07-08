package com.feedback.back.resources;

import com.feedback.back.dao.MetaDataDAO;
import com.feedback.back.dao.RecordDAO;
import com.feedback.back.entities.Dataset;
import com.feedback.back.entities.Record;
import com.feedback.back.except.DatasetNotFoundException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


/**
 * Created by Samarth Bhargav on 8/7/15.
 */
@Path ("/dataset")
public class DatasetResource
{

    private final MetaDataDAO metaDataDAO = MetaDataDAO.getInstance();
    private final RecordDAO recordDAO = RecordDAO.getInstance();


    private static class DatasetList
    {
        private List<Dataset> datasets;


        public List<Dataset> getDatasets()
        {
            return datasets;
        }


        public void setDatasets( List<Dataset> datasets )
        {
            this.datasets = datasets;
        }
    }


    @GET
    @Produces (MediaType.APPLICATION_JSON)
    public Response getAllDatasets()
    {

        DatasetList datasetList = new DatasetList();
        datasetList.setDatasets( metaDataDAO.getDatasets() );
        return Response.ok( datasetList ).build();
    }


    @POST
    @Consumes (MediaType.APPLICATION_JSON)
    public Response saveDataset( Dataset dataset )
    {
        this.metaDataDAO.save( dataset );
        return Response.ok().build();
    }


    @GET
    @Path ("/{dataset}/")
    @Produces (MediaType.APPLICATION_JSON)
    public Response getDataset(
        @PathParam ("dataset")
        String name )
    {
        try {
            return Response.ok( this.metaDataDAO.getDataset( name ) ).build();
        } catch ( DatasetNotFoundException e ) {
            return ResourceUtil.buildErrorEntity( e.getMessage(), Response.Status.NOT_FOUND );
        }
    }


    @GET
    @Path ("/{dataset}/records/")
    @Produces (MediaType.APPLICATION_JSON)
    public Response getRecords(
        @PathParam ("dataset")
        String dataset,
        @PathParam ("skip")
        @DefaultValue ("null")
        String skip )
    {
        //TODO Externalize the defaults
        return this.getRecordsWithSkipAndLimit( dataset, "0", "100" );
    }


    @GET
    @Path ("/{dataset}/records/{skip}/{limit}")
    @Produces (MediaType.APPLICATION_JSON)
    public Response getRecordsWithSkipAndLimit(
        @PathParam ("dataset")
        String dataset,
        @PathParam ("skip")
        @DefaultValue ("null")
        String skip,
        @PathParam ("limit")
        @DefaultValue ("null")
        String limit )
    {
        int skipValue;
        if ( skip == null ) {
            skipValue = 0;
        } else {
            skipValue = Integer.parseInt( skip );
        }

        int limitValue;
        if ( limit == null ) {
            limitValue = 100;
        } else {
            limitValue = Integer.parseInt( limit );
        }
        try {
            return Response.ok( recordDAO.getRecordsPage( dataset, skipValue, limitValue ) ).build();
        } catch ( DatasetNotFoundException e ) {
            return ResourceUtil.buildErrorEntity( e.getMessage(), Response.Status.NOT_FOUND );
        }
    }


    @GET
    @Path ("/{dataset}/record//{id}")
    @Produces (MediaType.APPLICATION_JSON)
    public Response getRecord(
        @PathParam ("dataset")
        String dataset,
        @PathParam ("id")
        String id )
    {
        try {
            return Response.ok( recordDAO.getRecord( dataset, id ) ).build();
        } catch ( DatasetNotFoundException e ) {
            return ResourceUtil.buildErrorEntity( e.getMessage(), Response.Status.NOT_FOUND );
        }
    }


    @POST
    @Path ("/{dataset}/record/")
    @Consumes (MediaType.APPLICATION_JSON)
    @Produces (MediaType.APPLICATION_JSON)
    public Response saveRecord(
        @PathParam ("dataset")
        String dataset, Record record )
    {
        try {
            this.recordDAO.save( record, dataset );
            return Response.ok().build();
        } catch ( DatasetNotFoundException e ) {
            return ResourceUtil.buildErrorEntity( e.getMessage(), Response.Status.NOT_FOUND );
        }
    }

}
