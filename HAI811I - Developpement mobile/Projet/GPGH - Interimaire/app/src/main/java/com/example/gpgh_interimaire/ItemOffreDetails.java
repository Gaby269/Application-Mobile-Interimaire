package com.example.gpgh_interimaire;

public class ItemOffreDetails {
    String titre, type, nameEntreprise,  descriptionOffre, rue, complementRue, codePostal, prix;
    int parking;
    boolean ticket, teletravail;
    int image;

    public ItemOffreDetails(String titre, String type, String prix, String nameEntreprise, int image, String descriptionOffre, String rue, String complementRue, String codePostal, int parking, boolean ticket, boolean teletravail) {
        this.titre = titre;
        this.nameEntreprise = nameEntreprise;
        this.type = type;
        this.prix = prix;
        this.image = image;
        this.descriptionOffre = descriptionOffre;
        this.rue = rue;
        this.complementRue = complementRue;
        this.codePostal = codePostal;
        this.parking = parking;
        this.ticket = ticket;
        this.teletravail = teletravail;
    }

    // ajouter des fonctionnalités supplémentaires ici, si nécessaire

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrix() {
        return prix;
    }

    public void setPrix(String prix) {
        this.prix = prix;
    }

    public String getDescriptionOffre() {
        return descriptionOffre;
    }

    public void setDescriptionOffre(String descriptionOffre) {
        this.descriptionOffre = descriptionOffre;
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