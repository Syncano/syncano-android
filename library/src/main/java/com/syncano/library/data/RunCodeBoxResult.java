package com.syncano.library.data;


import com.syncano.library.annotation.SyncanoField;

import java.util.Date;

public class RunCodeBoxResult {

    @SyncanoField(name = "status", readOnly = true)
    private String status;

    @SyncanoField(name = "duration", readOnly = true)
    private int duration;

    @SyncanoField(name = "result", readOnly = true)
    private String result;

    @SyncanoField(name = "executed_at", readOnly = true)
    private Date executedAt;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Date getExecutedAt() {
        return executedAt;
    }

    public void setExecutedAt(Date executedAt) {
        this.executedAt = executedAt;
    }
}
