package model;

import java.util.List;

public class Mail {

    private String emetteur;
    private String destinataire;
    private String date;
    private String sujet;
    private String corps;
    private String mime;

    public Mail(){}

    public Mail(String emetteur, String destinataire, String date, String sujet, String corps, String mime)
    {
        this.emetteur = emetteur;
        this.destinataire = destinataire;
        this.date = date;
        this.sujet = sujet;
        this.corps = corps;
        this.mime = mime;
    }

    public String getEmetteur() {
        return emetteur;
    }

    public void setEmetteur(String emetteur) {
        this.emetteur = emetteur;
    }

    public String getDestinataire() {
        return destinataire;
    }

    public void setDestinataire(String destinataire) {
        this.destinataire = destinataire;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSujet() {
        return sujet;
    }

    public void setSujet(String sujet) {
        this.sujet = sujet;
    }

    public String getCorps() {
        return corps;
    }

    public void setCorps(String corps) {
        this.corps = corps;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

}
