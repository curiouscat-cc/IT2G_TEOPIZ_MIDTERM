package com.scc.ui;

import com.scc.dao.VehicleDao;
import com.scc.model.Vehicle;

import java.util.List;
import java.util.Scanner;

public class VehicleMenu {
    private final VehicleDao vehicleDao;
    private final Scanner scanner;

    public VehicleMenu() {
        this.vehicleDao = new VehicleDao();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            System.out.println("\n==============================================================================");
            System.out.println("                           CAR RENTAL FLEET TRACKER                           ");
            System.out.println("==============================================================================");
            System.out.println("[1] Add New Vehicle");
            System.out.println("[2] View Fleet Vehicles");
            System.out.println("[3] Exit System");
            System.out.println("==========================");
            System.out.print("Enter choice: ");

            int choice = getIntInput();

            switch (choice) {
                case 1 -> handleAddVehicle();
                case 2 -> handleViewFleet();
                case 3 -> {
                    System.out.println("\nExiting system safely... Thank you!");
                    return;
                }
                default -> System.out.println("\n[Invalid Option] Please select between 1 and 3.");
            }
        }
    }

    private void handleAddVehicle() {
        System.out.println("\n-----ADD NEW VEHICLE------");

        System.out.print("Enter Model Name: ");
        String modelName = scanner.nextLine().trim();

        System.out.print("Enter Plate No.: ");
        String plateNo = scanner.nextLine().trim();

        double dailyRate = 0.0;
        while (true) {
            System.out.print("Enter Daily Rate: ");
            dailyRate = getDoubleInput();
            if (dailyRate > 0) break;
            System.out.println("[Invalid Rate] Daily rate must be greater than 0.");
        }

        String status = "";
        while (true) {
            System.out.print("Enter Status (AVAILABLE/RENTED): ");
            String inputStatus = scanner.nextLine().trim();
            if (inputStatus.equalsIgnoreCase("AVAILABLE")) {
                status = "Available";
                break;
            } else if (inputStatus.equalsIgnoreCase("RENTED")) {
                status = "Rented";
                break;
            } else {
                System.out.println("[Invalid Selection] Please type AVAILABLE or RENTED.");
            }
        }

        Vehicle newVehicle = new Vehicle(0, modelName, plateNo, dailyRate, status);

        boolean success = vehicleDao.addVehicle(newVehicle);
        if (success) {
            System.out.println("\nSUCCESS: Vehicle Registered to Fleet!");
        } else {
            System.out.println("\n-> FAILURE: Could not add vehicle. Please check for duplicate Plate Numbers.");
        }
    }

    private void handleViewFleet() {
        List<Vehicle> fleet = vehicleDao.getAllVehicles();

        System.out.println("\n==============================================================================");
        System.out.println("                            RENTAL FLEET VEHICLES                             ");
        System.out.println("==============================================================================");

        if (fleet.isEmpty()) {
            System.out.println("No vehicle records found in the database fleet registry.");
            System.out.println("==============================================================================");
            System.out.println("Total Records Displayed: 0");
            return;
        }

        // Perfectly aligned table headers matching the 78-char grid boundary
        System.out.printf("| %-4s | %-22s | %-12s | %-14s | %-10s |%n", "ID", "MODEL NAME", "PLATE NO.", "DAILY RATE", "STATUS");
        System.out.println("------------------------------------------------------------------------------");

        // Perfectly aligned table data rows
        for (Vehicle vehicle : fleet) {
            System.out.printf("| %-4d | %-22s | %-12s | PHP %-10.2f | %-10s |%n",
                    vehicle.id(),
                    vehicle.modelName(),
                    vehicle.plateNo(),
                    vehicle.dailyRate(),
                    vehicle.status());
        }

        System.out.println("==============================================================================");
        System.out.println("Total Records Displayed: " + fleet.size());
    }

    private int getIntInput() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private double getDoubleInput() {
        try {
            return Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1.0;
        }
    }
}