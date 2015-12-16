package com.syncano.library.data;

import com.syncano.library.Syncano;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.api.Response;
import com.syncano.library.callbacks.SyncanoCallback;
import com.syncano.library.choice.TraceStatus;

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
    private TraceStatus status;

    @SyncanoField(name = FIELD_DURATION, readOnly = true)
    private int duration;

    @SyncanoField(name = FIELD_RESULT, readOnly = true)
    private TraceResult result;

    @SyncanoField(name = FIELD_EXECUTED_AT, readOnly = true)
    private Date executedAt;

    private Integer codeBoxId = null;
    private Syncano syncano;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getCodeBoxId() {
        return codeBoxId;
    }

    public void setCodeBoxId(int id) {
        this.codeBoxId = id;
    }

    public TraceStatus getStatus() {
        return status;
    }

    public void setStatus(TraceStatus status) {
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

    private Syncano getSyncano() {
        if (syncano == null) {
            return Syncano.getInstance();
        }
        return syncano;
    }

    public Trace on(Syncano syncano) {
        this.syncano = syncano;
        return this;
    }

    public Response<Trace> fetch() {
        return getSyncano().getTrace(this).send();
    }

    public void fetch(SyncanoCallback<Trace> callback) {
        getSyncano().getTrace(this).sendAsync(callback);
    }

    public String getOutput() {
        if (result != null) {
            return result.stdout;
        }
        return null;
    }

    public String getErrorOutput() {
        if (result != null) {
            return result.stderr;
        }
        return null;
    }

    private static class TraceResult {
        @SyncanoField(name = FIELD_STDERR, readOnly = true)
        public String stderr;
        @SyncanoField(name = FIELD_STDOUT, readOnly = true)
        public String stdout;
    }
}
