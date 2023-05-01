package com.example.gpgh_interimaire;

public class ItemOffre {
    String titre, type, nameEntreprise,  descriptionOffre, rue, complementRue, codePostal, prix;
    int image;

    public ItemOffre(String titre, String type, String prix, String nameEntreprise, int image, String descriptionOffre, String rue, String complementRue, String codePostal) {
        this.titre = titre;
        this.nameEntreprise = nameEntreprise;
        this.type = type;
        this.prix = prix;
        this.image = image;
        this.descriptionOffre = descriptionOffre;
        this.rue = rue;
        this.complementRue = complementRue;
        this.codePostal = codePostal;
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


    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}