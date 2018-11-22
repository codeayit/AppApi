package com.robot.baseapi.db;

/**
 * Created by lny on 2018/4/13.
 * 查询排序
 */

public enum Order {
    ASC("asc"), DESC("desc");
    private String value;

    private Order(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return getValue();
    }
}
