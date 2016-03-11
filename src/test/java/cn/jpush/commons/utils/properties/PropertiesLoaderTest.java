package cn.jpush.commons.utils.properties;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.util.Map;

/**
 * PropertiesLoader Tester.
 * @author linzh
 * @version 1.0
 * @since <pre>三月 11, 2016</pre>
 */
public class PropertiesLoaderTest {
    /**
     * Method: load(Class<T> cls)
     */
    @Test
    public void testLoadCls() throws Exception {
        System.out.println("Method: load(Class<T> cls)");
        PropertiesLoader.load(Propertity1.class).print();
        System.out.println("---------------------------------");
        new Propertity1().print();
    }

    /**
     * Method: staticLoad(Class<T> cls)
     */
    public void testStatiLoadCls() throws Exception {
        System.out.println(" Method: staticLoad(Class<T> cls)");
        PropertiesLoader.staticLoad(Propertity2.class);
        Propertity2.print();
        System.out.println("---------------------------------");
    }

    /**
     * Method: staticLoad(Class<T> cls, String fileName)
     */
    @Test
    public void testStatiLoadForClsFileName() throws Exception {
        System.out.println(" Method: staticLoad(Class<T> cls, String fileName)");
        PropertiesLoader.staticLoad(Propertity4.class, "propertiesTest.properties");
        Propertity4.print();
        System.out.println("---------------------------------");
    }

    /**
     * Method: load(Class<T> cls, String fileName)
     */
    @Test
    public void testLoadForClsFileName() throws Exception {
        System.out.println(" Method: load(Class<T> cls, String fileName)");
        PropertiesLoader.load(Propertity1.class, "propertiesTest.properties").print();
        System.out.println("---------------------------------");
    }

    /**
     * Method: load(String filename)
     */
    @Test
    public void testLoadFilename() throws Exception {
        System.out.println(" Method: load(String filename)");
        for(Map.Entry entry : PropertiesLoader.load("propertiesTest.properties").getMap().entrySet() ) {
            System.out.printf("%s: %s ", entry.getKey(), entry.getValue());
        }
        System.out.println("\n---------------------------------");
    }
} 
