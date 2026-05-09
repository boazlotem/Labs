package org.example.lab3;

import javafx.application.Platform;

public class CheckLock extends Thread
{
    private User user;
    private int maxAttempts;
    private int lockoutTimeInSec;
    private LoginController controller;

    public CheckLock(User user, int maxAttempts, int lockoutTimeInSec, LoginController controller)
    {
        this.user=user;
        this.maxAttempts = maxAttempts;
        this.lockoutTimeInSec = lockoutTimeInSec;
        this.controller = controller;
    }
    public void run()
    {
        user.incrementLoginAttempts();
        if (user.getLogin_attempts() >= this.maxAttempts)
        {
            user.setLockoutEnd_time(System.currentTimeMillis() + lockoutTimeInSec * 1000);
            user.setLogin_attempts(0);
            Platform.runLater(() -> controller.showError("Too many failed attempts. Account is locked for " + lockoutTimeInSec + " seconds."));
        }
    }
}
