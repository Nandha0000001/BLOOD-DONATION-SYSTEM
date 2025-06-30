package com.blooddonationsystem.modules;
import com.blooddonationsystem.db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class DonorRegistrationModule {
    public static void registerDonor() {
        Scanner scanner = new Scanner(System.in); // Initialize Scanner here
        try (Connection conn = DBConnection.getConnection()) {
            System.out.print("Enter donor name: ");
            String name = scanner.nextLine();
            System.out.print("Enter donor blood type: ");
            String bloodType = scanner.nextLine();
            System.out.print("Enter donor email: ");
            String email = scanner.nextLine();

            String sql = "INSERT INTO donors (name, blood_type, email) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, bloodType);
            stmt.setString(3, email);
            stmt.executeUpdate();
            System.out.println("Donor registered successfully!");
        } catch (Exception e) {
            System.out.println("Error registering donor: " + e.getMessage());
        } finally {
            scanner.close(); // Ensure Scanner is closed in the finally block
        }
    }
}
