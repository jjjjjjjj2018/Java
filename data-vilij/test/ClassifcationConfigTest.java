
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jason
 */
public class ClassifcationConfigTest {
    
    
        //test positive
    @Test
    public void configPositiveTest() throws Exception {
        int maxIteration = 0, updateInterval = 0;
        int testIte = Integer.MAX_VALUE;
        int testUpdate = Integer.MAX_VALUE;
        if (testIte > 0 || testUpdate > 0 ) {
            maxIteration = testIte;
            updateInterval = testUpdate;
        }
        if (testIte <= 0 || testUpdate <= 0) {
            throw new Exception("zreo or neg");
        }

        assertEquals(testIte, maxIteration);
        assertEquals(testUpdate, updateInterval);

    }

    //test string
    @Test(expected = Exception.class)
    public void configStringTest() throws Exception {
        int maxIteration = 0, updateInterval = 0;
        String testInt = "abc";
        String testUpdate = "abc";
        maxIteration = Integer.parseInt(testInt);
        updateInterval = Integer.parseInt(testUpdate);

        throw new Exception("zero");

    }

    //test zero
    @Test(expected = Exception.class)
    public void configZeroTest() throws Exception {
        int maxIteration = 0, updateInterval = 0;
        int testIte = 0;
        int testUpdate = 0;
        if (testIte > 0 || testUpdate > 0) {
            maxIteration = testIte;
            updateInterval = testUpdate;
        }
        if (testIte <= 0 || testUpdate <= 0) {
            throw new Exception("zero");
        }

    }

    //test negative
    @Test(expected = Exception.class)
    public void configNegativeTest() throws Exception {
        int maxIteration = 0, updateInterval = 0;
        int testIte = Integer.MIN_VALUE;
        int testUpdate = Integer.MIN_VALUE;
        if (testIte > 0 || testUpdate > 0) {
            maxIteration = testIte;
            updateInterval = testUpdate;
        }
        if (testIte <= 0 || testUpdate <= 0) {
            throw new Exception("negative");
        }
    }
}
