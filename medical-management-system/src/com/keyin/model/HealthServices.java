package com.keyin.model;

import com.keyin.HealthMonitoringApp;
import com.keyin.client.DatabaseConnection;

import java.sql.*;
import java.util.Scanner;

public class HealthServices {
    public static void createMedicineReminder() throws SQLException {
        DatabaseConnection database = new DatabaseConnection();
        Connection connection = database.DBConnect("SprintFinalJava", "postgres", "cupcake");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter your user ID number:");

        boolean match = HealthDataDao.verifyUserData();
        PreparedStatement statement = null;
        if (match == true) {
            System.out.println("Credentials Match. You may create a new Medicine Reminder for this user.");
            System.out.println("\n");
            System.out.println("Create New Reminder");

            String query = "INSERT INTO medicine_reminders (user_id, medicine_name, dosage, schedule, start_date, end_date) VALUES (?, ?, ?, ?, ?, ?)";
            statement = connection.prepareStatement(query);

            System.out.println("Enter your user ID number again:");
            int userID = Integer.parseInt(HealthDataDao.getID());

            System.out.print("Medicine Name: ");
            String medName = scanner.nextLine();

            System.out.print("Dosage: ");
            String dosage = scanner.nextLine();

            System.out.print("Schedule: ");
            String schedule = scanner.nextLine();

            System.out.print("Start date of meds (YYYY-MM-DD): ");
            String sDate = scanner.nextLine();
            Date startDate = Date.valueOf(sDate);

            System.out.println("End date of meds (YYYY-MM-DD):");
            String eDate = scanner.nextLine();
            Date endDate = Date.valueOf(eDate);


            //java.util.Date d = new java.util.Date();

            statement.setInt(1, userID);
            statement.setString(2, medName);
            statement.setString(3, dosage);
            statement.setString(4, schedule);
            statement.setDate(5, startDate);
            statement.setDate(6, endDate);

            statement.executeUpdate();

            System.out.println("Successfully added new medicine reminder!");
        } else {
            System.out.println("Unable to add reminder!");
        }

        statement.close();
    }

    public static void getRecommendations() throws SQLException {
        DatabaseConnection database = new DatabaseConnection();
        Connection connection = database.DBConnect("SprintFinalJava", "postgres", "cupcake");
        Scanner scanner = new Scanner(System.in);

        System.out.println("View Recommendations by user ID");
        Statement statement = null;

        System.out.println("Enter User ID:");
        boolean match = HealthDataDao.verifyUserData();
        if (match == true) {

        System.out.println("Reenter user id: ");
        String userID = scanner.nextLine();
        int id = Integer.parseInt(userID);
        String GET_DATA_QUERY = "SELECT health_data.user_id as id, users_new.first_name as fn, users_new.last_name as ln, recommendations.recommendation_text as rt \n" +
                "\tFROM users_new\n" +
                "\tINNER JOIN health_data\n" +
                "\tON health_data.user_id = users_new.id\n" +
                "\tINNER JOIN recommendations\n" +
                "\tON recommendations.recommendation_id = health_data.recommendation_id\n" +
                "\tWHERE id = " + id;
        //String GET_DATA_QUERY = "SELECT * FROM doctor_patient WHERE doctor_id = '" + doctorID + "'";
        statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(GET_DATA_QUERY);
        if (rs.next()) {
            String patID = rs.getString("id");
            String patFN = rs.getString("fn");
            String patLN = rs.getString("ln");
            String patRT = rs.getString("rt");
            //String patID = rs.getString("patient_id");
            System.out.println("Doctor Recommendations");
            System.out.println("User ID   Name                 Recommendations");
            System.out.println(patID + "       " + patFN + " " + patLN + "            " + patRT);
        } else {
            System.out.println("There are no Recommendations to display. Please refresh the app.");
        }
        statement.close();
    }

    }

    public static void getMedicineReminder() throws SQLException {
        DatabaseConnection database = new DatabaseConnection();
        Connection connection = database.DBConnect("SprintFinalJava", "postgres", "cupcake");
        Scanner scanner = new Scanner(System.in);

        System.out.println("View Medication Reminders");
        System.out.println("Please enter your user ID number:");
        boolean match = HealthDataDao.verifyUserData();
        //int id = Integer.parseInt(getID());
        Statement statement = null;

        if (match == true) {
            System.out.println("Enter your user ID number again:");
            int id = Integer.parseInt(HealthDataDao.getID());
            String GET_DATA_QUERY = "SELECT * FROM medicine_reminders WHERE user_id =" + id;
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(GET_DATA_QUERY);

            System.out.println("                       Medicine Reminder Display");
            System.out.println("MEDICINE           DOSAGE   SCHEDULE             START DATE       END DATE");
            while (rs.next()) {
                //int id = rs.getInt("health_data_id");
                //int userID = rs.getInt("user_id");
                String med = rs.getString("medicine_name");
                String dos = rs.getString("dosage");
                String sche = rs.getString("schedule");
                Date start = rs.getDate("start_date");
                Date end = rs.getDate("end_date");
                System.out.println(med + "                 " + dos + "     " + sche + "           " + start + "       " + end);
            }
        } else {
            System.out.println("Unable to find reminders.");
        }
        statement.close();
    }
}
