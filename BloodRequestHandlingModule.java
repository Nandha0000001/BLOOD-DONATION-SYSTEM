package com.blooddonationsystem.modules;
import com.blooddonationsystem.db.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;  

public class BloodRequestHandlingModule {
    public static void handleRequest() {
        Scanner scanner = new Scanner(System.in); // Initialize Scanner here
        try (Connection conn = DBConnection.getConnection()) {
            System.out.print("Enter blood type requested: ");
            String bloodType = scanner.next();
            System.out.print("Enter units required: ");
            int units = scanner.nextInt();

            String checkSql = "SELECT units FROM inventory WHERE blood_type = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, bloodType);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next() && rs.getInt("units") >= units) {
                String updateSql = "UPDATE inventory SET units = units - ? WHERE blood_type = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                updateStmt.setInt(1, units);
                updateStmt.setString(2, bloodType);
                updateStmt.executeUpdate();
                System.out.println("Request fulfilled successfully!");
            } else {
                System.out.println("Insufficient stock for the requested blood type.");
            }
        } catch (Exception e) {
            System.out.println("Error handling request: " + e.getMessage());
        } finally {
            scanner.close(); 
        }
    }
}
