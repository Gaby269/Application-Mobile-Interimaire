package com.example.gpgh_interimaire;

public class ItemCandidature {
    String id_candidature, id_user, firstName, lastName, descriptionCandidature, complementCandidature, CV;

    public ItemCandidature(String id_candidature, String id_user, String firstName, String lastName, String descriptionCandidature, String complementCandidature, String CV) {
        this.id_candidature = id_candidature;
        this.id_user = id_user;
        this.firstName = firstName;
        this.lastName = lastName;
        this.descriptionCandidature = descriptionCandidature;
        this.complementCandidature = complementCandidature;
        this.CV = CV;
    }

    public String getId_candidature() {return id_candidature;}

    public void setId_candidature(String id_candidature) {this.id_candidature = id_candidature;}

    public String getId_user() {return id_user;}

    public void setId_user(String id_user) {this.id_user = id_user;}

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

    public void setDescriptionCandidature(String descriptionCandidature) {this.descriptionCandidature = descriptionCandidature;}

    public String getComplementCandidature() {
        return complementCandidature;
    }

    public void setComplementCandidature(String complementCandidature) {this.complementCandidature = complementCandidature;}

    public String getCV() {
        return CV;
    }

    public void setCV(String CV) {
        this.CV = CV;
    }
}