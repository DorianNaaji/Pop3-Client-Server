package gui;

import gui.generic.Window;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientAuthentificationDialog extends Window
{

    public ClientAuthentificationDialog(ClientMainGui parent) throws IOException
    {
        super(parent, "Connexion", "ClientConnexionDialog.fxml", 240, 200, Modality.APPLICATION_MODAL);

    }
}
