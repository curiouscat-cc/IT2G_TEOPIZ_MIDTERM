package com.scc.dao;

import com.scc.model.Vehicle;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VehicleDao {

    /**
     * Inserts a new vehicle record into the database using parameterized queries (PreparedStatement).
     */
    public boolean addVehicle(Vehicle vehicle) {
        String sql = "INSERT INTO vehicles (model_name, plate_no, daily_rate, status) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, vehicle.modelName());
            pstmt.setString(2, vehicle.plateNo());
            pstmt.setDouble(3, vehicle.dailyRate());
            pstmt.setString(4, vehicle.status());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("\n[DAO Error] Failed to insert vehicle record: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves all registered vehicles from the SQLite database.
     */
    public List<Vehicle> getAllVehicles() {
        List<Vehicle> fleetList = new ArrayList<>();
        String sql = "SELECT id, model_name, plate_no, daily_rate, status FROM vehicles ORDER BY id ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Vehicle vehicle = new Vehicle(
                        rs.getInt("id"),
                        rs.getString("model_name"),
                        rs.getString("plate_no"),
                        rs.getDouble("daily_rate"),
                        rs.getString("status")
                );
                fleetList.add(vehicle);
            }

        } catch (SQLException e) {
            System.err.println("\n[DAO Error] Failed to fetch fleet records: " + e.getMessage());
        }

        return fleetList;
    }
}