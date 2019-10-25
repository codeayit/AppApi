package com.robot.baseappapi;

import org.litepal.crud.LitePalSupport;

public class TestDb extends LitePalSupport {
    public TestDb(int status, int type) {
        this.status = status;
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private int status;
    private int type;
}
