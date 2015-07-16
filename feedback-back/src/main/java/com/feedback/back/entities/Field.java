package com.feedback.back.entities;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Samarth Bhargav on 16/7/15.
 */
public class Field
{
    private FieldType type;
    private String name;


    public Field()
    {
    }


    public Field( FieldType type, String name )
    {
        this.type = type;
        this.name = name;
    }


    public FieldType getType()
    {
        return type;
    }


    public void setType( FieldType type )
    {
        this.type = type;
    }


    public String getName()
    {
        return name;
    }


    public void setName( String name )
    {
        this.name = name;
    }


    public Document toDocument()
    {
        Document document = new Document();
        document.put( "name", this.getName() );
        document.put( "type", this.getType().name() );
        return document;
    }


    public static Field fromDocument( Document document )
    {
        if ( document == null ) {
            return null;
        }
        Field field = new Field();
        field.setName( document.getString( "name" ) );
        field.setType( FieldType.valueOf( document.getString( "type" ) ) );
        return field;
    }


    public static List<Document> toDocumentList( List<Field> fields )
    {
        if ( fields == null ) {
            return null;
        }
        List<Document> documents = new ArrayList<>( fields.size() );
        for ( Field field : fields ) {
            documents.add( field.toDocument() );
        }
        return documents;
    }


    public static List<Field> fromDocumentList( List<Document> documents )
    {
        if ( documents == null ) {
            return null;
        }
        List<Field> fields = new ArrayList<>( documents.size() );
        for ( Document document : documents ) {
            fields.add( Field.fromDocument( document ) );
        }
        return fields;
    }


    @Override
    public String toString()
    {
        return "Field{" +
            "type=" + type +
            ", name='" + name + '\'' +
            '}';
    }
}
