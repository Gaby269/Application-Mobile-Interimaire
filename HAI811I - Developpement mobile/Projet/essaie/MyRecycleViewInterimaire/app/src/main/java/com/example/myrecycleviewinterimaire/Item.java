package com.example.myrecycleviewinterimaire;


public class Item {
    String titre;
    String nameEntreprise,  petiteDescription, rue, complementRue, codePostal;
    int parking;
    boolean ticket, teletravail;
    int image;

    public Item(String titre, String nameEntreprise, int image, String petiteDescription, String rue, String complementRue, String codePostal, int parking, boolean ticket, boolean teletravail) {
        this.titre = titre;
        this.nameEntreprise = nameEntreprise;
        this.image = image;
        this.petiteDescription = petiteDescription;
        this.rue = rue;
        this.complementRue = complementRue;
        this.codePostal = codePostal;
        this.parking = parking;
        this.ticket = ticket;
        this.teletravail = teletravail;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getNameEntreprise() {
        return nameEntreprise;
    }

    public void setNameEntreprise(String nameEntreprise) {
        this.nameEntreprise = nameEntreprise;
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

    public boolean isTicket() {
        return ticket;
    }

    public void setTicket(boolean ticket) {
        this.ticket = ticket;
    }

    public boolean isTeletravail() {
        return teletravail;
    }

    public void setTeletravail(boolean teletravail) {
        this.teletravail = teletravail;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}

