package com.feedback.back.dao;

import com.mongodb.client.model.UpdateOptions;


/**
 * Created by Samarth Bhargav on 6/7/15.
 */
public class DAOUtil
{

    public static final UpdateOptions UPSERT_TRUE = new UpdateOptions().upsert( true );
}
