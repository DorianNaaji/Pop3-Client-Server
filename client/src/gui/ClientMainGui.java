package gui;

import gui.generic.Window;
import javafx.application.Platform;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;

public class ClientMainGui extends Window
{
    public ClientMainGui(Stage parent) throws IOException
    {
        super(parent, "Client de messagerie électronique", "ClientMainGui.fxml", 960, 700, Modality.NONE);
        this.setOnHiding(event ->
        {
            event.consume();
            try
            {
                if(this.getController().getClient() != null)
                {
                    if(!this.getController().getClient().getSocket().isClosed())
                    {
                        this.getController().getClient().quit();
                    }
                }
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
            finally
            {
                Platform.exit();
            }
        });
    }

    public ClientMainGuiController getController()
    {
        return (ClientMainGuiController)this.controller;
    }
}
