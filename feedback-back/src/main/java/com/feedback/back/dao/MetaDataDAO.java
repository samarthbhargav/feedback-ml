package com.feedback.back.dao;

import com.feedback.back.config.Constants;
import com.feedback.back.entities.Dataset;
import com.feedback.back.entities.DatasetStats;
import com.feedback.back.entities.Field;
import com.feedback.back.entities.api.DatasetStatistics;
import com.feedback.back.except.DatasetNotFoundException;
import com.feedback.back.mongo.MongoConnector;
import com.mongodb.Block;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Samarth Bhargav on 6/7/15.
 *
 * TODO Add documentation
 * TODO Add INFO Logging
 * TODO Add DEBUG Logging
 */
public class MetaDataDAO
{
    private static final Logger LOG = LoggerFactory.getLogger( MetaDataDAO.class );

    private static final MetaDataDAO INSTANCE = new MetaDataDAO();


    private MongoCollection<Document> datasetMetadata = MongoConnector.getDB( Constants.MONGO.META_DATA_DB )
        .getCollection( Constants.MONGO.COLLECTIONS_DATASET_METADATA );


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

        return datasets;
    }


    public void save( Dataset dataset )
    {
        dataset.setUpdateTime( System.currentTimeMillis() );
        this.datasetMetadata.replaceOne( new Document( "_id", dataset.getName() ), dataset.toDocument(), DAOUtil.UPSERT_TRUE );
    }


    public void removeDataset( String datasetName ) throws DatasetNotFoundException
    {
        RecordDAO.getInstance().removeAll( datasetName );
        this.datasetMetadata.deleteOne( new Document( "_id", datasetName ) );
        RecordDAO.getInstance().reloadDatasets();
    }


    public Dataset getDataset( String datasetName ) throws DatasetNotFoundException
    {
        Document document = this.datasetMetadata.find( new Document( "_id", datasetName ) ).first();
        if ( document == null ) {
            throw new DatasetNotFoundException( "dataset " + datasetName + " not found!" );
        }
        return Dataset.fromDocument( document );
    }


    public List<Field> getFields( String datasetName ) throws DatasetNotFoundException
    {
        return this.getDataset( datasetName ).getFields();
    }


    public DatasetStatistics getDatasetStatistics()
    {
        final List<DatasetStats> list = new ArrayList<>();
        for ( Dataset dataset : this.getDatasets() ) {
            DatasetStats datasetStats = new DatasetStats();
            datasetStats.setDataset( dataset.getName() );
            datasetStats.setNumberOfRecords(
                MongoConnector.getDB( Constants.MONGO.RECORDS_DB ).getCollection( dataset.getName() ).count() );
            list.add( datasetStats );
        }
        DatasetStatistics statistics = new DatasetStatistics();
        statistics.setDatasetStatistics( list );
        return statistics;
    }
}
