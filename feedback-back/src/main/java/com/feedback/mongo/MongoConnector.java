package com.feedback.mongo;

import com.feedback.config.Constants;
import com.feedback.util.Util;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;

import java.util.Arrays;


/**
 * Created by Samarth Bhargav on 30/6/15.
 */
public enum MongoConnector
{
    INSTANCE;

    private final MongoClient client;


    private MongoConnector()
    {
        if ( Util.isNullOrEmpty( Constants.MONGO.USERNAME ) ) {
            client = new MongoClient( Constants.MONGO.HOST, Constants.MONGO.PORT );
        } else {
            final MongoCredential credential = MongoCredential
                .createCredential( Constants.MONGO.USERNAME, Constants.MONGO.AUTH_DB, Constants.MONGO.PASSWORD.toCharArray() );
            client = new MongoClient( new ServerAddress( Constants.MONGO.HOST, Constants.MONGO.PORT ),
                Arrays.asList( credential ) );
        }
    }


    public static MongoDatabase getDB( String dbName )
    {
        return INSTANCE.client.getDatabase( dbName );
    }

}
