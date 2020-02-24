package gui;

import gui.generic.Window;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientConnexionDialog extends Window
{
    public ClientConnexionDialog(ClientMainGui parent) throws IOException
    {
        super(parent, "Connexion", "ClientConnexionDialog.fxml", 240, 200, Modality.APPLICATION_MODAL);
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
