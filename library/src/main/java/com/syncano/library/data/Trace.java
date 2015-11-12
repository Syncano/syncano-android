package com.syncano.library.data;

import com.syncano.library.annotation.SyncanoField;

import java.util.Date;

public class Trace {

    public static final String FIELD_ID = "id";
    public static final String FIELD_STATUS = "status";
    public static final String FIELD_DURATION = "duration";
    public static final String FIELD_RESULT = "result";
    public static final String FIELD_EXECUTED_AT = "executed_at";
    public static final String FIELD_STDERR = "stderr";
    public static final String FIELD_STDOUT = "stdout";

    @SyncanoField(name = FIELD_ID, readOnly = true)
    private int id;

    @SyncanoField(name = FIELD_STATUS, readOnly = true)
    private String status;

    @SyncanoField(name = FIELD_DURATION, readOnly = true)
    private int duration;

    @SyncanoField(name = FIELD_RESULT, readOnly = true)
    private TraceResult result;

    @SyncanoField(name = FIELD_EXECUTED_AT, readOnly = true)
    private Date executedAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public TraceResult getResult() {
        return result;
    }

    public void setResult(TraceResult result) {
        this.result = result;
    }

    public Date getExecutedAt() {
        return executedAt;
    }

    public void setExecutedAt(Date executedAt) {
        this.executedAt = executedAt;
    }

    public static class TraceResult {
        @SyncanoField(name = FIELD_STDERR, readOnly = true)
        public String stderr;
        @SyncanoField(name = FIELD_STDOUT, readOnly = true)
        public String stdout;
    }
}
