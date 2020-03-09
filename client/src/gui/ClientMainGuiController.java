package gui;

import businesslogic.Client;
import customexceptions.MailImproperlyFormedException;
import gui.generic.CustomAlert;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Mail;
import model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClientMainGuiController
{


    @FXML
    private Button syncBtn;
    @FXML
    private Label welcomeLabel;
    @FXML
    private TextArea mailContent;
    @FXML
    private ListView mailsList;


    private Client client;
    private User user;

    public void setClient(Client client) { this.client = client; }
    public Client getClient() { return client; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    @FXML
    public void initialize()
    {
        this.mailContent.setWrapText(true);
    }

    @FXML
    private void handleSyncButtonClick(ActionEvent event)
    {
        //todo : gérer la sync
        //new CustomAlert(Alert.AlertType.INFORMATION, "La fonctionnalité n'a pas encore été implémentée...", "Patience...", this.syncBtn.getScene().getWindow()).showAndWait();
        this.refreshMails();
    }

    @FXML
    private void handleListViewClick(MouseEvent event)
    {
        try
        {
            if(this.mailsList.getSelectionModel().getSelectedItem() != null)
            {
                Mail mail = (Mail)this.mailsList.getSelectionModel().getSelectedItem();
                this.mailContent.setText(mail.getCorps());
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            new CustomAlert(Alert.AlertType.ERROR, "Impossible de récupérer le contenu de ce mail : " + e.getMessage(), "Erreur", this.syncBtn.getScene().getWindow()).showAndWait();
        }
    }


    public void refreshLabel()
    {
        this.welcomeLabel.setText("Bonjour " + this.user.getName() + " ! Bienvenue sur votre boîte de réception.");
    }

    public void refreshMails()
    {
        this.mailsList.setItems(null);
        try
        {
            List<Mail> mails = this.client.synchronisation();
            this.mailsList.setItems(FXCollections.observableList(mails));
            this.mailsList.refresh();
        }
        catch(MailImproperlyFormedException ex1)
        {
            ex1.printStackTrace();
            new CustomAlert(Alert.AlertType.ERROR, "La récupération des mails a échoué : " + ex1.getMessage(), "Erreur", this.mailsList.getScene().getWindow()).showAndWait();
        }
        catch(IOException ex2)
        {
            ex2.printStackTrace();
            new CustomAlert(Alert.AlertType.ERROR, "Erreur d'entrée sortie : " + ex2.getMessage(), "Erreur", this.mailsList.getScene().getWindow()).showAndWait();
        }
        catch(Exception ex3)
        {
            ex3.printStackTrace();
            new CustomAlert(Alert.AlertType.ERROR, "Erreur : " + ex3.getMessage(), "Erreur", this.mailsList.getScene().getWindow()).showAndWait();
        }
    }
}
