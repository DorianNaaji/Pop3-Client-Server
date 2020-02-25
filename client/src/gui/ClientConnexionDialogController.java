package gui;

import businesslogic.Client;
import gui.generic.CustomAlert;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


public class ClientConnexionDialogController
{
    private String ipv4Regex = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
    private String tcpPortRegex = "^([0-9]{1,4}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$";

    @FXML
    private TextField ipTextbox;
    @FXML
    private TextField portTextbox;
    @FXML
    private Button btnConnect;
    @FXML
    private ProgressIndicator progressSpin;

    private Client clientToPassToMainGui = null;

    public Client getClientToPassToMainGui()
    {
        return this.clientToPassToMainGui;
    }

    @FXML
    private void initialize()
    {
        System.out.println("Client Dialog started");
        this.progressSpin.setVisible(false);

    }

    @FXML
    public boolean handleExitEvent()
    {
        return this.checkTextBoxes();
    }

    @FXML
    private void handleConnectButtonClick()
    {
        if(this.checkTextBoxes())
        {
            this.progressSpin.setVisible(true);
            new Thread(() ->
            {
                boolean exceptionOccured = false;
                try
                {
                    this.clientToPassToMainGui = new Client(this.ipTextbox.getText(), Integer.parseInt(this.portTextbox.getText()));
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    exceptionOccured = true;
                    Platform.runLater(() ->
                    {
                        this.progressSpin.setVisible(false);
                        CustomAlert alert = new CustomAlert(Alert.AlertType.ERROR, "Impossible d'établir la connexion avec le serveur : \n" + e, "Erreur", this.ipTextbox.getScene().getWindow());
                        alert.showAndWait();
                    });
                }
                if(!exceptionOccured)
                {
                    Platform.runLater(() ->
                    {
                        System.out.println("Connecté au serveur " + this.ipTextbox.getText() + " (Port : " + this.portTextbox.getText() + ").");
                        ((Stage)this.ipTextbox.getScene().getWindow()).close();
                    });
                }
            }).start();
        }
    }


    private boolean checkTextBoxes()
    {
        boolean ipv4Correct = this.ipTextbox.getText().matches(this.ipv4Regex);
        boolean tcpPortcorrect = this.portTextbox.getText().matches(this.tcpPortRegex);
        if(!ipv4Correct && !tcpPortcorrect)
        {
            CustomAlert alert = new CustomAlert(Alert.AlertType.WARNING, "IP et port incorrects. Veuillez réessayer.", "Mauvaise saisie", this.ipTextbox.getScene().getWindow());
            alert.showAndWait();
            return false;
        }
        else if(!ipv4Correct)
        {
            CustomAlert alert = new CustomAlert(Alert.AlertType.WARNING, "IP incorrecte. Veuillez réessayer.", "Mauvaise saisie", this.ipTextbox.getScene().getWindow());
            alert.showAndWait();
            return false;
        }
        else if(!tcpPortcorrect)
        {
            CustomAlert alert = new CustomAlert(Alert.AlertType.WARNING, "Port incorrect. Veuillez réessayer.", "Mauvaise saisie", this.ipTextbox.getScene().getWindow());
            alert.showAndWait();
            return false;
        }
        return true;
    }
}
