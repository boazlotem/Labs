package org.example.lab2;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.PasswordField;
import java.util.List;

public class LoginController {

    @FXML
    private Button LoginButton;

    @FXML
    private Button LogOut;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label passwordLabel;

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private TextField usernameField;

    @FXML
    private Label usernameLabel;

    private List<User> validUsers;


    public void setUsers(List<User> users) {
        this.validUsers = users;
    }

    @FXML
    // This method is called when the logout button is clicked. It loads the login screen again and sets the list of valid users in the new controller.
    private void handleLogOut(javafx.event.ActionEvent event)
    {
        try {
            FXMLLoader loader = new FXMLLoader(UserApp.class.getResource("login-view.fxml"));
            Scene scene = new Scene(loader.load(), 320, 240);

            LoginController controller = loader.getController();
            controller.setUsers(UserApp.getUserList());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Login");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e)
        {
            e.printStackTrace();
            statusLabel.setText("Error loading login screen");
        }

    }
    @FXML
    // This method is called when the login button is clicked. It checks the entered username and password against the list of valid users and either loads the welcome screen or displays an error message.
    private void handleLogin(javafx.event.ActionEvent event) {
        String inputName = usernameField.getText();
        String inputPassword = passwordField.getText();
        boolean authenticated = false;
        // Check if both fields are empty
        if(inputName.isEmpty() && inputPassword.isEmpty())
        {
            statusLabel.setText("Please enter both username and password");
            return;
        }// Check if either field is empty
        else if(inputName.isEmpty()) {
            statusLabel.setText("Please enter a username");
            return;
        }
        else if (inputPassword.isEmpty()) {
            statusLabel.setText("Please enter a password");
            return;
        }

        // Check if the validUsers list is not null before iterating
        if (validUsers != null) {
            for (User u : validUsers) {
                if (u.getUser_name().equals(inputName) && u.getPassword().equals(inputPassword)) {
                    authenticated = true;
                    break;
                }
            }
        }
        // If authenticated, load the welcome screen, otherwise show an error message
        if (authenticated) {
            try {
                FXMLLoader welcome = new FXMLLoader(LoginController.class.getResource("welcome-view.fxml"));
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setTitle("Welcome");
                Scene scene = new Scene(welcome.load(), 320, 240);
                stage.setScene(scene);
                stage.show();
            }
            catch (Exception e)
            {
                e.printStackTrace();
                statusLabel.setText("Error loading welcome screen");
            }

        }
        else {
            statusLabel.setStyle("-fx-text-fill: red;");
            statusLabel.setText("Login Failed! Invalid username or password");
        }
    }
}
