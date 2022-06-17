package com.ar.patient.fcm;

public class MessageEvent {
    public String message;

    public MessageEvent(String action, String message) {
        this.message = message;
    }

    public MessageEvent(String message) {
        this.message = message;
    }
}
