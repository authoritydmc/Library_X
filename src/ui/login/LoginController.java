package ui.login;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import database.DatabaseHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.commons.codec.digest.DigestUtils;
import settings.Preferences;
import util.LibraryAssistantUtil;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class LoginController implements Initializable {


    @FXML
    private JFXTextField username;
    @FXML
    private JFXPasswordField password;

    Preferences preference;
    private double OffsetX = 0;
    private double OffsetY = 0;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DatabaseHandler.getInstance();
        preference = Preferences.getPreferences();
    }

    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        String uname = username.getText();
        String pword = DigestUtils.sha256Hex(password.getText());

        if (uname.equals(preference.getUsername()) && pword.equals(preference.getPassword())) {

            closeStage();
            loadMain();
        } else {

            username.getStyleClass().add("wrong-credentials");
            password.getStyleClass().add("wrong-credentials");
        }
    }

    @FXML
    private void handleCancelButtonAction(ActionEvent event) {
        System.exit(0);
    }

    private void closeStage() {
        ((Stage) username.getScene().getWindow()).close();
    }

    void loadMain() {
        try {

            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/ui/main/main.fxml"));
            stage.initStyle(StageStyle.TRANSPARENT);

            LibraryAssistantUtil.setStageIcon(stage);
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);
            stage.show();


        } catch (IOException ex) {
            System.out.println("Error in loading main" + ex);
            ex.printStackTrace();
        }
    }

}
