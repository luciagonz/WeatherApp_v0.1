package com.appclima.appclimanavigation.model;

public class Chat {

    private String messageDetected; // message displayed
    private Integer transmitter; // 0 for voice assistant, 1 for user

    public Chat(String messageDetected, Integer transmitter) {
        this.messageDetected = messageDetected;
        this.transmitter = transmitter;
    }

    public String getMessageDetected() {
        return messageDetected;
    }

    @Override
    public String toString() {
        return "Chat{" +
                "messageDetected='" + messageDetected + '\'' +
                ", transmitter=" + transmitter +
                '}';
    }

    public void setMessageDetected(String messageDetected) {
        this.messageDetected = messageDetected;
    }

    public Integer getTransmitter() {
        return transmitter;
    }

    public void setTransmitter(Integer transmitter) {
        this.transmitter = transmitter;
    }

}
