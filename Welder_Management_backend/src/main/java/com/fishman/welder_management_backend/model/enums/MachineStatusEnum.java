package com.fishman.welder_management_backend.model.enums;

/**
 * 队伍状态枚举
 *
 */
public enum MachineStatusEnum {

    PUBLIC(0, "空闲"),
    PRIVATE(1, "使用"),
    SECRET(2, "维修");

    private int value;

    private String text;

    public static MachineStatusEnum getEnumByValue(Integer value) {
        if (value == null) {
            return null;
        }
        MachineStatusEnum[] values = MachineStatusEnum.values();
        for (MachineStatusEnum machineStatusEnum : values) {
            if (machineStatusEnum.getValue() == value) {
                return machineStatusEnum;
            }
        }
        return null;
    }

    MachineStatusEnum(int value, String text) {
        this.value = value;
        this.text = text;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
