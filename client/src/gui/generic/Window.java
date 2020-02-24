package gui.generic;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public abstract class Window extends Stage
{
    /**
     * Controller type.
     */
    protected Object controller;

    public Window(Stage parent, String title, String fxmlResource, int width, int height, Modality modality) throws IOException
    {
        super();
        this.setTitle(title);
        this.initOwner(parent);
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource(fxmlResource));
        Parent root = loader.load();
        this.controller = loader.getController();
        this.setScene(new Scene(root, width, height));
        this.setResizable(false);
        this.initModality(modality);
        this.getIcons().add(new Image("file:resources/icon.png"));
    }
}
