package org.example.lab3;
import javafx.application.Platform;


import java.util.List;

public class Validate extends Thread {
    private List<User> validUsers;
    private String inputName;
    private String inputPassword;
    private LoginController controller;
    private int n;
    private int time;
    // Constructor to initialize the Validate thread with necessary data
    public Validate(List<User> validUsers, String inputName, String inputPassword, LoginController controller, int n, int time) {
        this.validUsers = validUsers;
        this.inputName = inputName;
        this.inputPassword = inputPassword;
        this.controller = controller;
        this.n = n;
        this.time = time;
    }

    @Override
    public void run()
    {
        User temp = null;
        // Iterate through the list of valid users to find a match for the input username
        if (validUsers != null) {
            for (User u : validUsers) {
                if (u.getUser_name().equals(inputName))
                {
                    temp = u;
                    break;
                }
            }
        }
        // If a matching user is found, check the password and handle login attempts and lockout logic
        if(temp!=null) {
            if(temp.isLockedOut()) {
                Platform.runLater(() -> controller.showError("Account is locked. Please try again later."));
                return;
            }
            if (temp.getPassword().equals(inputPassword)) {
                Platform.runLater(() -> controller.changeToWelcomeScreen());
                temp.resetAttempts();
            } else {
                Platform.runLater(() -> controller.showError("Login Failed! Invalid password"));
                CheckLock checkLockThread = new CheckLock(temp, n, time);
                checkLockThread.start();
            }
        }
        else
        {
            Platform.runLater(() -> controller.showError("Login Failed! Invalid username"));
        }
    }
}
