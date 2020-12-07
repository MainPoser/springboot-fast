package cn.com.datu.springboot.arcsoft.model.enums;

/**
 * @author tianyao
 */

public enum IrLiveness {
    UNKNOWN(-1,"未知"),
    NOT_LIVENESS(0,"非活体"),
    LIVENESS(1,"活体"),
    OVER_FACE(1,"超出人脸");
    private int key;
    private String value;

    IrLiveness(int key, String value) {
        this.key = key;
        this.value = value;
    }
    public static String getValueByKey(int key){
        String value = "";
        for (IrLiveness irLiveness : IrLiveness.values()) {
            if (key == irLiveness.key){
                value = irLiveness.value;
                break;
            }
        }
        return value;
    }
}
