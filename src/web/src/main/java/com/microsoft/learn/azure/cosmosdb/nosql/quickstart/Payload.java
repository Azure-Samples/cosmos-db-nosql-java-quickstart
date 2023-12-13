package com.microsoft.learn.azure.cosmosdb.nosql.quickstart;

public class Payload {

    private String message;

    public Payload() {        
    }

    public Payload(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
