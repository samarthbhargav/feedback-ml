package com.feedback.entities;

import com.feedback.except.InvalidEntityException;
import com.feedback.util.Util;


/**
 * Created by Samarth Bhargav on 16/7/15.
 */
public enum FieldType
{
    NUMERICAL, CATEGORICAL, TEXT;


    public void validate( Object object, String fieldName ) throws InvalidEntityException
    {
        switch ( this ) {
            case CATEGORICAL:
                // Can be anything - leave data as is
                // TODO
                break;
            case NUMERICAL:
                Double d = Util.getDouble( object );
                if ( d == null ) {
                    throw new InvalidEntityException( "field " + fieldName + " must be of type " + NUMERICAL );
                }
                break;
            case TEXT:
                if ( !( object instanceof String ) ) {
                    throw new InvalidEntityException( "field " + fieldName + " must be of type " + TEXT );
                }
                break;
            default:
                throw new IllegalStateException();
        }
    }


    public Object convert( Object object )
    {
        switch ( this ) {
            case CATEGORICAL:
                return toCategorical( object );
            case NUMERICAL:
                return toNumerical( object );
            case TEXT:
                return toText( object );
            default:
                throw new IllegalStateException();
        }
    }


    private Object toCategorical( Object object )
    {
        return object;
    }


    private Double toNumerical( Object object )
    {
        return Util.getDouble( object );
    }


    private String toText( Object object )
    {
        if ( object == null ) {
            return null;
        }
        return object.toString();
    }
}
