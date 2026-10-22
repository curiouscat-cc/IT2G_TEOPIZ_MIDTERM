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
            System.out.println("\n=====================================================================================");
            System.out.println("                     RENTAL FLEET REGISTRATION & STATUS SYSTEM                       ");
            System.out.println("=====================================================================================");
            System.out.println("[1] Add New Vehicle");
            System.out.println("[2] View Fleet Vehicles");
            System.out.println("[3] Exit System");
            System.out.print("Select an option (1-3): ");

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
        System.out.println("\n-------------------------------------------------------------------------------------");
        System.out.println("                                 ADD NEW VEHICLE                                     ");
        System.out.println("-------------------------------------------------------------------------------------");

        System.out.print("Enter Model Name (e.g., Toyota Vios): ");
        String modelName = scanner.nextLine().trim();

        System.out.print("Enter Plate No. (e.g., ABC-1234): ");
        String plateNo = scanner.nextLine().trim();

        double dailyRate = 0.0;
        while (true) {
            System.out.print("Enter Daily Rate (e.g., 2000.00): ");
            dailyRate = getDoubleInput();
            if (dailyRate > 0) break;
            System.out.println("[Invalid Rate] Daily rate must be greater than 0.");
        }

        String status = "";
        while (true) {
            System.out.println("Select Fleet Status:");
            System.out.println(" [1] Available");
            System.out.println(" [2] Rented");
            System.out.print("Choice: ");
            int statusChoice = getIntInput();
            if (statusChoice == 1) {
                status = "Available";
                break;
            } else if (statusChoice == 2) {
                status = "Rented";
                break;
            } else {
                System.out.println("[Invalid Selection] Choose 1 for Available or 2 for Rented.");
            }
        }

        Vehicle newVehicle = new Vehicle(0, modelName, plateNo, dailyRate, status);

        boolean success = vehicleDao.addVehicle(newVehicle);
        if (success) {
            System.out.println("\n-> SUCCESS: Vehicle record successfully added to fleet!");
        } else {
            System.out.println("\n-> FAILURE: Could not add vehicle. Please check for duplicate Plate Numbers.");
        }
    }

    private void handleViewFleet() {
        List<Vehicle> fleet = vehicleDao.getAllVehicles();

        System.out.println("\n=====================================================================================");
        System.out.println("                             RENTAL FLEET VEHICLES                                   ");
        System.out.println("=====================================================================================");

        if (fleet.isEmpty()) {
            System.out.println("No vehicle records found in the database fleet registry.");
            System.out.println("=====================================================================================");
            return;
        }

        // Broadened column widths to prevent line wrapping or layout crushing
        System.out.printf("| %-4s | %-25s | %-15s | %-15s | %-12s |%n", "ID", "MODEL NAME", "PLATE NO.", "DAILY RATE", "STATUS");
        System.out.println("-------------------------------------------------------------------------------------");

        for (Vehicle vehicle : fleet) {
            System.out.printf("| %-4d | %-25s | %-15s | PHP %-11.2f | %-12s |%n",
                    vehicle.id(),
                    vehicle.modelName(),
                    vehicle.plateNo(),
                    vehicle.dailyRate(),
                    vehicle.status());
        }

        System.out.println("=====================================================================================");
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