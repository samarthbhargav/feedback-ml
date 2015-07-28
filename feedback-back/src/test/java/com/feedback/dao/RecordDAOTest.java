package com.feedback.dao;

import com.feedback.config.Constants;
import com.feedback.entities.Dataset;
import com.feedback.entities.Record;
import com.feedback.entities.RecordStats;
import com.feedback.entities.api.RecordStatistics;
import com.feedback.entities.api.RecordsPage;
import com.feedback.except.DatasetNotFoundException;
import com.feedback.except.InvalidEntityException;
import com.feedback.mongo.MongoConnector;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;


/**
 * Created by Samarth Bhargav on 9/7/15.
 */
public class RecordDAOTest
{
    private static MongoDatabase META_DB = MongoConnector.getDB( Constants.MONGO.META_DATA_DB );
    private static MongoDatabase DB = MongoConnector.getDB( Constants.MONGO.RECORDS_DB );
    private static final String DATASET = "dataset";


    private void createDataset() throws Exception
    {
        Dataset dataset = new Dataset();
        dataset.setName( DATASET );
        MetaDataDAO.getInstance().save( dataset );
    }


    @Before
    @After
    public void cleanup() throws Exception
    {
        META_DB.drop();
        DB.drop();
        RecordDAO.getInstance().reloadDatasets();
    }


    @Test
    public void testSingleton() throws Exception
    {
        RecordDAO recordDAO = RecordDAO.getInstance();
        Assert.assertTrue( recordDAO == RecordDAO.getInstance() );
    }


    @Test (expected = DatasetNotFoundException.class)
    public void testDatasetNotFoundExceptionThrown() throws Exception
    {
        RecordDAO recordDAO = RecordDAO.getInstance();
        Record record = new Record();
        record.setId( "ID" );
        Map<String, Object> content = new HashMap<>();
        content.put( "content-key", "content-value" );
        record.setContent( content );
        record.setLabel( "label" );

        recordDAO.save( DATASET, record );
    }


    @Test
    public void testSave() throws Exception
    {
        createDataset();
        RecordDAO recordDAO = RecordDAO.getInstance();
        Record record = new Record();
        record.setId( "ID" );
        Map<String, Object> content = new HashMap<>();
        content.put( "content-key", "content-value" );
        record.setContent( content );
        record.setLabel( "label" );

        recordDAO.save( DATASET, record );

        Record savedRecord = recordDAO.getRecord( DATASET, "ID" );
        Assert.assertNotNull( savedRecord );
        Assert.assertEquals( record.getContent(), savedRecord.getContent() );
        Assert.assertEquals( record.getId(), savedRecord.getId() );
        Assert.assertEquals( record.getLabel(), savedRecord.getLabel() );
        Assert.assertEquals( record.getContent(), savedRecord.getContent() );
    }


    @Test
    public void testRecordStatistics() throws Exception
    {
        createDataset();
        RecordDAO recordDAO = RecordDAO.getInstance();
        RecordStatistics statistics = recordDAO.getRecordStatistics( DATASET );
        Assert.assertNotNull( statistics );
        Assert.assertNotNull( statistics.getStats() );
        Assert.assertTrue( statistics.getStats().isEmpty() );

        Integer recordId = 0;

        int howManyLabels = 10;
        int howManyPerLabel = 10;

        for ( Integer i = 0; i < howManyLabels; i++ ) {
            for ( int j = 0; j < howManyPerLabel; j++ ) {
                Record record = new Record();
                record.setId( ( recordId++ ).toString() );
                Map<String, Object> content = new HashMap<>();
                content.put( "content-key", "content-value" );
                record.setContent( content );
                record.setLabel( i.toString() );
                recordDAO.save( DATASET, record );
            }
        }

        statistics = recordDAO.getRecordStatistics( DATASET );
        List<RecordStats> recordStatsList = statistics.getStats();

        Assert.assertEquals( howManyLabels, recordStatsList.size() );
        for ( RecordStats recordStats : recordStatsList ) {
            Assert.assertEquals( howManyPerLabel, recordStats.getNumberOfRecords() );
        }

        // Add a null records
        for ( int j = 0; j < howManyPerLabel; j++ ) {
            Record record = new Record();
            record.setId( ( recordId++ ).toString() );
            Map<String, Object> content = new HashMap<>();
            content.put( "content-key", "content-value" );
            record.setContent( content );
            recordDAO.save( DATASET, record );
        }


        statistics = recordDAO.getRecordStatistics( DATASET );
        recordStatsList = statistics.getStats();
        // +1 for null label
        Assert.assertEquals( howManyLabels + 1, recordStatsList.size() );
        for ( RecordStats recordStats : recordStatsList ) {
            Assert.assertEquals( howManyPerLabel, recordStats.getNumberOfRecords() );
        }
    }


    @Test
    public void testNullRecord() throws Exception
    {
        createDataset();
        Assert.assertNull( RecordDAO.getInstance().getRecord( DATASET, "non-existent" ) );
    }


    @Test (expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionThrown() throws Exception
    {
        createDataset();
        RecordDAO recordDAO = RecordDAO.getInstance();
        recordDAO.getRecordsPage( DATASET, -1, -1 );
    }


    @Test
    public void testRecordsPage() throws Exception
    {
        createDataset();
        RecordDAO recordDAO = RecordDAO.getInstance();
        RecordsPage recordsPage = recordDAO.getRecordsPage( DATASET, 0, 10 );
        Assert.assertNotNull( recordsPage );
        Assert.assertNotNull( recordsPage.getRecords() );
        Assert.assertTrue( recordsPage.getRecords().isEmpty() );
        Assert.assertEquals( recordsPage.getTotalNumberOfRecords(), 0 );

        // Add 100 records
        int howMany = 100;
        for ( Integer i = 0; i < howMany; i++ ) {
            Record record = new Record();
            record.setId( i.toString() );
            Map<String, Object> content = new HashMap<>();
            content.put( "content-key", "content-value" );
            record.setContent( content );
            recordDAO.save( DATASET, record );
        }

        Integer limit = 10;
        Integer skip = 0;
        recordsPage = recordDAO.getRecordsPage( DATASET, skip, limit );
        Assert.assertNotNull( recordsPage );
        Assert.assertNotNull( recordsPage.getRecords() );
        Assert.assertTrue( limit == recordsPage.getLimit() );
        Assert.assertTrue( skip == recordsPage.getSkip() );
        Assert.assertEquals( skip.toString(), recordsPage.getRecords().get( 0 ).getId() );
        Assert.assertEquals( String.valueOf( skip + limit - 1 ), recordsPage.getRecords().get( limit - 1 ).getId() );
        Assert.assertEquals( recordsPage.getTotalNumberOfRecords(), 100 );

        skip = 50;
        limit = 20;
        recordsPage = recordDAO.getRecordsPage( DATASET, skip, limit );
        Assert.assertNotNull( recordsPage );
        Assert.assertNotNull( recordsPage.getRecords() );
        Assert.assertTrue( limit == recordsPage.getLimit() );
        Assert.assertTrue( skip == recordsPage.getSkip() );
        Assert.assertEquals( skip.toString(), recordsPage.getRecords().get( 0 ).getId() );
        Assert.assertEquals( String.valueOf( skip + limit - 1 ), recordsPage.getRecords().get( limit - 1 ).getId() );
        Assert.assertEquals( recordsPage.getTotalNumberOfRecords(), 100 );
        // There are only 100 records, so check if only 50 are there
        skip = 50;
        limit = 100;
        recordsPage = recordDAO.getRecordsPage( DATASET, skip, limit );
        Assert.assertNotNull( recordsPage );
        Assert.assertNotNull( recordsPage.getRecords() );
        Assert.assertTrue( limit == recordsPage.getLimit() );
        Assert.assertTrue( skip == recordsPage.getSkip() );
        Assert.assertEquals( skip.toString(), recordsPage.getRecords().get( 0 ).getId() );
        Assert.assertEquals( String.valueOf( howMany - 1 ), recordsPage.getRecords().get( howMany - skip - 1 ).getId() );
        Assert.assertEquals( recordsPage.getTotalNumberOfRecords(), 100 );

        skip = 100;
        limit = 100;
        recordsPage = recordDAO.getRecordsPage( DATASET, skip, limit );
        Assert.assertNotNull( recordsPage );
        Assert.assertNotNull( recordsPage.getRecords() );
        Assert.assertTrue( recordsPage.getRecords().isEmpty() );
        Assert.assertEquals( recordsPage.getTotalNumberOfRecords(), 100 );
    }


    @Test
    public void testBulkSave() throws Exception
    {
        createDataset();
        RecordDAO recordDAO = RecordDAO.getInstance();
        int howMany = 100;
        List<Record> records = new ArrayList<>( howMany );
        for ( Integer i = 0; i < howMany; i++ ) {
            Record record = new Record();
            record.setId( i.toString() );
            Map<String, Object> content = new HashMap<>();
            content.put( "content-key", "content-value" );
            record.setContent( content );
            records.add( record );
        }

        // Bulk save
        recordDAO.saveBulk( DATASET, records );

        RecordsPage page = recordDAO.getRecordsPage( DATASET, 0, 1000 );
        Assert.assertEquals( howMany, page.getRecords().size() );
    }


    @Test
    public void testRemove() throws Exception
    {
        createDataset();
        RecordDAO recordDAO = RecordDAO.getInstance();

        // Doesn't exist - no exception must be thrown
        recordDAO.remove( DATASET, "id" );

        // Save 10 records
        int howMany = 10;
        for ( Integer i = 0; i < howMany; i++ ) {
            Record record = new Record();
            record.setId( i.toString() );
            Map<String, Object> content = new HashMap<>();
            content.put( "content-key", "content-value" );
            record.setContent( content );
            recordDAO.save( DATASET, record );
        }

        // Removing one document
        DeleteResult result = recordDAO.remove( DATASET, "0" );
        Assert.assertEquals( 1, result.getDeletedCount() );

        // Remove bulk
        List<String> toRemove = Arrays.asList( "1", "2" );
        result = recordDAO.removeBulk( DATASET, toRemove );
        Assert.assertEquals( toRemove.size(), result.getDeletedCount() );

        // Remove the rest
        result = recordDAO.removeAll( DATASET );
        Assert.assertEquals( result.getDeletedCount(), howMany - toRemove.size() - 1 );
    }


    @Test (expected = InvalidEntityException.class)
    public void testInvalidRecord() throws Exception
    {
        createDataset();
        Record record = new Record();
        RecordDAO.getInstance().save( DATASET, record );
    }


    @Test
    public void testLabelRecord() throws Exception
    {
        createDataset();
        RecordDAO recordDAO = RecordDAO.getInstance();
        recordDAO.labelRecord( DATASET, "someID", "newLabel" );

        // No document should be created if label is set for non-existent record
        Assert.assertEquals( 0, DB.getCollection( DATASET ).count() );

        Record record = new Record();
        record.setId( "someID" );
        record.setLabel( null );

        recordDAO.save( DATASET, record );

        recordDAO.labelRecord( DATASET, "someID", "newLabel" );
        Assert.assertEquals( "newLabel", recordDAO.getRecord( DATASET, "someID" ).getLabel() );

        recordDAO.labelRecord( DATASET, "someID", "newerLabel" );
        Assert.assertEquals( "newerLabel", recordDAO.getRecord( DATASET, "someID" ).getLabel() );
    }
}