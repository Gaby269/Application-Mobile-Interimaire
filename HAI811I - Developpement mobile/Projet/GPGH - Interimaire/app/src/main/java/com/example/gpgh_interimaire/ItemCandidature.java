package com.example.gpgh_interimaire;

public class ItemCandidature {
    String firstName, lastName, descriptionCandidature, complementCandidature, CV;

    public ItemCandidature(String firstName, String lastName, String descriptionCandidature, String complementCandidature, String CV) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.descriptionCandidature = descriptionCandidature;
        this.complementCandidature = complementCandidature;
        this.CV = CV;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDescriptionCandidature() {
        return descriptionCandidature;
    }

    public void setDescriptionCandidature(String descriptionCandidature) {
        this.descriptionCandidature = descriptionCandidature;
    }

    public String getComplementCandidature() {
        return complementCandidature;
    }

    public void setComplementCandidature(String complementCandidature) {
        this.complementCandidature = complementCandidature;
    }

    public String getCV() {
        return CV;
    }

    public void setCV(String CV) {
        this.CV = CV;
    }
}