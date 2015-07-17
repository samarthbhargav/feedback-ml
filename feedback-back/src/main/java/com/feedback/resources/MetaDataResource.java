package com.feedback.resources;

import com.feedback.entities.FieldType;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;


/**
 * Created by Samarth Bhargav on 16/7/15.
 */
@Path ("/meta")
public class MetaDataResource
{
    private static class FieldTypeList
    {

        private List<FieldType> types;


        public List<FieldType> getTypes()
        {
            return types;
        }


        public void setTypes( List<FieldType> types )
        {
            this.types = types;
        }
    }


    @GET
    @Path ("/availableFieldTypes")
    @Produces (MediaType.APPLICATION_JSON)
    public Response getAvailableFieldTypes()
    {
        FieldTypeList fieldTypeList = new FieldTypeList();
        fieldTypeList.setTypes( Arrays.asList( FieldType.CATEGORICAL, FieldType.NUMERICAL, FieldType.TEXT ) );
        return Response.ok( fieldTypeList ).build();
    }
}
