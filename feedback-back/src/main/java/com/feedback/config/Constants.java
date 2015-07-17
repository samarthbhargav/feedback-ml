package com.feedback.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;


/**
 * Created by Samarth Bhargav on 8/7/15.
 */
public class Constants
{
    private Constants()
    {

    }


    public static final Config config = ConfigFactory.load();


    /**
     * Configs related to MongoDB
     */
    public static class MONGO
    {
        // ----- Connection Configs -----
        public static final String HOST = config.getString( "mongo.host" );
        public static final int PORT = config.getInt( "mongo.port" );


        public static final String USERNAME = config.getString( "mongo.username" );
        public static final String PASSWORD = config.getString( "mongo.password" );
        public static final String AUTH_DB = config.getString( "mongo.auth_db" );

        // ----- DB Names -----
        public static final String RECORDS_DB = config.getString( "mongo.db.records" );
        public static final String META_DATA_DB = config.getString( "mongo.db.metaData" );

        // ----- Collection Names -----
        public static final String COLLECTIONS_DATASET_METADATA = config.getString( "mongo.collections.datasetMetaData" );
    }

}
