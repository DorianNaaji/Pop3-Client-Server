package gui;

import customexceptions.ClosingConnexionException;
import gui.generic.CustomAlert;
import gui.generic.Window;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;

public class ClientMainGui extends Window
{
    public ClientMainGui(Stage parent) throws IOException
    {
        super(parent, "Client de messagerie électronique", "ClientMainGui.fxml", 800, 600, Modality.NONE);
        this.setOnHiding(event ->
        {
            event.consume();
        });
        this.setOnCloseRequest(event ->
        {
            event.consume();
            try
            {
                this.getController().getClient().Quit();
                Platform.exit();
            }
            catch(IOException ex)
            {
                ex.printStackTrace();
                CustomAlert alert = new CustomAlert(Alert.AlertType.ERROR, "Une erreur d'entrée/sortie est survenue : " + ex.getMessage(), "Erreur", this);
                alert.showAndWait();
            }
            catch(ClosingConnexionException ex)
            {
                ex.printStackTrace();
                CustomAlert alert = new CustomAlert(Alert.AlertType.ERROR, ex.getMessage(), "Erreur", this);
                alert.showAndWait();
            }

        });
    }

    public ClientMainGuiController getController()
    {
        return (ClientMainGuiController)this.controller;
    }
}
