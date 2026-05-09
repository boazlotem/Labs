package org.example.lab3;
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
    private int n;
    private int time;

    public void setParams(int n, int time)
    {
        this.n = n;
        this.time = time;
    }

    // This method is used to set the list of valid users in the controller. It is called from the UserApp class when the login screen is loaded.
    public void setUsers(List<User> users)
    {
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
            controller.setParams(UserApp.getN(), UserApp.getTime());

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
    // This method is used to display an error message on the login screen. It is called from the Validate thread when the login fails.
    public void showError(String message) {
        statusLabel.setStyle("-fx-text-fill: red;");
        statusLabel.setWrapText(true);
        statusLabel.setText(message);
    }
    // This method is used to change the scene to the welcome screen. It is called from the CheckLock thread when the login is successful and the account is not locked.
    public void changeToWelcomeScreen() {
        try {
            FXMLLoader welcome = new FXMLLoader(LoginController.class.getResource("welcome-view.fxml"));
            Stage stage = (Stage)statusLabel.getScene().getWindow();
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
    @FXML
    // This method is called when the login button is clicked. It checks if the username and password fields are empty and displays an error message if they are. If both fields are filled, it starts a new Validate thread to check the credentials.
    private void handleLogin() {
        String inputName = usernameField.getText();
        String inputPassword = passwordField.getText();
        if(inputName.isEmpty() && inputPassword.isEmpty())
        {
            statusLabel.setText("Please enter both username and password");
            return;
        }
        else if(inputName.isEmpty()) {
            statusLabel.setText("Please enter a username");
            return;
        }
        else if (inputPassword.isEmpty()) {
            statusLabel.setText("Please enter a password");
            return;
        }
        Validate validateThread = new Validate(validUsers, inputName, inputPassword, this, n, time);
        validateThread.start();
    }
}
