package gui;

import gui.generic.CustomAlert;
import gui.generic.Window;
import javafx.application.Platform;
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
            this.prematureClosing = true;
            Platform.exit();
        });
        this.setOnHiding(event ->
        {
            if(!this.prematureClosing)
            {
                event.consume();
                parent.getController().setClient(this.getController().getClientToPassToMainGui());
                try
                {
                    Stage clientAuthDialog = new ClientAuthentificationDialog(parent, parent.getController().getClient());
                    clientAuthDialog.show();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                    new CustomAlert(Alert.AlertType.ERROR, "Erreur lors de l'ouverture de la fenêtre d'authentification : " + e.getMessage(), "Erreur", this).showAndWait();
                }
            }
        });
    }

    private boolean prematureClosing = false;

    public ClientConnexionDialogController getController()
    {
        return (ClientConnexionDialogController)this.controller;
    }
}
