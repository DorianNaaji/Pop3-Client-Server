package gui;

import businesslogic.Client;
import gui.generic.CustomAlert;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.User;

import java.io.IOException;

public class ClientMainGuiController
{
    // todo
    //       2. Gérer  la récupération des mails, l'enregistrement sous forme de fichiers et l'affichage,
    //       !! Faire un main respectant la prog Gertosio !!
    // TODO : lancer une synchronisation dès que connectée (cf diagramme d'état, on envoie un stat direct après l'autorisation (authentification)

    // todo
    //  27.02.2019
    //  1. Lire et récupérer les mails grâce à la commande implémentée par Myriam (nb !! en mémoire ou sur disque ?!)
    //  2. les afficher dès que connecté
    //  3. relancer ce process à chaque fois qu'un clic sur le bouton synchronisation est fait.

    @FXML
    private Button syncBtn;
    @FXML
    private Label welcomeLabel;

    private Client client;
    private User user;

    public void setClient(Client client) { this.client = client; }
    public Client getClient() { return client; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    @FXML
    private void handleSyncButtonClick(ActionEvent event)
    {
        CustomAlert alert = new CustomAlert(Alert.AlertType.INFORMATION, "La fonctionnalité n'a pas encore été implémentée....", "Patience...", this.syncBtn.getScene().getWindow());
    }

    public void refreshLabel()
    {
        this.welcomeLabel.setText("Bonjour " + this.user.getName() + " ! Bienvenue sur votre boîte de réception.");
    }

}
