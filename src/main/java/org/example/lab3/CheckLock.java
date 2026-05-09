package org.example.lab3;

import javafx.application.Platform;

public class CheckLock extends Thread
{
    private User user;
    private int maxAttempts;
    private int lockoutTimeInSec;

    public CheckLock(User user, int maxAttempts, int lockoutTimeInSec)
    {
        this.user=user;
        this.maxAttempts = maxAttempts;
        this.lockoutTimeInSec = lockoutTimeInSec;
    }
    public void run()
    {
        user.incrementLoginAttempts();
        if (user.getLogin_attempts() >= this.maxAttempts)
        {
            user.setLockoutEnd_time(System.currentTimeMillis() + lockoutTimeInSec * 1000);
            user.setLogin_attempts(0);
        }
    }
}
