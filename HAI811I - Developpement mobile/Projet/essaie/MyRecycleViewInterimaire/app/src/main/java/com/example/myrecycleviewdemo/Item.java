package com.example.myrecycleviewdemo;

import java.util.List;

public class Item {
    String titre;
    String entreprise;
    int image;
    String petiteDescription;
    String rue, complementRue, codePostal;
    int parking;
    boolean teletravail,ticket;

    public Item(String titre, String entreprise, int image, String petiteDescription, String rue, String complementRue, String codePostal, int parking, boolean teletravail, boolean ticket) {
        this.titre = titre;
        this.entreprise = entreprise;
        this.image = image;
        this.petiteDescription = petiteDescription;
        this.rue = rue;
        this.complementRue = complementRue;
        this.codePostal = codePostal;
        this.parking = parking;
        this.teletravail = teletravail;
        this.ticket = ticket;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getEntreprise() {
        return entreprise;
    }

    public void setEntreprise(String entreprise) {
        this.entreprise = entreprise;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getPetiteDescription() {
        return petiteDescription;
    }

    public void setPetiteDescription(String petiteDescription) {
        this.petiteDescription = petiteDescription;
    }

    public String getRue() {
        return rue;
    }

    public void setRue(String rue) {
        this.rue = rue;
    }

    public String getComplementRue() {
        return complementRue;
    }

    public void setComplementRue(String complementRue) {
        this.complementRue = complementRue;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public int getParking() {
        return parking;
    }

    public void setParking(int parking) {
        this.parking = parking;
    }

    public boolean getTeletravail() {
        return teletravail;
    }

    public void setTeletravail(boolean teletravail) {
        this.teletravail = teletravail;
    }

    public boolean getTicket() {
        return ticket;
    }

    public void setTicket(boolean ticket) {
        this.ticket = ticket;
    }
}
