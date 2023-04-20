package com.keyin.model;

import com.keyin.HealthMonitoringApp;
import com.keyin.client.DatabaseConnection;
import org.mindrot.jbcrypt.BCrypt;
import java.util.Scanner;
import java.sql.*;

public class UserDao {

    public static void createUser() throws SQLException {
        DatabaseConnection database = new DatabaseConnection();
        Connection connection = database.DBConnect("SprintFinalJava", "postgres", "cupcake");
        Scanner scanner = new Scanner(System.in);

        String query = "INSERT INTO users_new (first_name, last_name, email, password, is_doctor) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);

        String doctorQuery = "INSERT INTO doctors_new SELECT * from users_new WHERE password = ?";
        PreparedStatement doctorStatement = connection.prepareStatement(doctorQuery);

        System.out.print("Enter your first name: ");
        String first_name = scanner.nextLine();

        System.out.print("Enter your last name: ");
        String last_name = scanner.nextLine();

        System.out.print("Enter your email: ");
        String email = scanner.nextLine();

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        System.out.print("Enter your Y if you're a DOCTOR: ");
        String YNDoctor = scanner.nextLine();
        YNDoctor = YNDoctor.toUpperCase();
        boolean is_doctor = false;

        if (YNDoctor.equals("Y")) {
            is_doctor = true;
            doctorStatement.setString(1, hashedPassword);
        }
        statement.setString(1, first_name);
        statement.setString(2, last_name);
        statement.setString(3, email);
        statement.setString(4, hashedPassword);
        statement.setBoolean(5, is_doctor);

        statement.executeUpdate();
        if (YNDoctor.equals("Y")) {
            doctorStatement.executeUpdate();
        }
        String idQuery = "SELECT id FROM users_new WHERE password = '" + hashedPassword + "'";
        Statement idStatement = connection.createStatement();
        ResultSet rs = idStatement.executeQuery(idQuery);
        while (rs.next()) {
            int id = rs.getInt("id");
            System.out.println("Successfully added as User! " + id + " is your user number!");
        }
        System.out.println("Refresh the app to login with your new account!");
        statement.close();
        doctorStatement.close();
        idStatement.close();
    }
    public static void getUserByEmail() throws SQLException { // get user by id from database
        DatabaseConnection database = new DatabaseConnection();
        Connection connection = database.DBConnect("SprintFinalJava", "postgres", "cupcake");
        Scanner scanner = new Scanner(System.in);

        System.out.println("User Email Lookup");
        String searchEmail = scanner.nextLine();

        String GET_USER_QUERY = "SELECT * FROM users_new WHERE email = '" + searchEmail + "'";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(GET_USER_QUERY);
        while (rs.next()) {
            int id = rs.getInt("id");
            String firstName = rs.getString("first_name");
            String lastName = rs.getString("last_name");
            String email = rs.getString("email");
            boolean is_doctor = rs.getBoolean("is_doctor");

            System.out.format("%s, %s, %s, %s, %s\n", id, firstName, lastName, email, is_doctor);
        }
        statement.close();

    }



    public static void getUserById() throws SQLException { //get user by email from database
        DatabaseConnection database = new DatabaseConnection();
        Connection connection = database.DBConnect("SprintFinalJava", "postgres", "cupcake");
        Scanner scanner = new Scanner(System.in);

        System.out.println("User ID Lookup");
        String searchID = scanner.nextLine();

        String GET_USER_QUERY = "SELECT * FROM users_new WHERE id =" + searchID;
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(GET_USER_QUERY);
        while (rs.next()) {
            int id = rs.getInt("id");
            String firstName = rs.getString("first_name");
            String lastName = rs.getString("last_name");
            String email = rs.getString("email");
            boolean is_doctor = rs.getBoolean("is_doctor");

            // print the results
            System.out.format("%s, %s, %s, %s, %s\n", id, firstName, lastName, email, is_doctor);
        }
        statement.close();
    }

    public static void updateUser() throws SQLException {
        DatabaseConnection database = new DatabaseConnection();
        Connection connection = database.DBConnect("SprintFinalJava", "postgres", "cupcake");
        Scanner scanner = new Scanner(System.in);

        System.out.println("Type in ID of user data to update");
        String searchID = scanner.nextLine();

        System.out.print("Enter your first name: ");
        String first_name = scanner.nextLine();

        System.out.print("Enter your last name: ");
        String last_name = scanner.nextLine();

        System.out.print("Enter your email: ");
        String email = scanner.nextLine();

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();


        String GET_USER_QUERY = "UPDATE users_new SET first_name = '" + first_name + "', last_name = '" + last_name + "', email = '"
                + email + "', password = '" + password + "' WHERE id = " + searchID;
        Statement statement = connection.createStatement();
        statement.executeUpdate(GET_USER_QUERY);
        System.out.format("%s, %s, %s, %s, %s\n", first_name, last_name, email, password);
        statement.close();

    }
    public static void deleteUser() throws SQLException {
        DatabaseConnection database = new DatabaseConnection();
        Connection connection = database.DBConnect("SprintFinalJava", "postgres", "cupcake");
        Scanner scanner = new Scanner(System.in);

        System.out.println("Type in ID of user data to delete");
        String searchID = scanner.nextLine();

        Statement statement = connection.createStatement();
        String delete = "DELETE FROM users_new WHERE id = " + searchID ;
        statement.executeUpdate(delete);
        System.out.println("User Deleted!");
        statement.close();
    }

    public static void getPortals () throws SQLException {
        DatabaseConnection database = new DatabaseConnection();
        Connection connection = database.DBConnect("SprintFinalJava", "postgres", "cupcake");
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your email: ");
        String inputUsername = scanner.nextLine();

        System.out.print("Enter your password: ");
        String inputPassword = scanner.nextLine();

        String GET_USER_QUERY = "SELECT * FROM users_new WHERE email = '" + inputUsername + "'";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(GET_USER_QUERY);
        while (rs.next()) {
            String hashedPassword = rs.getString("password");
            //String hashedPassword = BCrypt.hashpw(inputPassword, BCrypt.gensalt());
            boolean match = BCrypt.checkpw(inputPassword, hashedPassword);
            if (match) {
                System.out.println("Credentials Match. Entering app.");
                System.out.println("\n");
                String idQuery = "SELECT id FROM users_new WHERE email = '" + inputUsername + "'";
                Statement idStatement = connection.createStatement();
                ResultSet idRS = idStatement.executeQuery(idQuery);
                while (idRS.next()) {
                    int id = idRS.getInt("id");
                    System.out.println("Welcome User " + id + " !");
                }
                System.out.println("");
                String GET_BOOL_QUERY = "SELECT is_doctor FROM users_new WHERE email = '" + inputUsername + "'";
                Statement boolStatement = connection.createStatement();
                ResultSet boolRS = boolStatement.executeQuery(GET_BOOL_QUERY);
                while (boolRS.next()){
                    boolean isDoctor = boolRS.getBoolean("is_doctor");
                    //System.out.println(isDoctor);
                    if (isDoctor == true) {
                        HealthMonitoringApp.showDoctorPortal();
                    } else {
                        HealthMonitoringApp.showPatientPortal();
                    }
                }
                boolStatement.close();
                idStatement.close();
            } else {
                System.out.println("Wrong Credentials! Please Refresh the app and try again.");
            }
        }
        statement.close();


    }
    public int getID (int id) {
        DatabaseConnection database = new DatabaseConnection();
        Connection connection = database.DBConnect("SprintFinalJava", "postgres", "cupcake");
        Scanner scanner = new Scanner(System.in);

        return id;
    }

}
