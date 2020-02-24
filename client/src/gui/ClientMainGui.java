package gui;

import gui.generic.Window;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;

public class ClientMainGui extends Window
{
    public ClientMainGui(Stage parent) throws IOException
    {
        super(parent, "Client de messagerie électronique", "ClientMainGui.fxml", 800, 600, Modality.NONE);
    }

    public ClientMainGuiController getController()
    {
        return (ClientMainGuiController)this.controller;
    }
}
