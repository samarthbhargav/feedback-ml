package com.feedback.back.dao;

import com.feedback.back.config.Constants;
import com.feedback.back.entities.Dataset;
import com.feedback.back.mongo.MongoConnector;
import com.mongodb.Block;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


/**
 * Created by Samarth Bhargav on 6/7/15.
 */
public class MetaDataDAO
{
    private static final Logger LOG = LoggerFactory.getLogger( MetaDataDAO.class );

    private static final MetaDataDAO INSTANCE = new MetaDataDAO();


    private MongoCollection<Document> datasetMetadata = MongoConnector.getDB( Constants.META_DATA_DB )
        .getCollection( Constants.COLLECTIONS_DATASET_METADATA );


    private MetaDataDAO()
    {

    }


    public static MetaDataDAO getInstance()
    {
        return INSTANCE;
    }


    public List<Dataset> getDatasets()
    {
        final List<Dataset> datasets = new ArrayList<>();

        this.datasetMetadata.find().forEach( new Block<Document>()
        {
            @Override
            public void apply( Document document )
            {
                datasets.add( Dataset.fromDocument( document ) );
            }
        } );
        ;
        return datasets;
    }


    public void save( Dataset dataset )
    {
        dataset.setUpdateTime( System.currentTimeMillis() );
        this.datasetMetadata.replaceOne( new Document( "_id", dataset.getName() ), dataset.toDocument(), DAOUtil.UPSERT_TRUE );
    }


    public Dataset getDataset( String datasetName )
    {
        return Dataset.fromDocument( this.datasetMetadata.find( new Document( "_id", datasetName ) ).first() );
    }


    public List<String> getFields( String dataset )
    {
        Document document = this.datasetMetadata.find( new Document( "_id", dataset ) ).first();
        if ( dataset == null ) {
            return null;
        } else {
            return Dataset.fromDocument( document ).getFields();
        }
    }


    public void addField( String dataset, String fieldName )
    {
        this.addFields( dataset, Arrays.asList( fieldName ) );
    }


    public void addFields( String dataset, Collection<String> fields )
    {
        this.datasetMetadata
            .updateOne( new Document( "_id", dataset ), new Document( "$addToSet", new Document( "fields", fields ) ),
                new UpdateOptions().upsert( false ) );
    }

}
