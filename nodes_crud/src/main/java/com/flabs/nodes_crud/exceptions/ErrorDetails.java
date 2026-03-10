package com.flabs.nodes_crud.exceptions;

import java.time.LocalDateTime;

public class ErrorDetails {
    private LocalDateTime timestamp;
    private String message;
    private String details;

    public ErrorDetails(LocalDateTime timestamps, String message, String details) {
        super();
        this.timestamp = timestamps;
        this.message = message;
        this.details = details;
    }

    public LocalDateTime getTimestamps() {
        return this.timestamp;
    }

    public void setTimestamp(LocalDateTime date) {
        this.timestamp = date;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String mess) {
        this.message = mess;
    }

    public String getDetails() {
        return this.details;
    }

    public void setDetails(String det) {
        this.details = det;
    }

}