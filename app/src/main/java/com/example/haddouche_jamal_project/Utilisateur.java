package com.example.haddouche_jamal_project;

public class Utilisateur {
    private String name;
    private String prenom;
    private String email;
    private String phone;
    private String password;
    private String filiere;

    public Utilisateur(String name, String prenom, String email, String phone, String password) {
        this.name = name;
        this.prenom = prenom;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    public Utilisateur(String name, String prenom, String email, String phone, String password, String filiere) {
        this.name = name;
        this.prenom = prenom;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.filiere = filiere;
    }

    public Utilisateur() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFiliere() {
        return filiere;
    }

    public void setFiliere(String filiere) {
        this.filiere = filiere;
    }
}
