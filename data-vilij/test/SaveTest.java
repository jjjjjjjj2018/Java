/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import static java.io.File.separator;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javafx.scene.control.TextArea;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jason
 */
public class SaveTest {

    public SaveTest() {
    }

    @Test
    public void SaveTest() throws IOException, ClassNotFoundException {
        String toSave = "@instance1  label1 5,2";
        FileOutputStream file = new FileOutputStream("sample.tsd");
        ObjectOutputStream fout = new ObjectOutputStream(file);
        fout.writeObject(toSave);
        fout.close();
        String s = "";
        FileInputStream openFile = new FileInputStream("sample.tsd");
        ObjectInputStream fin = new ObjectInputStream(openFile);
        s = (String) fin.readObject();
        assertEquals(s, toSave);
    }
}
