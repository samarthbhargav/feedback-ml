package com.feedback.resources;

import com.feedback.dao.MetaDataDAO;
import com.feedback.dao.RecordDAO;
import com.feedback.entities.Dataset;
import com.feedback.entities.Record;
import com.feedback.except.DatasetNotFoundException;
import com.feedback.except.InvalidEntityException;
import com.mongodb.client.result.DeleteResult;

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


    // ------------- Endpoints for CRUD operations on Datasets --------------------


    /**
     * Get all Datasets
     * @return
     */
    @GET
    @Produces (MediaType.APPLICATION_JSON)
    public Response getAllDatasets()
    {

        DatasetList datasetList = new DatasetList();
        try {
            datasetList.setDatasets( metaDataDAO.getDatasets() );
        } catch ( InvalidEntityException e ) {
            return ResourceUtil.buildErrorEntity( e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR );
        }
        return Response.ok( datasetList ).build();
    }


    /**
     * Saves a dataset. This operation is idempotent.
     * @param dataset
     * @return
     */
    @POST
    @Consumes (MediaType.APPLICATION_JSON)
    @Produces (MediaType.APPLICATION_JSON)
    public Response saveDataset( Dataset dataset )
    {
        try {
            this.metaDataDAO.save( dataset );
        } catch ( InvalidEntityException e ) {
            return ResourceUtil.buildErrorEntity( e.getMessage(), Response.Status.BAD_REQUEST );
        }
        return Response.ok().build();
    }


    /**
     * Returns a particular dataset and all records associated with it
     * @param name
     * @return
     */
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
        } catch ( InvalidEntityException e ) {
            return ResourceUtil.buildErrorEntity( e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR );
        }
    }


    /**
     * Deletes a Dataset
     * @param name the name of the dataset to be deleted
     * @return
     */
    @DELETE
    @Path ("/{dataset}")
    @Produces (MediaType.APPLICATION_JSON)
    public Response removeDataset(
        @PathParam ("dataset")
        String name )
    {
        try {
            this.metaDataDAO.removeDataset( name );
            return Response.ok().build();
        } catch ( DatasetNotFoundException e ) {
            return ResourceUtil.buildErrorEntity( e.getMessage(), Response.Status.NOT_FOUND );
        } catch ( InvalidEntityException e ) {
            return ResourceUtil.buildErrorEntity( e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR );
        }
    }


    // ------------- Endpoints for CRUD operations on Records --------------------


    /**
     * Returns a 'page' of records
     * @param dataset the dataset name
     * @param skip number of records to skip
     * @return
     */
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


    /**
     * Returns a 'page' of records
     * @param dataset
     * @param skip number of records to skip
     * @param limit number of records to return at once
     * @return
     */

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
        } catch ( InvalidEntityException e ) {
            // TODO log this
            return ResourceUtil.buildErrorEntity( e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR );
        }
    }


    /**
     * Returns a particular record
     * @param dataset the name of the dataset
     * @param id the id of the record
     * @return
     */
    @GET
    @Path ("/{dataset}/record/{id}")
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
        } catch ( InvalidEntityException e ) {
            // TODO log this
            return ResourceUtil.buildErrorEntity( e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR );
        }
    }


    /**
     * Saves a record. This operation is idempotent
     * @param dataset the name of the dataset
     * @param record the record to save
     * @return
     */
    @POST
    @Path ("/{dataset}/record/")
    @Consumes (MediaType.APPLICATION_JSON)
    @Produces (MediaType.APPLICATION_JSON)
    public Response saveRecord(
        @PathParam ("dataset")
        String dataset, Record record )
    {
        try {
            this.recordDAO.save( dataset, record );
            return Response.ok().build();
        } catch ( DatasetNotFoundException e ) {
            return ResourceUtil.buildErrorEntity( e.getMessage(), Response.Status.NOT_FOUND );
        } catch ( InvalidEntityException e ) {
            return ResourceUtil.buildErrorEntity( e.getMessage(), Response.Status.BAD_REQUEST );
        }
    }


    /**
     * Deletes a particular record
     * @param dataset
     * @param id
     * @return
     */
    @DELETE
    @Path ("/{dataset}/record/{id}")
    @Produces (MediaType.APPLICATION_JSON)
    public Response removeRecord(
        @PathParam ("dataset")
        String dataset,
        @PathParam ("id")
        String id )
    {
        try {
            DeleteResult result = recordDAO.remove( dataset, id );
            return Response.ok( result ).build();
        } catch ( DatasetNotFoundException e ) {
            return ResourceUtil.buildErrorEntity( e.getMessage(), Response.Status.NOT_FOUND );
        } catch ( InvalidEntityException e ) {
            return ResourceUtil.buildErrorEntity( e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR );
        }
    }
}
