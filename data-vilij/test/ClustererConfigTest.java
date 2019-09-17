
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
public class ClustererConfigTest {
    //test positive

    @Test
    public void configPositiveTest() throws Exception {
        int maxIteration = 0, updateInterval = 0, numClust = 0;
        int testIte = Integer.MAX_VALUE;
        int testUpdate = Integer.MAX_VALUE;
        int testNumClust = Integer.MAX_VALUE;
        if (testIte > 0 || testUpdate > 0 || testNumClust > 0) {
            maxIteration = testIte;
            updateInterval = testUpdate;
            numClust = testNumClust;
        }
        if (testIte <= 0 || testUpdate <= 0 || testNumClust <= 0) {
            throw new Exception("zreo or neg");
        }
        assertEquals(testIte, maxIteration);
        assertEquals(testUpdate, updateInterval);
        assertEquals(testNumClust, numClust);

    }

    //test string
    @Test(expected = Exception.class)
    public void configStringTest() throws Exception {
        int maxIteration = 0, updateInterval = 0, numClust = 0;
        String testIte = "abc";
        String testUpdate = "abc";
        String testNumClust = "abc";
        maxIteration = Integer.parseInt(testIte);
        updateInterval = Integer.parseInt(testUpdate);
        numClust = Integer.parseInt(testNumClust);
        throw new Exception("zero");
    }

    //test zero
    @Test(expected = Exception.class)
    public void configZeroTest() throws Exception {
        int maxIteration = 0, updateInterval = 0, numClust = 0;
        int testIte = 0;
        int testUpdate = 0;
        int testClust = 0;
        if (testIte > 0 || testUpdate > 0 || testClust > 0) {
            maxIteration = testIte;
            updateInterval = testUpdate;
            numClust = testClust;
        }
        if (testIte <= 0 || testUpdate <= 0 || testClust <= 0) {
            throw new Exception("zero");
        }

    }

    //test negative
    @Test(expected = Exception.class)
    public void configNegativeTest() throws Exception {
        int maxIteration = 0, updateInterval = 0, numClust = 0;
        int testIte = Integer.MIN_VALUE;
        int testUpdate = Integer.MIN_VALUE;
        int testNumClust = Integer.MIN_VALUE;
        if (testIte > 0 || testUpdate > 0 || testNumClust > 0) {
            maxIteration = testIte;
            updateInterval = testUpdate;
            numClust = testNumClust;
        }
        if (testIte <= 0 || testUpdate <= 0 || testNumClust <= 0) {
            throw new Exception("negative");
        }
    }
}
