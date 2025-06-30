package com.blooddonationsystem.modules;
import com.blooddonationsystem.db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;


public class SearchFunctionalitiesModule {
    public static void search() {
        Scanner scanner = new Scanner(System.in); 
        try (Connection conn = DBConnection.getConnection()) {
            System.out.println("\n--- Search Options ---");
            System.out.println("1. Search Donor by Blood Type");
            System.out.println("2. View Donation History");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            if (choice == 1) {
                System.out.print("Enter blood type to search: ");
                String bloodType = scanner.next();
                String sql = "SELECT * FROM donors WHERE blood_type = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, bloodType);
                ResultSet rs = stmt.executeQuery();
                System.out.println("\n--- Donors with Blood Type: " + bloodType + " ---");
                while (rs.next()) {
                    System.out.println("ID: " + rs.getInt("id") +
                                       ", Name: " + rs.getString("name") +
                                       ", Email: " + rs.getString("email"));
                }
            } else if (choice == 2) {
                System.out.print("Enter donor ID to view history: ");
                int donorId = scanner.nextInt();
                String sql = "SELECT * FROM donations WHERE donor_id = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, donorId);
                ResultSet rs = stmt.executeQuery();
                System.out.println("\n--- Donation History for Donor ID: " + donorId + " ---");
                while (rs.next()) {
                    System.out.println("Blood Type: " + rs.getString("blood_type") +
                                       ", Units: " + rs.getInt("units"));
                }
            } else {
                System.out.println("Invalid choice.");
            }
        } catch (Exception e) {
            System.out.println("Error in search functionality: " + e.getMessage());
        } finally {
            scanner.close(); 
        }
    }
}
