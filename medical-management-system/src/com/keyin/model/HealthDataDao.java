package com.keyin.model;
import com.keyin.client.DatabaseConnection;

import java.sql.*;
import java.util.Scanner;

import org.mindrot.jbcrypt.BCrypt;

public class HealthDataDao {
    public static void createHealthData() throws SQLException {
        DatabaseConnection database = new DatabaseConnection();
        Connection connection = database.DBConnect("SprintFinalJava", "postgres", "cupcake");
        Scanner scanner = new Scanner(System.in);
        System.out.println("please enter your user ID number:");

        boolean match = verifyUserData();
        PreparedStatement statement = null;
        if (match == true) {
            System.out.println("Credentials Match. You may create new health data for this user.");
            System.out.println("\n");
            System.out.println("Create New Health Data Entry");

            String query = "INSERT INTO health_data (user_id, weight_lbs, height_cm , steps, heart_rate, date, recommendation_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
            statement = connection.prepareStatement(query);

            System.out.println("Enter your user ID number again:");
            int userID = Integer.parseInt(getID());

            System.out.print("Enter your weight: ");
            String weight = scanner.nextLine();
            int userWeight = Integer.parseInt(weight);

            System.out.print("Enter your height: ");
            String height = scanner.nextLine();
            int userHeight = Integer.parseInt(height);

            System.out.print("Enter your steps: ");
            String steps = scanner.nextLine();
            int userSteps = Integer.parseInt(steps);

            System.out.print("Enter your heart rate: ");
            String heartRate = scanner.nextLine();
            int userHeartRate = Integer.parseInt(heartRate);


            java.util.Date d = new java.util.Date();

            statement.setInt(1, userID);
            statement.setInt(2, userWeight);
            statement.setInt(3, userHeight);
            statement.setInt(4, userSteps);
            statement.setInt(5, userHeartRate);
            statement.setDate(6, new java.sql.Date(d.getTime()));

            boolean nav = true;
            while (nav) {
                if (userWeight >= 250) {
                    if (userSteps < 10000) {
                        if (100 < userHeartRate || 60 > userHeartRate) {
                            statement.setInt(7, 10);
                            nav=false;
                            break;
                        }
                        statement.setInt(7, 6);
                        nav = false;
                        break;
                    } else if (100 < userHeartRate || 60 > userHeartRate) {
                        statement.setInt(7, 8);
                        nav = false;
                    } else {
                        statement.setInt(7, 1);
                        nav = false;
                        break;
                    }
                } else if (userWeight <= 100) {
                    if (userSteps < 10000) {
                        if (100 < userHeartRate || 60 > userHeartRate) {
                            statement.setInt(7, 11);
                            nav = false;
                            break;
                        }
                        statement.setInt(7, 7);
                        nav = false;
                        break;
                    } else if (100 < userHeartRate || 60 > userHeartRate) {
                        statement.setInt(7, 9);
                        nav = false;
                        break;
                    } else {
                        statement.setInt(7, 2);
                        nav = false;
                        break;
                    }
                } else if (userSteps < 10000) {
                    if (100 < userHeartRate || 60 > userHeartRate) {
                        statement.setInt(7,12);
                        nav = false;
                        break;
                    } else {
                        statement.setInt(7, 3);
                        nav = false;
                        break;
                    }
                } else if (100 < userHeartRate || 60 > userHeartRate) {
                    statement.setInt(7, 4);
                    nav = false;
                    break;
                    //} else if ((userWeight < 100) && (userSteps < minS)) {
                    //statement.setInt(7, 7);
                    //} else if ((userWeight > maxW) && (userSteps < 10000)) {
                    //statement.setInt(7, 6);
                    //} else if ((userWeight < 100) && ((100 < userHeartRate) || (60 > userHeartRate))) {
                    //statement.setInt(7, 9);
                    //} else if ((userWeight > 250) && ((100 < userHeartRate) || (60 > userHeartRate))) {
                    //statement.setInt(7, 8);
                    //} else if ((userWeight < 100) && (userSteps < 10000) && ((100 < userHeartRate) || 60 > (userHeartRate))) {
                    //statement.setInt(7, 11);
                    //} else if ((userWeight > 250) && (userSteps < 10000) && ((100 < userHeartRate) || (60 > userHeartRate))) {
                    //statement.setInt(7, 10);
                } else {
                    statement.setInt(7, 5);
                    nav = false;
                    break;
                }
            }

            statement.executeUpdate();

            System.out.println("Successfully added Health Data for " + new java.sql.Date(d.getTime()) + " !");
        } else {
            System.out.println("Unable to add health data!");
        }

        statement.close();
    }

    public static void updateHealthData() throws SQLException {
        DatabaseConnection database = new DatabaseConnection();
        Connection connection = database.DBConnect("SprintFinalJava", "postgres", "cupcake");
        Scanner scanner = new Scanner(System.in);

        System.out.println("Health Data Lookup");
        System.out.println("Please Enter your Health Data ID that you'd like to update:");
        String dataID = scanner.nextLine();

        String GET_DATA_QUERY = "SELECT * FROM health_data WHERE health_data_id =" + dataID;
        Statement statementHD = connection.createStatement();
        ResultSet rs = statementHD.executeQuery(GET_DATA_QUERY);

        System.out.print("Please enter your user id: ");
        String id = scanner.nextLine();
        int intID = Integer.parseInt(id);
        //int intID = getID();

        System.out.print("To Verify you are the user, please enter your password: ");
        String inputPassword = scanner.nextLine();

        //Statement userStatement = null;
        //Statement statement = null;
        while (rs.next()) {
            int userID = rs.getInt("user_id");
            if (intID == userID) {
                Statement statement = null;
                Statement userStatement = null;
                String GET_USER_QUERY = "SELECT password FROM users_new WHERE id = " + intID;
                userStatement = connection.createStatement();
                ResultSet passwordSet = userStatement.executeQuery(GET_USER_QUERY);
                while (passwordSet.next()) {
                    String hashedPassword = passwordSet.getString("password");
                    if (BCrypt.checkpw(inputPassword, hashedPassword)) {
                        System.out.print("Enter your weight: ");
                        String weight = scanner.nextLine();
                        //int userWeight = Integer.parseInt(weight);

                        System.out.print("Enter your height: ");
                        String height = scanner.nextLine();
                        //int userHeight = Integer.parseInt(height);

                        System.out.print("Enter your steps: ");
                        String steps = scanner.nextLine();
                        //int userSteps = Integer.parseInt(steps);

                        System.out.print("Enter your heart rate: ");
                        String heartRate = scanner.nextLine();
                        //int userHeartRate = Integer.parseInt(heartRate);

                        String UPDATE_DATA_QUERY = "UPDATE health_data SET weight_lbs = " + weight + ", height_cm = " + height + ", steps = " + steps + ", heart_rate = " + heartRate
                                + " WHERE health_data_id = " + dataID;
                        statement = connection.createStatement();

                        statement.executeUpdate(UPDATE_DATA_QUERY);

                        System.out.println("Health Data Updated for Entry " + dataID);
                    } else {
                        System.out.println("Cannot update health data entry " + dataID);
                    }


                } userStatement.close();
                statement.close();
            }
            //statement.close();
            System.out.println("Error. Cannot access health data entry.");
        }
        //userStatement.close();
        statementHD.close();
    }




    public static void deleteHealthData() throws SQLException {
        DatabaseConnection database = new DatabaseConnection();
        Connection connection = database.DBConnect("SprintFinalJava", "postgres", "cupcake");
        Scanner scanner = new Scanner(System.in);

        System.out.println("Delete Health Data");
        System.out.println("Please Enter your Health Data ID that you'd like to delete:");
        String dataID = scanner.nextLine();

        System.out.print("Please enter your user id: ");
        String id = scanner.nextLine();
        int intID = Integer.parseInt(id);
        //int intID = getID();

        System.out.print("To Verify you are the user, please enter your password: ");
        String inputPassword = scanner.nextLine();

        //Statement userStatement = null;
        //Statement statement = null;
        String GET_DATA_QUERY = "SELECT * FROM health_data WHERE health_data_id =" + dataID;
        Statement statementHD = connection.createStatement();
        ResultSet rs = statementHD.executeQuery(GET_DATA_QUERY);
        while (rs.next()) {
            int userID = rs.getInt("user_id");
            if (intID == userID) {
                //Statement statement = null;
                //Statement userStatement = null;
                String GET_USER_QUERY = "SELECT password FROM users_new WHERE id = " + intID;
                Statement userStatement = connection.createStatement();
                ResultSet passwordSet = userStatement.executeQuery(GET_USER_QUERY);
                while (passwordSet.next()) {
                    String hashedPassword = passwordSet.getString("password");
                    if (BCrypt.checkpw(inputPassword, hashedPassword)) {
                        String delete = "DELETE FROM health_data WHERE health_data_id = " + dataID;
                        Statement deleteStatement = connection.createStatement();
                        deleteStatement.executeUpdate(delete);
                        System.out.println("Health Data Entry Deleted!");
                        deleteStatement.close();
                    } else {
                        System.out.println("Cannot delete health data entry " + dataID);
                    }
                }
                userStatement.close();

            } else {
                System.out.println("Cannot delete health data entry " + dataID);
            }
        } statementHD.close();
    }
    public static String getID() {
        Scanner scanner = new Scanner(System.in);

        //System.out.println("To add Health Data, please enter your user ID number:");
        String id = scanner.nextLine();

        return id;
    }
    public static boolean verifyUserData() throws SQLException {
        DatabaseConnection database = new DatabaseConnection();
        Connection connection = database.DBConnect("SprintFinalJava", "postgres", "cupcake");
        Scanner scanner = new Scanner(System.in);

        int id = Integer.parseInt(getID());

        System.out.print("To Verify you are the user, please enter your password: ");
        String inputPassword = scanner.nextLine();


        String GET_USER_QUERY = "SELECT password FROM users_new WHERE id = " + id;
        Statement userStatement = connection.createStatement();
        //System.out.println(GET_USER_QUERY);
        ResultSet passwordSet = userStatement.executeQuery(GET_USER_QUERY);

        while (passwordSet.next()) {
            String hashedPassword = passwordSet.getString("password");
            if (BCrypt.checkpw(inputPassword, hashedPassword)) {

                return true;
            }
        }
        userStatement.close();

        return false;
    }

}
