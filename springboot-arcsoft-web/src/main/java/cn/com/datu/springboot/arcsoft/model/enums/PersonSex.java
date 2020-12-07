package cn.com.datu.springboot.arcsoft.model.enums;

import lombok.Data;
import org.omg.CORBA.UNKNOWN;

/**
 * @author tianyao
 */
public enum PersonSex {
    UNKNOWN(-1,"未知"),
    MAN(0,"男性"),
    FEMALE(1,"女性");
    private int key;
    private String value;

    PersonSex(int key, String value) {
        this.key = key;
        this.value = value;
    }
    public static String getValueByKey(int key){
        String value = "";
        for (PersonSex personSex : PersonSex.values()) {
            if (key == personSex.key){
                value = personSex.value;
                break;
            }
        }
        return value;
    }
}
