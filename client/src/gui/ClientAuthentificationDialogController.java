package gui;

import businesslogic.Client;
import gui.generic.CustomAlert;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.User;


public class ClientAuthentificationDialogController
{

    @FXML
    private PasswordField passwordTextbox;

    @FXML
    private TextField usernameTextbox;

    @FXML
    private Button authentificationBtn;

    @FXML
    private ProgressIndicator progressSpin;

    private int connexionAttemptsCount = 0;

    private Client client = null;

    public void setClient(Client c) { this.client = c; }

    public Client getClient() { return this.client; }

    private boolean authentified = false;

    public boolean getAuthentified() { return this.authentified; }

    @FXML
    private void initialize()
    {
        this.progressSpin.setVisible(false);
    }

    @FXML
    private void handleAuthentificationButtonClick(ActionEvent event)
    {
        if(this.checkTextBoxes())
        {

                this.progressSpin.setVisible(true);
                User user = new User(this.usernameTextbox.getText(), this.passwordTextbox.getText());
                this.client.setUser(user);
                new Thread(() ->
                {
                    boolean exceptionOccured = false;
                    try
                    {
                        if (this.client.apop())
                        {
                            Platform.runLater(() ->
                            {
                                System.out.println(user.getName() + " est authentifié avec le serveur.");
                                this.authentified = true;
                                ((Stage) this.usernameTextbox.getScene().getWindow()).close();
                            });
                        }
                        else
                        {
                            Platform.runLater(() ->
                            {
                                CustomAlert alert = new CustomAlert(Alert.AlertType.ERROR, "L'authentification a échoué côté serveur. Veuillez vérifier vos identifiants ou réessayer.", "Échec d'authentification", this.usernameTextbox.getScene().getWindow());
                                alert.showAndWait();
                            });
                        }
                    }
                    catch (Exception e)
                    {
                        exceptionOccured = true;
                        e.printStackTrace();
                        Platform.runLater(() ->
                        {
                            CustomAlert alert = new CustomAlert(Alert.AlertType.ERROR, "L'authentification a échoué côté client : " + e.getMessage(), "Échec d'authentification", this.usernameTextbox.getScene().getWindow());
                            alert.showAndWait();
                        });
                    }
                    finally
                    {
                        Platform.runLater(() ->
                        {
                            this.progressSpin.setVisible(false);
                            this.connexionAttemptsCount++;
                        });
                    }

                }).start();
                if(this.connexionAttemptsCount == 2)
                {
                    CustomAlert alert = new CustomAlert(Alert.AlertType.ERROR, "L'authentification a échoué trop de fois.", "Trop de tentatives", this.usernameTextbox.getScene().getWindow());
                    alert.showAndWait();
                    try
                    {
                        this.client.quit();
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                    finally
                    {
                        Platform.exit();
                    }
                }
        }
    }

    private boolean checkTextBoxes()
    {
        boolean usernameTextBoxUnfilled = this.usernameTextbox.getText().equals("");
        boolean passwordTextBoxUnfilled = this.passwordTextbox.getText().equals("");

        if(usernameTextBoxUnfilled && passwordTextBoxUnfilled)
        {
            CustomAlert alert = new CustomAlert(Alert.AlertType.WARNING, "Veuillez renseigner vos informations de connexions.", "Mauvaise saisie", this.usernameTextbox.getScene().getWindow());
            alert.showAndWait();
            return false;
        }
        else if(usernameTextBoxUnfilled)
        {
            CustomAlert alert = new CustomAlert(Alert.AlertType.WARNING, "Veuillez renseigner votre identifiant.", "Mauvaise saisie", this.usernameTextbox.getScene().getWindow());
            alert.showAndWait();
            return false;
        }
        else if(passwordTextBoxUnfilled)
        {
            CustomAlert alert = new CustomAlert(Alert.AlertType.WARNING, "Veuillez renseigner votre mot de passe", "Mauvaise saisie", this.usernameTextbox.getScene().getWindow());
            alert.showAndWait();
            return false;
        }
        return true;
    }
}
