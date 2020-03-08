package gui;

import businesslogic.Client;
import gui.generic.CustomAlert;
import gui.generic.Window;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientAuthentificationDialog extends Window
{

    public ClientAuthentificationDialog(ClientMainGui parent, Client client) throws IOException
    {
        super(parent, "Authentification", "ClientAuthentificationDialog.fxml", 240, 200, Modality.APPLICATION_MODAL);
        this.getController().setClient(client);
        this.setOnCloseRequest(event ->
        {
            event.consume();
            Platform.exit();
        });
        this.setOnHiding(event ->
        {
            event.consume();
            parent.getController().setUser((this.getController().getClient().getUser()));
            parent.getController().refreshLabel();
            parent.getController().refreshMails();
        });
    }

    public ClientAuthentificationDialogController getController() { return (ClientAuthentificationDialogController) this.controller;}
}
