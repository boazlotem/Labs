package org.example.lab3;
public class User
{
    private String user_name;
    private String password;
    private int login_attempts;
    private long lockoutEnd_time;

    public User(String user_name, String password)
    {
        this.user_name=user_name;
        this.password=password;
        this.login_attempts=0;
        this.lockoutEnd_time=0;
    }

    public String getUser_name()
    {
        return this.user_name;
    }

    public String getPassword() {
        return password;
    }

    public int getLogin_attempts() {
        return login_attempts;
    }
    public void setLockoutEnd_time(long lockoutEnd_time) {
        this.lockoutEnd_time = lockoutEnd_time;
    }
    // Increment login attempts and set lockout time if max attempts are reached
    public synchronized void incrementLoginAttempts() {

        this.login_attempts++;
    }

    // Reset login attempts and lockout time
    public synchronized void setLogin_attempts(int num)
    {
        this.login_attempts=num;
    }
    // Check if the user is currently locked out
    public synchronized boolean isLockedOut() {
        return System.currentTimeMillis() < lockoutEnd_time;
    }
    // Reset login attempts and lockout time after a successful login
    public synchronized void resetAttempts() {
        this.login_attempts = 0;
        this.lockoutEnd_time = 0;
    }


    @Override
    public String toString() {
        return "User{" +
                "user_name='" + user_name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

class InvalidInputException extends Exception
{
    public InvalidInputException(String message)
    {
        super(message);
    }
}
