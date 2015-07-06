package com.feedback.back.dao;

import com.feedback.back.mongo.MongoConnector;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Samarth Bhargav on 6/7/15.
 */
public class MetaDataDAO
{
    private static final Logger LOG = LoggerFactory.getLogger( MetaDataDAO.class );

    private static final MetaDataDAO INSTANCE = new MetaDataDAO();


    private MongoCollection<Document> datasetMetadata = MongoConnector.getDB( "feedback" ).getCollection( "dataset-metadata" );


    private MetaDataDAO()
    {

    }


    public static MetaDataDAO getInstance()
    {
        return INSTANCE;
    }


    public List<String> getDatasets()
    {
        List<String> datasetNames = new ArrayList<>();
        this.datasetMetadata.distinct( "_id", String.class ).into( datasetNames );
        return datasetNames;
    }


    public void addDataset( String dataset )
    {
        this.datasetMetadata.insertOne( new Document( "_id", dataset ) );
    }
}
