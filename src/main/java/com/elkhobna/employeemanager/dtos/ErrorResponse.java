package com.elkhobna.employeemanager.dtos;

public class ErrorResponse {

    private String message;
    private String timestamp;
    private String path;

    public ErrorResponse(String message, String path) {
        this.message = message;
        this.timestamp = java.time.Instant.now().toString();
        this.path = path;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
