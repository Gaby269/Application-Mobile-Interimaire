package com.example.gpgh_interimaire;

public class ItemOffre {
    String id_offre, titre, type, nameEntreprise,  date_debut, date_fin, codePostal, prix;
    int image;

    public ItemOffre(String id_offre, String titre, String type, String prix, String nameEntreprise, String date_debut, String date_fin, String codePostal) {
        this.id_offre = id_offre;
        this.titre = titre;
        this.nameEntreprise = nameEntreprise;
        this.type = type;
        this.prix = prix;
        this.date_debut = date_debut;
        this.date_fin = date_fin;
        this.codePostal = codePostal;
    }

    // ajouter des fonctionnalités supplémentaires ici, si nécessaire


    public String getId_offre() {
        return id_offre;
    }

    public void setId_offre(String id_offre) {
        this.id_offre = id_offre;
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

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getDate_debut() {
        return date_debut;
    }

    public String getDate_fin() {
        return date_fin;
    }

    public void setDate_fin(String date_fin) {
        this.date_fin = date_fin;
    }

    public void setDate_debut(String date_debut) {
        this.date_debut = date_debut;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}