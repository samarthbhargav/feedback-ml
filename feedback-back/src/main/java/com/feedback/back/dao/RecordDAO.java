package com.feedback.back.dao;

import com.feedback.back.entities.DatasetStatistics;
import com.feedback.back.entities.DatasetStats;
import com.feedback.back.entities.Record;
import com.feedback.back.entities.RecordsPage;
import com.feedback.back.mongo.MongoConnector;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Samarth Bhargav on 30/6/15.
 */
public class RecordDAO
{
    private static final Logger LOG = LoggerFactory.getLogger( RecordDAO.class );
    private static final RecordDAO INSTANCE = new RecordDAO();


    public static RecordDAO getInstance()
    {
        return INSTANCE;
    }


    private Map<String, MongoCollection<Document>> collections = new HashMap<>();
    private MetaDataDAO metaDataDAO = MetaDataDAO.getInstance();


    private MongoCollection<Document> getCollection( String dataset )
    {
        if ( !this.collections.containsKey( dataset ) ) {
            this.metaDataDAO.addDataset( dataset );
            collections.put( dataset, MongoConnector.getDB( "feedback-records" ).getCollection( dataset ) );
        }
        return this.collections.get( dataset );
    }


    private RecordDAO()
    {
        List<String> datasetNames = this.metaDataDAO.getDatasets();
        LOG.info( "Found data sets: {}", datasetNames );
        for ( String dataset : datasetNames ) {
            collections.put( dataset, MongoConnector.getDB( "feedback-records" ).getCollection( dataset ) );
        }
        LOG.info( "Loaded {} data sets", this.collections.size() );
    }


    public void insert( Record record, String dataset )
    {
        this.getCollection( dataset ).insertOne( record.toDocument() );
    }


    public void save( Record record, String dataset )
    {
        this.getCollection( dataset )
            .replaceOne( new Document( "_id", record.getId() ), record.toDocument(), DAOUtil.UPSERT_TRUE );
    }


    public Record getRecord( String dataset, String id )
    {
        return Record.fromDocument( this.getCollection( dataset ).find( Filters.and( Filters.eq( "_id", id ) ) ).first() );
    }


    public RecordsPage getRecordsPage( String dataset, int skip, int limit )
    {
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
        return recordsPage;
    }


    public DatasetStatistics getDatasetStatistics()
    {
        final List<DatasetStats> list = new ArrayList<>();

        for ( Map.Entry<String, MongoCollection<Document>> entry : this.collections.entrySet() ) {
            DatasetStats datasetStats = new DatasetStats();
            datasetStats.setDataset( entry.getKey() );
            datasetStats.setNumberOfRecords( entry.getValue().count() );
            list.add( datasetStats );
        }
        DatasetStatistics statistics = new DatasetStatistics();
        statistics.setDatasetStatistics( list );
        return statistics;
    }


    public static void main( String[] args )
    {
        RecordDAO recordDAO = new RecordDAO();
        Record record = new Record();
        record.setId( "someI2d" );
        record.setLabel( "label" );
        record.setContent( null );
        recordDAO.save( record, "dataset2" );
    }
}
