package com.robot.baseappapi;

import org.litepal.crud.LitePalSupport;

public class TestDb extends LitePalSupport {
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
