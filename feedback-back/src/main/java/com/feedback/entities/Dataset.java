package com.feedback.entities;

import com.feedback.except.InvalidEntityException;
import com.feedback.util.Util;
import org.bson.Document;

import java.util.List;
import java.util.Map;


/**
 * POJO for Dataset. Also Contains validation methods
 * Created by Samarth Bhargav on 8/7/15.
 */
public class Dataset
{
    private String name;
    private boolean strictValidation;
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


    public boolean isStrictValidation()
    {
        return strictValidation;
    }


    public void setStrictValidation( boolean strictValidation )
    {
        this.strictValidation = strictValidation;
    }


    private void validate() throws InvalidEntityException
    {
        if ( Util.isNullOrEmpty( this.getName() ) ) {
            throw new InvalidEntityException( "name field cannot be null" );
        }

        if ( this.isStrictValidation() ) {
            if ( this.fields == null || this.fields.isEmpty() ) {
                throw new InvalidEntityException( "strictValidation is set - provide at least one field" );
            }
        }
    }


    public void validateRecordForDataset( Record record ) throws InvalidEntityException
    {
        // Validation is not enabled
        if ( !this.strictValidation ) {
            return;
        }

        Map<String, Object> content = record.getContent();
        if ( content == null || content.isEmpty() ) {
            throw new InvalidEntityException( "content cannot be null or empty" );
        }

        // Validate against all fields
        for ( Field field : this.fields ) {
            Object object = content.get( field.getName() );
            if ( object == null ) {
                throw new InvalidEntityException( "field " + field.getName() + " must be present and be non-null" );
            }
            field.getType().validate( object, field.getName() );
            content.put( field.getName(), field.getType().convert( object ) );
        }
    }


    public Document toDocument() throws InvalidEntityException
    {
        this.validate();
        return new Document( "_id", this.getName() ).append( "fields", Field.toDocumentList( this.getFields() ) )
            .append( "updateTime", this.getUpdateTime() ).append( "strictValidation", this.strictValidation );
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
        dataset.setStrictValidation( document.getBoolean( "strictValidation" ) );
        dataset.validate();
        return dataset;
    }


    @Override
    public String toString()
    {
        return "Dataset{" +
            "name='" + name + '\'' +
            ", strictValidation=" + strictValidation +
            ", fields=" + fields +
            ", updateTime=" + updateTime +
            '}';
    }
}
