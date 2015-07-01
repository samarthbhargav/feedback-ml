package com.feedback.back.entities;

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


    public Document toDocument()
    {
        Document document = new Document();
        document.put( "_id", this.getId() );
        document.put( "label", this.getLabel() );
        document.put( "content", this.getContent() );
        return document;
    }


    public static Record fromDocument( Document object )
    {
        if ( object == null ) {
            return null;
        }
        Record record = new Record();
        record.setContent( (Map<String, String>) object.get( "content" ) );
        record.setId( (String) object.get( "_id" ) );
        record.setLabel( (String) object.get( "label" ) );
        return record;
    }
}
