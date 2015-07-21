package com.feedback.util;

/**
 * Created by Samarth Bhargav on 10/7/15.
 */
public class Util
{

    public static boolean isNullOrEmpty( String string )
    {
        return string == null || string.isEmpty();
    }


    public static Double getDouble( Object object )
    {
        if ( object == null ) {
            return null;
        }
        if ( object instanceof Number ) {
            return ( (Number) object ).doubleValue();
        } else {
            try {
                return Double.parseDouble( object.toString() );
            } catch ( NumberFormatException e ) {
                // Ignore exception
                return null;
            }
        }
    }
}
