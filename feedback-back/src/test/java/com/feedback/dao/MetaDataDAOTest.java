package com.feedback.dao;

import com.feedback.config.Constants;
import com.feedback.entities.*;
import com.feedback.entities.api.DatasetStatistics;
import com.feedback.except.DatasetNotFoundException;
import com.feedback.mongo.MongoConnector;
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
    private static MongoDatabase META_DB = MongoConnector.getDB( Constants.MONGO.META_DATA_DB );
    private static MongoDatabase DB = MongoConnector.getDB( Constants.MONGO.RECORDS_DB );


    @After
    @Before
    public void cleanUp()
    {
        // Clean up after / before tests
        DB.drop();
        META_DB.drop();
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
    public void testGetDatasets() throws Exception
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

        dataset.setFields(
            Arrays.asList( new Field( FieldType.CATEGORICAL, "field1" ), new Field( FieldType.NUMERICAL, "field2" ) ) );
        metaDataDAO.save( dataset );
        Dataset savedDataset = metaDataDAO.getDataset( "dataset" );
        assertDatasetCorrectness( savedDataset, "dataset" );

        List<Field> fields = metaDataDAO.getFields( "dataset" );
        Assert.assertEquals( FieldType.CATEGORICAL, fields.get( 0 ).getType() );
        Assert.assertEquals( FieldType.NUMERICAL, fields.get( 1 ).getType() );
        Assert.assertEquals( "field1", fields.get( 0 ).getName() );
        Assert.assertEquals( "field2", fields.get( 1 ).getName() );

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


    @Test
    public void testDatasetStatistics() throws Exception
    {
        MetaDataDAO metaDataDAO = MetaDataDAO.getInstance();
        // For 0 datasets, it should be empty
        DatasetStatistics statistics = metaDataDAO.getDatasetStatistics();
        Assert.assertNotNull( statistics );
        Assert.assertNotNull( statistics.getDatasetStatistics() );
        Assert.assertTrue( statistics.getDatasetStatistics().isEmpty() );


        // Add a dataset with 0 records
        Dataset dataset = new Dataset();
        dataset.setName( "dataset" );
        metaDataDAO.save( dataset );

        statistics = metaDataDAO.getDatasetStatistics();
        Assert.assertEquals( 1, statistics.getDatasetStatistics().size() );
        DatasetStats stats = statistics.getDatasetStatistics().get( 0 );
        Assert.assertEquals( 0, stats.getNumberOfRecords() );
        Assert.assertEquals( dataset.getName(), stats.getDataset() );

        // Add one record
        Record record = new Record();
        record.setId( "Bleh" );
        RecordDAO.getInstance().save( dataset.getName(), record );
        statistics = metaDataDAO.getDatasetStatistics();
        Assert.assertEquals( 1, statistics.getDatasetStatistics().size() );
        stats = statistics.getDatasetStatistics().get( 0 );
        Assert.assertEquals( 1, stats.getNumberOfRecords() );
        Assert.assertEquals( dataset.getName(), stats.getDataset() );
    }


    @Test (expected = DatasetNotFoundException.class)
    public void testInvalidRemoveDataset() throws Exception
    {
        MetaDataDAO metaDataDAO = MetaDataDAO.getInstance();
        metaDataDAO.removeDataset( "non-existent-dataset" );
    }


    @Test
    public void testRemoveDataset() throws Exception
    {
        MetaDataDAO metaDataDAO = MetaDataDAO.getInstance();

        Dataset dataset = new Dataset();
        dataset.setName( "bleh" );
        metaDataDAO.save( dataset );

        metaDataDAO.removeDataset( dataset.getName() );

        // Dataset should be removed
        Assert.assertEquals( 0, metaDataDAO.getDatasets().size() );
    }
}