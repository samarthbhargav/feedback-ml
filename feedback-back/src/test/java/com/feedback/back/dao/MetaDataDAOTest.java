package com.feedback.back.dao;

import com.feedback.back.config.Constants;
import com.feedback.back.entities.Dataset;
import com.feedback.back.except.DatasetNotFoundException;
import com.feedback.back.mongo.MongoConnector;
import com.mongodb.client.MongoDatabase;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
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


    public void assertDatasetCorrectness( Dataset dataset, String name )
    {
        Assert.assertNotNull( dataset );
        Assert.assertNotNull( dataset.getName() );
        Assert.assertNotNull( dataset.getUpdateTime() );
        Assert.assertEquals( name, dataset.getName() );
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
        assertDatasetCorrectness( datasetList.get( 0 ), "Blah" );
    }


    @Test (expected = DatasetNotFoundException.class)
    public void testNonExistentDataset() throws Exception
    {
        MetaDataDAO metaDataDAO = MetaDataDAO.getInstance();
        metaDataDAO.getDataset( "non-existent-dataset" );
    }


    @Test
    public void testGetDataset() throws Exception
    {
        MetaDataDAO metaDataDAO = MetaDataDAO.getInstance();
        Dataset dataset = new Dataset();
        dataset.setName( "dataset" );
        metaDataDAO.save( dataset );
        assertDatasetCorrectness( metaDataDAO.getDataset( "dataset" ), "dataset" );
    }


    @Test
    public void testGetFields() throws Exception
    {
        MetaDataDAO metaDataDAO = MetaDataDAO.getInstance();
        Dataset dataset = new Dataset();
        dataset.setName( "dataset" );
        dataset.setFields( Arrays.asList( "field1", "field2" ) );
        metaDataDAO.save( dataset );
        Dataset savedDataset = metaDataDAO.getDataset( "dataset" );
        assertDatasetCorrectness( savedDataset, "dataset" );
        Assert.assertEquals( Arrays.asList( "field1", "field2" ), savedDataset.getFields() );


        List<String> fields = metaDataDAO.getFields( "dataset" );
        Assert.assertEquals( Arrays.asList( "field1", "field2" ), fields );
    }


    @Test
    public void testNullFields() throws Exception
    {
        MetaDataDAO metaDataDAO = MetaDataDAO.getInstance();
        Dataset dataset = new Dataset();
        dataset.setName( "dataset" );
        metaDataDAO.save( dataset );
        Dataset savedDataset = metaDataDAO.getDataset( "dataset" );
        assertDatasetCorrectness( savedDataset, "dataset" );
        Assert.assertEquals( null, savedDataset.getFields() );
    }


    @Test (expected = DatasetNotFoundException.class)
    public void testGetFieldsInvalidDataset() throws Exception
    {
        MetaDataDAO.getInstance().getFields( "dataset-1" );
    }

}