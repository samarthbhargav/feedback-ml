package com.feedback.stats;

import com.feedback.config.Constants;
import com.feedback.dao.MetaDataDAO;
import com.feedback.entities.Dataset;
import com.feedback.entities.Record;
import com.feedback.except.DatasetNotFoundException;
import com.feedback.except.InvalidEntityException;
import com.feedback.mongo.MongoConnector;
import com.feedback.stats.entities.FiveNumberSummary;
import com.feedback.util.Util;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Sorts;
import org.bson.Document;

import java.util.Arrays;


/**
 * Created by Samarth Bhargav on 16/7/15.
 */
public class StatsService
{
    private final MetaDataDAO metaDataDAO = MetaDataDAO.getInstance();
    private MongoDatabase recordsDB = MongoConnector.getDB( Constants.MONGO.RECORDS_DB );


    public FiveNumberSummary getFiveNumberSummary( String datasetName, String featureName )
        throws DatasetNotFoundException, InvalidEntityException
    {
        // TODO do validation if strict
        Dataset dataset = metaDataDAO.getDataset( datasetName );

        MongoCollection<Document> collection = recordsDB.getCollection( dataset.getName() );

        FiveNumberSummary fiveNumberSummary = new FiveNumberSummary();

        String fieldName = "content." + featureName;
        Document criteria = new Document( fieldName, new Document( "$ne", null ) );
        System.out.println( criteria );
        long total = collection.count( criteria );
        if ( total == 0 ) {
            // No records, return every thing null
            return fiveNumberSummary;
        }
        if ( total % 2 == 0 ) {
            // Even number - get 2 middle elements and split the difference
            MongoCursor<Document> docs = collection.find( criteria ).sort( Sorts.ascending( fieldName ) )
                .skip( (int) ( ( total / 2 ) - 1 ) ).limit( 2 ).batchSize( 2 ).iterator();
            Document left = docs.next();
            Document right = docs.next();
            Double leftVal = Util.getDouble( Record.getContent( left, featureName ) );
            Double rightVal = Util.getDouble( Record.getContent( right, featureName ) );
            fiveNumberSummary.setMedian( ( leftVal + rightVal ) / 2 );

            // TODO set FQ and TQ
        } else {
            System.out.println( "Odd" );
            // Odd number - get the middle element
            Document middle = collection.find().sort( Sorts.descending( fieldName ) ).skip( (int) total / 2 ).first();
            fiveNumberSummary.setMedian( Util.getDouble( Record.getContent( middle, featureName ) ) );

            // TODO set FQ and TQ
        }

        String aggrFieldName = "$" + fieldName;
        Document match = new Document( "$match", criteria );
        Document group = new Document( "$group",
            new Document( "_id", "" ).append( "min", new Document( "$min", aggrFieldName ) )
                .append( "max", new Document( "$max", aggrFieldName ) ).append( "sum", new Document( "$sum", aggrFieldName ) )
                .append( "count", new Document( "$sum", 1 ) ) );


        Document results = collection.aggregate( Arrays.asList( match, group ) ).allowDiskUse( true ).batchSize( 1 )
            .useCursor( false ).first();

        Double min = Util.getDouble( results.get( "min" ) );
        Double max = Util.getDouble( results.get( "max" ) );
        Double sum = Util.getDouble( results.get( "sum" ) );
        Double count = Util.getDouble( results.get( "count" ) );


        fiveNumberSummary.setMin( min );
        fiveNumberSummary.setMax( max );

        if ( count > 0 ) {
            fiveNumberSummary.setMean( sum / count );
        } else {
            // TODO LOG
        }

        return fiveNumberSummary;
    }
}
