package com.blooddonationsystem.modules;
import com.blooddonationsystem.db.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;
public class BloodInventoryManagementModule {

    public static void viewInventory() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM inventory";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            System.out.println("\n--- Blood Inventory ---");
            while (rs.next()) {
                System.out.println("Blood Type: " + rs.getString("blood_type") +
                                   ", Units: " + rs.getInt("units"));
            }
        } catch (Exception e) {
            System.out.println("Error viewing inventory: " + e.getMessage());
        }
    }

    public static void addDonation(Scanner scanner) {
    	
        try (Connection conn = DBConnection.getConnection()) {
            System.out.print("Enter donor ID: ");
            int donorId = scanner.nextInt();
            System.out.print("Enter blood type: ");
            String bloodType = scanner.next();
            System.out.print("Enter units donated: ");
            int units = scanner.nextInt();

            // Check if the blood type exists in inventory
            String checkBloodTypeSQL = "SELECT COUNT(*) FROM inventory WHERE blood_type = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkBloodTypeSQL);
            checkStmt.setString(1, bloodType);
            ResultSet rs = checkStmt.executeQuery();
            rs.next();

            if (rs.getInt(1) == 0) {
                // Blood type does not exist, insert it with 0 units
                String insertBloodTypeSQL = "INSERT INTO inventory (blood_type, units) VALUES (?, 0)";
                PreparedStatement insertStmt = conn.prepareStatement(insertBloodTypeSQL);
                insertStmt.setString(1, bloodType);
                insertStmt.executeUpdate();
                System.out.println("Blood type added to inventory.");
            }

            // Insert donation record
            String donationSql = "INSERT INTO donations (donor_id, blood_type, units) VALUES (?, ?, ?)";
            PreparedStatement donationStmt = conn.prepareStatement(donationSql);
            donationStmt.setInt(1, donorId);
            donationStmt.setString(2, bloodType);
            donationStmt.setInt(3, units);
            donationStmt.executeUpdate();

            // Update inventory
            String inventorySql = "UPDATE inventory SET units = units + ? WHERE blood_type = ?";
            PreparedStatement inventoryStmt = conn.prepareStatement(inventorySql);
            inventoryStmt.setInt(1, units);
            inventoryStmt.setString(2, bloodType);
            inventoryStmt.executeUpdate();
            System.out.println("Donation recorded and inventory updated successfully!");

        } catch (Exception e) {
            System.out.println("Error adding donation: " + e.getMessage());
        }
    }
    }
