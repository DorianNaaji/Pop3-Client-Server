package gui;

import businesslogic.Client;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Mail;

import java.awt.event.ActionEvent;
import java.io.File;

/* HELP  :*/
/* "Could not find or load "Main" : https://stackoverflow.com/questions/10654120/error-could-not-find-or-load-main-class-in-intellij-ide */
/* "Create a JavaFX Project" : https://www.jetbrains.com/help/idea/javafx.html */

public class Main extends Application
{

    @Override
    public void start(final Stage primaryStage) throws Exception
    {
        ClientMainGui mainGui = new ClientMainGui(primaryStage);
        mainGui.show();

        Stage connexionDialog = new ClientConnexionDialog(mainGui);
        connexionDialog.show();
    }


    public static void main(String[] args)
    {
        launch(args);
    }
}