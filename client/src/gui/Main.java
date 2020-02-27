package gui;

import javafx.application.Application;
import javafx.stage.Stage;

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