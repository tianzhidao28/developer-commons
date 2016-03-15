package cn.jpush.commons.utils.properties;

/**
 *
 * Created by leolin on 16/3/11.
 */
@Config("propertiesTest.properties")
public class Propertity1 {
    @Key
    public int intField;
    @Key
    public String stringField;
    @Key
    public boolean booleanField;
    public void print() {
        System.out.printf("intField: %d  stringField: %s  booleanField: %s\n", intField, stringField, booleanField);
    }
}