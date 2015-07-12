package com.feedback.back.entities;

import com.feedback.back.except.InvalidEntityException;
import com.feedback.util.Util;
import org.bson.Document;

import java.util.Map;


/**
 * Created by Samarth Bhargav on 30/6/15.
 */
public class Record
{
    private String label;
    private String id;
    private Map<String, String> content;
    private Long lastModified;


    public String getId()
    {
        return id;
    }


    public void setId( String id )
    {
        this.id = id;
    }


    public Map<String, String> getContent()
    {
        return content;
    }


    public void setContent( Map<String, String> content )
    {
        this.content = content;
    }


    public String getLabel()
    {
        return label;
    }


    public void setLabel( String label )
    {
        this.label = label;
    }


    public Long getLastModified()
    {
        return lastModified;
    }


    public void setLastModified( Long lastModified )
    {
        this.lastModified = lastModified;
    }


    public Document toDocument() throws InvalidEntityException
    {
        this.validate();
        Document document = new Document();
        document.put( "_id", this.getId() );
        document.put( "label", this.getLabel() );
        document.put( "content", this.getContent() );
        document.put( "lastModified", this.getLastModified() );
        return document;
    }


    public static Record fromDocument( Document object ) throws InvalidEntityException
    {
        if ( object == null ) {
            return null;
        }
        Record record = new Record();
        record.setContent( (Map<String, String>) object.get( "content" ) );
        record.setId( object.getString( "_id" ) );
        record.setLabel( (String) object.get( "label" ) );
        record.setLastModified( object.getLong( "lastModified" ) );
        record.validate();
        return record;
    }


    private void validate() throws InvalidEntityException
    {
        if ( Util.isNullOrEmpty( this.getId() ) ) {
            throw new InvalidEntityException( "id field cannot be null" );
        }
    }


    @Override
    public String toString()
    {
        return "Record{" +
            "label='" + label + '\'' +
            ", id='" + id + '\'' +
            ", content=" + content +
            ", lastModified=" + lastModified +
            '}';
    }
}
