package com.blooddonationsystem.ui;
import com.blooddonationsystem.db.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class BloodInventoryUI {

	public static void viewInventory() {
	    JFrame frame = new JFrame("Blood Inventory");
	    frame.setSize(400, 300);
	    JTextArea textArea = new JTextArea();

	    SwingUtilities.invokeLater(() -> {
	        try (Connection conn = DBConnection.getConnection()) {
	            if (conn == null) {
	                textArea.setText("Unable to connect to the database.");
	                return;
	            }
	            String sql = "SELECT * FROM inventory";
	            try (PreparedStatement stmt = conn.prepareStatement(sql);
	                 ResultSet rs = stmt.executeQuery()) {

	                StringBuilder inventory = new StringBuilder();
	                inventory.append(String.format("%-15s %-10s\n", "Blood Type", "Units"));
	                inventory.append("------------------------------------\n");

	                while (rs.next()) {
	                    String bloodType = rs.getString("blood_type");
	                    int units = rs.getInt("units");

	                    inventory.append(String.format("%-15s %-10d\n", bloodType, units));
	                }

	                textArea.setText(inventory.toString());
	            }
	        } catch (SQLException e) {
	            textArea.setText("Error: " + e.getMessage());
	        }
	    });

	    frame.add(new JScrollPane(textArea));
	    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    frame.setVisible(true);
	}


    public static void addDonation() {
        JFrame frame = new JFrame("Add Donation");
        frame.setSize(300, 300);
        JPanel panel = new JPanel(new GridLayout(4, 2));

        JLabel donorIdLabel = new JLabel("Donor ID:");
        JTextField donorIdField = new JTextField();
        JLabel bloodTypeLabel = new JLabel("Blood Type:");
        JTextField bloodTypeField = new JTextField();
        JLabel unitsLabel = new JLabel("Units:");
        JTextField unitsField = new JTextField();
        JButton submitBtn = new JButton("Add");

        panel.add(donorIdLabel);
        panel.add(donorIdField);
        panel.add(bloodTypeLabel);
        panel.add(bloodTypeField);
        panel.add(unitsLabel);
        panel.add(unitsField);
        panel.add(new JLabel());
        panel.add(submitBtn);

        frame.add(panel);

        submitBtn.addActionListener(e -> {
            String donorIdText = donorIdField.getText();
            String bloodTypeText = bloodTypeField.getText();
            String unitsText = unitsField.getText();

            if (donorIdText.isEmpty() || bloodTypeText.isEmpty() || unitsText.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill in all fields.");
                return;
            }

            try {
                int donorId = Integer.parseInt(donorIdText);
                int units = Integer.parseInt(unitsText);

                SwingUtilities.invokeLater(() -> {
                    try (Connection conn = DBConnection.getConnection()) {
                        if (conn == null) {
                            JOptionPane.showMessageDialog(frame, "Unable to connect to the database.");
                            return;
                        }

                        String checkBloodTypeSql = "SELECT COUNT(*) AS count FROM inventory WHERE blood_type = ?";
                        try (PreparedStatement checkBloodTypeStmt = conn.prepareStatement(checkBloodTypeSql)) {
                            checkBloodTypeStmt.setString(1, bloodTypeText);
                            ResultSet rs = checkBloodTypeStmt.executeQuery();

                            if (rs.next() && rs.getInt("count") == 0) {
                                JOptionPane.showMessageDialog(frame, "Blood type does not exist in the inventory.");
                                return;
                            }
                        }

                        
                        String donationSql = "INSERT INTO donations (donor_id, blood_type, units) VALUES (?, ?, ?)";
                        try (PreparedStatement donationStmt = conn.prepareStatement(donationSql)) {
                            donationStmt.setInt(1, donorId);
                            donationStmt.setString(2, bloodTypeText);
                            donationStmt.setInt(3, units);
                            donationStmt.executeUpdate();
                        }

                        String inventorySql = "UPDATE inventory SET units = units + ? WHERE blood_type = ?";
                        try (PreparedStatement inventoryStmt = conn.prepareStatement(inventorySql)) {
                            inventoryStmt.setInt(1, units);
                            inventoryStmt.setString(2, bloodTypeText);
                            inventoryStmt.executeUpdate();
                        }

                        JOptionPane.showMessageDialog(frame, "Donation recorded successfully!");
                        frame.dispose();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
                    }
                });
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Please enter valid numbers for donor ID and units.");
            }
        });

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }}
