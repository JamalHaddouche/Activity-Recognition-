package com.example.haddouche_jamal_project;

import java.util.Date;

public class Localisation {
    private Date date;
    private String pays;
    private String ville;
    private String adresse;
    private double alltitude;
    private String idUser;

    public Localisation(Date date, String pays, String ville, String adresse, double alltitude) {
        this.date = date;
        this.pays = pays;
        this.ville = ville;
        this.adresse = adresse;
        this.alltitude = alltitude;
    }
    public Localisation(Date date, String pays, String ville, String adresse, double alltitude, String idUser) {
        this.date = date;
        this.pays = pays;
        this.ville = ville;
        this.adresse = adresse;
        this.alltitude = alltitude;
        this.idUser = idUser;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public double getAlltitude() {
        return alltitude;
    }

    public void setAlltitude(double alltitude) {
        this.alltitude = alltitude;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }
}
