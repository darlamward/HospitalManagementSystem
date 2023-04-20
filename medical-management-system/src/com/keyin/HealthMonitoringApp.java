// A hospital portal working through the terminal. It is connected to a health database in pgAdmin.
// The only thing needed in the main is loginUser(). The app will then work.
// Completed on April 20, 2023
// Completed by Darla Ward.

package com.keyin;
import java.sql.*;
import java.util.Scanner;

import com.keyin.model.DoctorPortalDao;
import com.keyin.model.HealthDataDao;
import com.keyin.model.HealthServices;
import com.keyin.model.UserDao;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;


public class HealthMonitoringApp {
    public static void main(String[] args) throws SQLException {
        loginUser();
        //Below are all my testing function calls.
        //HealthDataDao.createHealthData();
        //HealthDataDao.getHealthDataByUserId();
        //HealthDataDao.getHealthDataByDataId();
        //HealthDataDao.updateHealthData();
        //HealthDataDao.deleteHealthData();
        //getDateToday();
        //UserDao.getUserById();
        //UserDao.getUserByEmail();
        //UserDao.updateUser();
        //UserDao.deleteUser();
        //UserDao.verifyPassword();
        //UserDao.createUser();
        //DoctorPortalDao.getDoctorById();
        //boolean block = HealthDataDao.verifyUserData();
        //System.out.println(block);
        //DoctorPortalDao.addPatientDoctorRelationship();
        //DoctorPortalDao.getPatientsByDoctorId();
        //HealthServices.getRecommendations();
        //HealthServices.createMedicineReminder();
        //HealthServices.getMedicineReminder();
    }

    public static void loginUser() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Health Monitoring App!");
        System.out.println("Would you like to LOGIN or REGISTER?");
        System.out.print("Type LOGIN or REGISTER: ");
        String appAction = scanner.nextLine();
        appAction = appAction.toUpperCase();

        if (appAction.equals("LOGIN")) {
            UserDao.getPortals();

        } else {
            UserDao.createUser();
        }
    }
    public static void showPatientPortal() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("1. Add Health Data Entry");
        System.out.println("2. Update Health Data Entry");
        System.out.println("3. Delete Health Data Entry");
        System.out.println("4. View Doctor Recommendations" );
        System.out.println("5. Create Medication Reminder");
        System.out.println("6. View Medication Reminders");
        System.out.println("7. Search for Doctor by Doctor ID");

        System.out.println("Select Menu Item");
        String menuItem = scanner.nextLine();
        if (menuItem.equals("1")){
            HealthDataDao.createHealthData();
        }else if (menuItem.equals("2")) {
            HealthDataDao.updateHealthData();
        } else if (menuItem.equals("3")){
            HealthDataDao.deleteHealthData();
        } else if (menuItem.equals("4")){
            HealthServices.getRecommendations();
        } else if (menuItem.equals("5")){
            HealthServices.createMedicineReminder();
        } else if (menuItem.equals("6")) {
            HealthServices.getMedicineReminder();
        } else if (menuItem.equals("7")){
            DoctorPortalDao.getDoctorById();
        } else {
            System.out.println("Not a valid option. Please refresh the app!");
        }
    }
    public static void showDoctorPortal () throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("1. View Patients");
        System.out.println("2. View Patient Info by User ID");
        System.out.println("3. View Patient Info by email");
        System.out.println("4. Search Patient Health Data by ID");
        System.out.println("5. Search Patient Health Data by Health Data ID");
        System.out.println("6. Add Doctor Patient Relationship");

        System.out.println("Select Menu Item");
        String menuItem = scanner.nextLine();
        if (menuItem.equals("1")){
            DoctorPortalDao.getPatientsByDoctorId();
        }else if (menuItem.equals("2")) {
            UserDao.getUserById();
        } else if (menuItem.equals("3")){
            UserDao.getUserByEmail();
        }else if (menuItem.equals("4")) {
            DoctorPortalDao.getHealthDataByUserId();
        }else if (menuItem.equals("5")) {
            DoctorPortalDao.getHealthDataByDataId();
        }else if (menuItem.equals("6")){
            DoctorPortalDao.addPatientDoctorRelationship();
        } else {
            System.out.println("Not a valid option. Please refresh the app!");
        }

    }

}

