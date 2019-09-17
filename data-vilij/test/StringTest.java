
import dataprocessors.TSDProcessor;
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
public class StringTest {

    //good string test
    @Test
    public void StringTest() throws Exception {
        String test = "@instance1\tlabel1\t5,2";
        TSDProcessor processor = new TSDProcessor();
        processor.processString(test);
    }

    //bad string test
    @Test(expected = Exception.class)
    public void badStringTest() throws Exception {
        String test = "askjghkjdsfhKHJ";
        TSDProcessor processor = new TSDProcessor();
        processor.processString(test);
        throw new Exception("bad string");
    }
}
