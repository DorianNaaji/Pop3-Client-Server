package gui;

import gui.generic.CustomAlert;
import gui.generic.Window;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientConnexionDialog extends Window
{
    public ClientConnexionDialog(ClientMainGui parent) throws IOException
    {
        super(parent, "Connexion", "ClientConnexionDialog.fxml", 240, 200, Modality.APPLICATION_MODAL);
        this.setOnCloseRequest(event ->
        {
            event.consume();
            CustomAlert alert = new CustomAlert(Alert.AlertType.WARNING, "Veuillez cliquer sur le bouton connexion.", "Connexion nécessaire", this);
            alert.showAndWait();
        });
        this.setOnHiding(event ->
        {
            event.consume();
            parent.getController().setClient(this.getController().getClientToPassToMainGui());
        });
    }

    public ClientConnexionDialogController getController()
    {
        return (ClientConnexionDialogController)this.controller;
    }
}
