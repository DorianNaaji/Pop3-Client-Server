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

import java.io.IOException;

public class ClientMainGuiController
{
    //todo : 1. Gérer l'authentification (nouvelle window),
    //       2. Gérer  la récupération des mails, l'enregistrement sous forme de fichiers et l'affichage,
    //       !! Faire un main respectant la prog Gertosio !!

    @FXML
    private Button syncBtn;
    private Client client;

    public void setClient(Client client)
    {
        this.client = client;
    }

    public Client getClient()
    {
        return client;
    }

//    @FXML
//    public void initialize()
//    {
//
//    }

    @FXML
    private void handleSyncButtonClick(ActionEvent event)
    {
        CustomAlert alert = new CustomAlert(Alert.AlertType.INFORMATION, "La fonctionnalité n'a pas encore été implémentée....", "Patience...", this.syncBtn.getScene().getWindow());
    }

}
