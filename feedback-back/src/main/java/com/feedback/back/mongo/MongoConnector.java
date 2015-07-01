package com.feedback.back.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;


/**
 * Created by Samarth Bhargav on 30/6/15.
 */
public enum MongoConnector
{
    INSTANCE;

    private MongoClient client;


    private MongoConnector()
    {
        // TODO make this configurable
        client = new MongoClient( new MongoClientURI( "mongodb://localhost:27017" ) );
    }


    public static MongoDatabase getDB( String dbName )
    {
        return INSTANCE.client.getDatabase( dbName );
    }

}
