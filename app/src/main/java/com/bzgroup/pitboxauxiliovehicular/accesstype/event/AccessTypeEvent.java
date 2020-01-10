package com.bzgroup.pitboxauxiliovehicular.accesstype.event;

public class AccessTypeEvent {
    public final static int ACCESS_TYPE_SUCCESS = 0;
    public final static int ACCESS_TYPE_EMPTY = 1;
    public final static int ACCESS_TYPE_ERROR = 2;

    private int eventType;
    private String errorMessage;

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
