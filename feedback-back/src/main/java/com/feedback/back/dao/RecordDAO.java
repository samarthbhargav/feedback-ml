package com.feedback.back.dao;

import com.feedback.back.entities.DatasetStatistic;
import com.feedback.back.entities.DatasetStatistics;
import com.feedback.back.entities.Record;
import com.feedback.back.entities.RecordsPage;
import com.feedback.back.mongo.MongoConnector;
import com.mongodb.Block;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by Samarth Bhargav on 30/6/15.
 */
public class RecordDAO
{
    MongoCollection<Document> collection = MongoConnector.getDB( "feedback" ).getCollection( "Records" );


    public RecordDAO()
    {
    }


    public void insert( Record record, String dataset )
    {
        Document document = record.toDocument();
        document.put( "dataset", dataset );
        this.collection.insertOne( document );
    }


    public void save( Record record, String dataset )
    {
        Document document = record.toDocument();
        document.put( "dataset", dataset );
        this.collection.replaceOne( new Document( "_id", record.getId() ), document );
    }


    public Record getRecord( String dataset, String id )
    {
        return Record.fromDocument(
            collection.find( Filters.and( Filters.eq( "_id", id ), Filters.eq( "dataset", dataset ) ) ).first() );
    }


    public RecordsPage getRecordsPage( String dataset, int skip, int limit )
    {
        List<Document> documents = new ArrayList<>();
        collection.find( Filters.eq( "dataset", dataset ) ).skip( skip ).limit( limit ).into( documents );
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
        Document group = new Document()
            .append( "$group", new Document( "_id", "$dataset" ).append( "count", new Document( "$sum", 1 ) ) );

        final List<DatasetStatistic> list = new ArrayList<>();
        this.collection.aggregate( Arrays.asList( group ) ).forEach( new Block<Document>()
        {
            @Override public void apply( Document document )
            {
                DatasetStatistic statistics = new DatasetStatistic();
                statistics.setDataset( document.getString( "_id" ) );
                statistics.setNumberOfRecords( document.getInteger( "count" ) );
                list.add( statistics );
            }
        } );
        DatasetStatistics statistics = new DatasetStatistics();
        statistics.setDatasetStatistics( list );
        return statistics;
    }

}
