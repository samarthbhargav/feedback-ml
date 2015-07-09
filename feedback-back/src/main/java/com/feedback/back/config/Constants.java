package com.feedback.back.config;

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

    public static final String RECORDS_DB = config.getString( "mongo.db.records" );
    public static final String META_DATA_DB = config.getString( "mongo.db.metaData" );

    public static final String COLLECTIONS_DATASET_METADATA = config.getString( "mongo.collections.datasetMetaData" );

}
