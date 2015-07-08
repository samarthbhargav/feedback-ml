package com.feedback.back.dao;

import com.feedback.back.config.Constants;
import com.feedback.back.entities.Dataset;
import com.feedback.back.mongo.MongoConnector;
import com.mongodb.client.MongoDatabase;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;


/**
 * Created by Samarth Bhargav on 8/7/15.
 */
public class MetaDataDAOTest
{
    private static MongoDatabase DB = MongoConnector.getDB( Constants.META_DATA_DB );


    @After
    @Before
    public void cleanUp()
    {
        // Clean up after / before tests
        DB.drop();
    }


    @Test
    public void testSingleton()
    {
        MetaDataDAO metaDataDAO = MetaDataDAO.getInstance();
        Assert.assertTrue( metaDataDAO == MetaDataDAO.getInstance() );
    }


    @Test
    public void testGetDatasets()
    {
        MetaDataDAO metaDataDAO = MetaDataDAO.getInstance();
        List<Dataset> datasetList = metaDataDAO.getDatasets();
        // This should never return null
        Assert.assertNotNull( datasetList );
        Assert.assertEquals( 0, datasetList.size() );

        Dataset dataset = new Dataset();
        dataset.setName( "Blah" );
        metaDataDAO.save( dataset );

        datasetList = metaDataDAO.getDatasets();
        Assert.assertNotNull( datasetList );
        Assert.assertEquals( 1, datasetList.size() );
        Dataset savedDataset = datasetList.get( 0 );
        Assert.assertEquals( "Blah", savedDataset.getName() );
        Assert.assertNotNull( savedDataset.getUpdateTime() );
    }


}