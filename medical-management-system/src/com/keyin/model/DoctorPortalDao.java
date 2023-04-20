package com.keyin.model;

import com.keyin.client.DatabaseConnection;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.List;
import java.util.Scanner;

public class DoctorPortalDao {
    public static void getDoctorById() throws SQLException {
        DatabaseConnection database = new DatabaseConnection();
        Connection connection = database.DBConnect("SprintFinalJava", "postgres", "cupcake");
        Scanner scanner = new Scanner(System.in);

        System.out.println("Doctor Lookup by ID");
        System.out.println("Enter the Doctor ID:");
        String searchID = scanner.nextLine();

        String GET_USER_QUERY = "SELECT * FROM doctors_new WHERE users_id =" + searchID;
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(GET_USER_QUERY);
        if (rs.next()) {
            int id = rs.getInt("users_id");
            String firstName = rs.getString("first_name");
            String lastName = rs.getString("last_name");
            String email = rs.getString("email");

            // print the results
            System.out.println("Doctor ID: " + id);
            System.out.println("Name: " + firstName + " " + lastName);
            System.out.println("Email: " + email);
        } else {
            System.out.println("There is no doctor by that ID.");
        }
        statement.close();
    }

    public static void addPatientDoctorRelationship() throws SQLException {
        DatabaseConnection database = new DatabaseConnection();
        Connection connection = database.DBConnect("SprintFinalJava", "postgres", "cupcake");
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please enter your ID number:");
        boolean match = HealthDataDao.verifyUserData();
        //int id = Integer.parseInt(getID());
        PreparedStatement statement = null;

        if (match == true) {
            System.out.println("Enter your ID number again:");
            int id = Integer.parseInt(HealthDataDao.getID());
            System.out.println("Enter Patient ID:");
            String p_id = scanner.nextLine();
            int patient_id = Integer.parseInt(p_id);
            String UPDATE_QUERY = "INSERT INTO doctor_patient (doctor_id, patient_id) VALUES (" + id + ", " + patient_id + ")";
            statement = connection.prepareStatement(UPDATE_QUERY);
            try {
                statement.executeUpdate();
                System.out.println("Successfully added Doctor Patient Relationship.");
            } catch (SQLException e) {
                System.out.println("Relationship already exists.");
            }
        } else {
            System.out.println("Unable to connect to Doctor Patient Table. Please restart app and try another id.");
        }
        statement.close();
    }

    public static void getPatientsByDoctorId() throws SQLException {
        DatabaseConnection database = new DatabaseConnection();
        Connection connection = database.DBConnect("SprintFinalJava", "postgres", "cupcake");
        Scanner scanner = new Scanner(System.in);

        System.out.println("Get Patients by Doctor ID");
        Statement statement = null;

        System.out.println("Enter Doctor ID: ");
        String doctorID = scanner.nextLine();
        String GET_DATA_QUERY = "SELECT doctors_new.first_name as dfn, doctors_new.last_name as dln," +
                " users_new.first_name as pfn, users_new.last_name as pln" +
                "  FROM doctor_patient\n" +
                "  INNER JOIN doctors_new\n" +
                "  ON doctor_patient.doctor_id = doctors_new.users_id\n" +
                "  INNER JOIN users_new\n" +
                "  ON doctor_patient.patient_id = users_new.id\n" +
                "  WHERE doctor_id = " + doctorID;
        //String GET_DATA_QUERY = "SELECT * FROM doctor_patient WHERE doctor_id = '" + doctorID + "'";
        statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(GET_DATA_QUERY);
        if (rs.next()) {
            String docFN = rs.getString("dfn");
            String docLN = rs.getString("dln");
            String patFN = rs.getString("pfn");
            String patLN = rs.getString("pln");
            //String patID = rs.getString("patient_id");
            System.out.println("Doctor Patient Relationships");
            System.out.println("Doctor                      Patient");
            System.out.println(docFN + " " + docLN + "                 " + patFN + " " + patLN);
        } else {
            System.out.println("There are no Relationships to display. Please refresh the app.");
        }
        statement.close();
    }

    public static void getHealthDataByUserId() throws SQLException {
        // searches by
        DatabaseConnection database = new DatabaseConnection();
        Connection connection = database.DBConnect("SprintFinalJava", "postgres", "cupcake");
        Scanner scanner = new Scanner(System.in);

        System.out.println("Health Data Lookup");
        System.out.println("Please enter your user ID number:");
        boolean match = HealthDataDao.verifyUserData();
        //int id = Integer.parseInt(getID());
        Statement statement = null;

        if (match == true) {
            System.out.println("Enter the patient id:");
            int id = Integer.parseInt(HealthDataDao.getID());
            String GET_DATA_QUERY = "SELECT * FROM health_data WHERE user_id =" + id;
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(GET_DATA_QUERY);

            System.out.println("                       Health Data Display");
            System.out.println("   DATE       WEIGHT(lbs)       HEIGHT(cm)       STEPS       HEART RATE(bpm)");
            while (rs.next()) {
                //int id = rs.getInt("health_data_id");
                //int userID = rs.getInt("user_id");
                int weight = rs.getInt("weight_lbs");
                int height = rs.getInt("height_cm");
                int steps = rs.getInt("steps");
                int heart_rate = rs.getInt("heart_rate");
                Date date = rs.getDate("date");
                System.out.println(date + "       " + weight + "               " + height + "           " + steps + "           " + heart_rate);
            }
        } else {
            System.out.println("Unable to find health data.");
        }
        statement.close();
    }

    public static void getHealthDataByDataId() throws SQLException {
        DatabaseConnection database = new DatabaseConnection();
        Connection connection = database.DBConnect("SprintFinalJava", "postgres", "cupcake");
        Scanner scanner = new Scanner(System.in);

        System.out.println("Health Data Lookup");
        Statement statement = null;

        System.out.println("Enter health data entry id: ");
        String healthData = scanner.nextLine();

        String GET_DATA_QUERY = "SELECT * FROM health_data WHERE health_data_id =" + healthData;
        statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(GET_DATA_QUERY);

        System.out.print("Please enter your user id: ");
        String id = scanner.nextLine();
        int intID = Integer.parseInt(id);
        //int intID = getID();

        System.out.print("To Verify you are the user, please enter your password: ");
        String inputPassword = scanner.nextLine();

        while (rs.next()) {
            //int id = rs.getInt("health_data_id");
            int userID = rs.getInt("user_id");
            int weight = rs.getInt("weight_lbs");
            int height = rs.getInt("height_cm");
            int steps = rs.getInt("steps");
            int heart_rate = rs.getInt("heart_rate");
            Date date = rs.getDate("date");
            if (intID == userID) {
                String GET_USER_QUERY = "SELECT password FROM users_new WHERE id = " + intID;
                Statement userStatement = connection.createStatement();
                ResultSet passwordSet = userStatement.executeQuery(GET_USER_QUERY);
                System.out.println("                       Health Data Display");
                System.out.println("   DATE       WEIGHT(lbs)       HEIGHT(cm)       STEPS       HEART RATE(bpm)");
                while (passwordSet.next()) {
                    String hashedPassword = passwordSet.getString("password");
                    if (BCrypt.checkpw(inputPassword, hashedPassword)) {
                        System.out.println(date + "       " + weight + "               " + height + "           " + steps + "           " + heart_rate);
                    }
                }

                //System.out.println(date + "       " + weight + "               " + height + "           " + steps + "           " + heart_rate);
            } else {
                System.out.println("There is no Health Data available.");
            }
        }
        statement.close();
    }
}



