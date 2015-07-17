package com.feedback.entities;

import com.feedback.except.InvalidEntityException;
import com.feedback.util.Util;
import org.bson.Document;

import java.util.List;


/**
 * Created by Samarth Bhargav on 8/7/15.
 */
public class Dataset
{
    private String name;
    private List<Field> fields;
    private Long updateTime;


    public String getName()
    {
        return name;
    }


    public void setName( String name )
    {
        this.name = name;
    }


    public List<Field> getFields()
    {
        return fields;
    }


    public void setFields( List<Field> fields )
    {
        this.fields = fields;
    }


    public Long getUpdateTime()
    {
        return updateTime;
    }


    public void setUpdateTime( Long updateTime )
    {
        this.updateTime = updateTime;
    }


    private void validate() throws InvalidEntityException
    {
        if ( Util.isNullOrEmpty( this.getName() ) ) {
            throw new InvalidEntityException( "name field cannot be null" );
        }
    }


    public Document toDocument() throws InvalidEntityException
    {
        this.validate();
        return new Document( "_id", this.getName() ).append( "fields", Field.toDocumentList( this.getFields() ) )
            .append( "updateTime", this.getUpdateTime() );
    }


    public static Dataset fromDocument( Document document ) throws InvalidEntityException
    {
        if ( document == null ) {
            return null;
        }
        Dataset dataset = new Dataset();
        dataset.setUpdateTime( document.getLong( "updateTime" ) );
        dataset.setFields( Field.fromDocumentList( (List<Document>) document.get( "fields" ) ) );
        dataset.setName( document.getString( "_id" ) );
        dataset.validate();
        return dataset;
    }


    @Override
    public String toString()
    {
        return "Dataset{" +
            "name='" + name + '\'' +
            ", fields=" + fields +
            ", updateTime=" + updateTime +
            '}';
    }
}
