package com.example.gpgh_interimaire;

import java.util.List;

public class Conversation {
    private String dernierMessage;
    private List<String> messages;
    private List<String> participants;
    private List<String> nonLu;

    public Conversation() {}

    // Getters et setters

    public String getDernierMessage() {
        return dernierMessage;
    }

    public void setDernierMessage(String dernierMessage) {
        this.dernierMessage = dernierMessage;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    public List<String> getNonLu() {
        return nonLu;
    }

    public void setNonLu(List<String> nonLu) {
        this.nonLu = nonLu;
    }
}
