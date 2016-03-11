package cn.jpush.commons.utils.properties;

/**
 * Created by leolin on 16/3/11.
 */
@Config("propertiesTest.properties")
public class Propertity2 {
    @Key
    public static int intField;
    @Key
    public static String stringField;
    @Key
    public static boolean booleanField;

    public static void print() {
//        System.out.printf("intField: %d  stringField: %s  booleanField: %s\n", intField, stringField, booleanField);
    }
}
