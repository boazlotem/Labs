package org.example.lab2;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.util.Scanner;
public class UserApp extends Application
{
    // regular expression to check the username and password patterns
    private static final String userNameREGEX = "^([a-zA-Z0-9\\-+%._]+)@([a-zA-Z0-9][a-zA-Z0-9\\-.-]*)\\.([a-zA-Z]{2,})$";
    private static final Pattern userNamePATTERN = Pattern.compile(userNameREGEX);
    private static final String passwordREGEX="^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[+!%@#^=_()*&$;~-]).+$";
    private static final Pattern passwordPATTERN= Pattern.compile(passwordREGEX);
    // list to store the valid users
    private static final List<User> userList=new ArrayList<>();
    public static List<User> getUserList() {
        return userList;
    }
    // This method reads the users from the "Users.txt" file, validates them, and adds the valid users to the userList. It handles file not found exceptions and prints appropriate error messages.
    public static void loadUsersFromFile() {
        try {
            // read the file and extract the data
            File readFile = new File("Users.txt");
            if (!readFile.exists()) {
                System.out.println("Error: Users.txt file not found.");
                return;
            }
            Scanner Reader = new Scanner(readFile);
            String data;
            String password;
            String userName = "";
            User temp;
            while (Reader.hasNextLine()) {
                // Read a line from the file
                data = Reader.nextLine();
                // Returns a new string with leading and trailing whitespace removed
                data = data.trim();
                boolean first_string = false;
                int idx = 0;
                while (true) {
                    //check if the current character is a space and if it is the first string
                    if (data.charAt(idx) == ' ' && !first_string) {
                        //extract the username
                        userName = data.substring(0, idx);
                        first_string = true;
                    }

                    if (data.charAt(idx) != ' ' && first_string) {
                        //extract the password because it is the second string
                        password = data.substring(idx);
                        break;
                    }
                    idx++;
                }
                //check if the user is valid
                temp = checkUser(userName, password);
                if (temp != null) {
                    //add the user to the list if it is valid
                    userList.add(temp);
                }
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("Error: Users.txt file not found.");
        }
    }
    // This method checks if the provided username and password are valid according to the defined patterns and length requirements. It throws custom exceptions with specific error messages if any of the validation checks fail, and returns a User object if the input is valid.
    public static User checkUser(String user_name, String password)
    {
        int USER_MAX_LENGTH = 50;
        int PASSWORD_MIN_LENGTH = 8;
        int PASSWORD_MAX_LENGTH = 12;

        try {
            // check username length
            if (user_name.length() > USER_MAX_LENGTH)
                throw new InvalidInputException("Username is too long, try something shorter");
            // check password length
            if (password.length() < PASSWORD_MIN_LENGTH)
                throw new InvalidInputException("Your password is too short, add more characters");

            if (password.length() > PASSWORD_MAX_LENGTH)
                throw new InvalidInputException("Your password is too long, try a shorter one");

            Matcher name_matcher = userNamePATTERN.matcher(user_name);

            // check username pattern
            if (name_matcher.matches()) {
                Matcher password_matcher = passwordPATTERN.matcher(password);

                // check also password pattern
                if (password_matcher.matches())
                {
                    return new User(user_name, password);
                }
                // if the password doesn't match the pattern throw an exception
                else
                {
                    throw new InvalidInputException("Please enter a valid password");
                }
            }
            // if the username doesn't match the pattern throw an exception
            else
            {
                throw new InvalidInputException("Please enter a valid Email as username");
            }
        }
        catch (InvalidInputException e)
        {
            // print the error message if the user is invalid
            System.out.println(e.getMessage());
            return null;
        }
    }
    @Override

    public void start(Stage stage) throws IOException {
        loadUsersFromFile();
        FXMLLoader fxmlLoader = new FXMLLoader(UserApp.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("User Login");
        LoginController controller = fxmlLoader.getController();
        controller.setUsers(userList);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args)
    {
        launch();
    }
}
