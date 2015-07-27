package com.feedback.dao;

import com.feedback.config.Constants;
import com.feedback.entities.Dataset;
import com.feedback.entities.Record;
import com.feedback.entities.RecordStats;
import com.feedback.entities.api.RecordStatistics;
import com.feedback.entities.api.RecordsPage;
import com.feedback.except.DatasetNotFoundException;
import com.feedback.except.InvalidEntityException;
import com.feedback.mongo.MongoConnector;
import com.mongodb.Block;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOneModel;
import com.mongodb.client.model.WriteModel;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


/**
 * Created by Samarth Bhargav on 30/6/15.
 *
 * TODO Add documentation
 * TODO Add INFO Logging
 * TODO Add DEBUG Logging
 */
public class RecordDAO
{
    private static final Logger LOG = LoggerFactory.getLogger( RecordDAO.class );
    private static final RecordDAO INSTANCE = new RecordDAO();


    public static RecordDAO getInstance()
    {
        return INSTANCE;
    }


    private final Map<String, MongoCollection<Document>> collections = new HashMap<>();
    private final MetaDataDAO metaDataDAO = MetaDataDAO.getInstance();


    private RecordDAO()
    {

    }


    protected void reloadDatasets() throws InvalidEntityException
    {
        List<Dataset> datasets = this.metaDataDAO.getDatasets();
        this.collections.clear();
        LOG.info( "Found data sets: {}", datasets );
        for ( Dataset dataset : datasets ) {
            collections.put( dataset.getName(),
                MongoConnector.getDB( Constants.MONGO.RECORDS_DB ).getCollection( dataset.getName() ) );
        }
        LOG.info( "Loaded {} data sets", this.collections.size() );
    }


    private MongoCollection<Document> getCollection( String dataset ) throws DatasetNotFoundException, InvalidEntityException
    {
        if ( !this.collections.containsKey( dataset ) ) {
            reloadDatasets();
            // Even after reloading if a dataset isn't present - throw an exception
            if ( !this.collections.containsKey( dataset ) ) {
                throw new DatasetNotFoundException( "dataset " + dataset + " not found!" );
            }
        }
        return this.collections.get( dataset );
    }


    public void save( String dataset, Record record ) throws DatasetNotFoundException, InvalidEntityException
    {

        this.metaDataDAO.getDataset( dataset ).validateRecordForDataset( record );
        record.setLastModified( System.currentTimeMillis() );
        this.getCollection( dataset )
            .replaceOne( new Document( "_id", record.getId() ), record.toDocument(), DAOUtil.UPSERT_TRUE );
    }


    public void saveBulk( String dataset, List<Record> records ) throws DatasetNotFoundException, InvalidEntityException
    {
        if ( records.isEmpty() ) {
            return;
        }
        Dataset dset = this.metaDataDAO.getDataset( dataset );
        for ( Record record : records ) {
            dset.validateRecordForDataset( record );

        }
        List<WriteModel<Document>> replaceOneModels = new ArrayList<>( records.size() );
        long modificationTime = System.currentTimeMillis();
        for ( Record record : records ) {
            record.setLastModified( modificationTime );
            replaceOneModels
                .add( new ReplaceOneModel( new Document( "_id", record.getId() ), record.toDocument(), DAOUtil.UPSERT_TRUE ) );
        }

        this.getCollection( dataset ).bulkWrite( replaceOneModels );
    }


    public void labelRecord( String dataset, String id, String newLabel )
        throws DatasetNotFoundException, InvalidEntityException
    {
        this.getCollection( dataset ).updateOne( new Document( "_id", id ),
            new Document( "$set", new Document( "label", newLabel ).append( "lastModified", System.currentTimeMillis() ) ) );
    }


    public DeleteResult remove( String dataset, String id ) throws DatasetNotFoundException, InvalidEntityException
    {
        return this.getCollection( dataset ).deleteOne( new Document( "_id", id ) );
    }


    public DeleteResult removeBulk( String dataset, List<String> ids ) throws DatasetNotFoundException, InvalidEntityException
    {
        return this.getCollection( dataset ).deleteMany( new Document( "_id", new Document( "$in", ids ) ) );
    }


    public DeleteResult removeAll( String dataset ) throws DatasetNotFoundException, InvalidEntityException
    {
        return this.getCollection( dataset ).deleteMany( new Document() );
    }


    public Record getRecord( String dataset, String id ) throws DatasetNotFoundException, InvalidEntityException
    {
        return Record.fromDocument( this.getCollection( dataset ).find( Filters.and( Filters.eq( "_id", id ) ) ).first() );
    }


    public RecordsPage getRecordsPage( String dataset, int skip, int limit )
        throws DatasetNotFoundException, InvalidEntityException
    {
        if ( skip < 0 || limit < 0 ) {
            throw new IllegalArgumentException( "invalid skip or limit" );
        }
        List<Document> documents = new ArrayList<>();
        this.getCollection( dataset ).find().skip( skip ).limit( limit ).into( documents );
        List<Record> records = new ArrayList<>( documents.size() );
        for ( Document document : documents ) {
            records.add( Record.fromDocument( document ) );
        }
        RecordsPage recordsPage = new RecordsPage();
        recordsPage.setLimit( limit );
        recordsPage.setSkip( skip );
        recordsPage.setRecords( records );
        recordsPage.setTotalNumberOfRecords( this.getCollection( dataset ).count() );
        return recordsPage;
    }


    public RecordStatistics getRecordStatistics( String dataset ) throws DatasetNotFoundException, InvalidEntityException
    {
        Document group = new Document( "$group",
            new Document( "_id", "$label" ).append( "count", new Document( "$sum", 1L ) ) );
        final List<RecordStats> list = new ArrayList<>();
        this.getCollection( dataset ).aggregate( Arrays.asList( group ) ).allowDiskUse( true ).useCursor( true )
            .forEach( new Block<Document>()
            {
                @Override
                public void apply( Document document )
                {
                    RecordStats recordStats = new RecordStats();
                    recordStats.setNumberOfRecords( document.getLong( "count" ) );
                    recordStats.setLabel( document.getString( "_id" ) );
                    list.add( recordStats );
                }
            } );
        RecordStatistics recordStatistics = new RecordStatistics();
        recordStatistics.setStats( list );
        return recordStatistics;
    }
}
