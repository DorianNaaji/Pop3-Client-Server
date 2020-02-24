package gui.generic;

import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.Window;

public class CustomAlert extends Alert
{
    public CustomAlert(Alert.AlertType alertType, String text, String title, Window parent)
    {
        super(alertType, text);
        this.setTitle(title);
        this.initOwner(parent);
        this.setHeaderText(null);
    }
}
