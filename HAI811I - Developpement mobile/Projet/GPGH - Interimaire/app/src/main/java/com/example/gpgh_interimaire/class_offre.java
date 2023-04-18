package com.example.gpgh_interimaire;

public class class_offre {
    private String title;
    private String petiteDescription;
    private String longueDescription;
    private int photoId;
    private String nomEntreprise;
    private String rueAdresse;
    private String complementAdresse;
    private String codePostale;
    private String ville;
    // ajouter des fonctionnalités supplémentaires ici, si nécessaire

    public class_offre(String title, String petiteDescription, String longueDescription, int photoId, String nomEntreprise, String rueAdresse, String complementAdresse, String codePostale, String ville) {
        this.title = title;
        this.petiteDescription = petiteDescription;
        this.longueDescription = longueDescription;
        this.photoId = photoId;
        this.nomEntreprise = nomEntreprise;
        this.rueAdresse = rueAdresse;
        this.complementAdresse = complementAdresse;
        this.codePostale = codePostale;
        this.ville = ville;
    }

    // Ajouter les getters et setters pour chaque attribut ici

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getPetiteDescription() {
        return petiteDescription;
    }
    public void setPetiteDescription(String petiteDescription) {
        this.petiteDescription = petiteDescription;
    }

    public String getLongueDescription() {
        return longueDescription;
    }
    public void setLongueDescription(String longueDescription) {
        this.longueDescription = longueDescription;
    }

    public int getPhotoId() {
        return photoId;
    }
    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    public String getNomEntreprise() {
        return nomEntreprise;
    }
    public void setNomEntreprise(String nomEntreprise) {
        this.nomEntreprise = nomEntreprise;
    }

    public String getRueAdresse() {
        return rueAdresse;
    }
    public void setRueAdresse(String rueAdresse) {
        this.rueAdresse = rueAdresse;
    }

    public String getComplementAdresse() {
        return complementAdresse;
    }
    public void setComplementAdresse(String complementAdresse) {
        this.complementAdresse = complementAdresse;
    }

    public String getCodePostale() {
        return codePostale;
    }
    public void setCodePostale(String codePostale) {
        this.codePostale = codePostale;
    }

    public String getVille() {
        return ville;
    }
    public void setVille(String ville) {
        this.ville = ville;
    }
}