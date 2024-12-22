package com.linhhn.zync.util.exception;

import java.time.LocalDateTime;

public class HttpErrorInfo {
    private LocalDateTime timestamp;
    private String path;
    private String message;

    public HttpErrorInfo(String path, String message) {
        this.timestamp = LocalDateTime.now();
        this.path = path;
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
