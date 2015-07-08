package com.feedback.back.dao;

import com.feedback.back.entities.Record;
import org.junit.*;


public class RecordDAOTest {

    @BeforeClass
    public static void beforeClass() {
        // Setup dummy database
        System.out.println("Before Tests start executing");
    }

    @AfterClass
    public static void afterClass() {
        // Remove dummy database
        System.out.println("After every test has executed");
    }

    @Before
    public void beforeEveryTest() {
        // Do something before every test
        System.out.println("Before every test");
    }

    @After
    public void afterEveryTest() {
        // Do somethign after every test
        System.out.println("After every test!");
    }

    @Test
    public void testSave() throws Exception {
        RecordDAO recordDAO = RecordDAO.getInstance();
        Record record = new Record();
        record.setId("someID");
        record.setLabel("someLabel");
        recordDAO.save(record, "someDataset");

        Record savedRecord = recordDAO.getRecord("someDataset", "someID");

        Assert.assertNotNull(savedRecord);
        Assert.assertEquals("someID", savedRecord.getId());
        Assert.assertEquals("someLabel", savedRecord.getLabel());
    }


    @Test
    public void thisIsATestToo() {
        System.out.println("This is a test");
    }

    @Test
    public void failingTest() {
        System.out.println("This test will fail");

        Assert.assertTrue(false);
    }
}