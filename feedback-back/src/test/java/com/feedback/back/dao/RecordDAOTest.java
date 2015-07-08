package com.feedback.back.dao;

import com.feedback.back.entities.Record;
import org.junit.Assert;
import org.junit.Test;


public class RecordDAOTest extends RecordDAO {

    @Test
    public void testSave()throws Exception
    {
        RecordDAO recordDAO = RecordDAO.getInstance();
        Record record = new Record();
        recordDAO.save(record,"dataset2");
        Assert.assertNotNull(recordDAO);

    }

   @Test
    public void testGetRecord()throws Exception
   {
       String dataset,id;
       dataset="dataset";
       id="id ";
       RecordDAO recordDAO = RecordDAO.getInstance();
       recordDAO.getRecord(dataset,id);
       Assert.assertNotNull(recordDAO);
   }

}