package com.blooddonationsystem.ui;
import com.blooddonationsystem.db.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class BloodRequestUI {

    public static void handleRequest() {
        JFrame frame = new JFrame("Blood Requests");
        frame.setSize(400, 300); // Increased size for a better layout
        frame.setLocationRelativeTo(null); // Center the window
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(20, 20)); // BorderLayout with gaps for better spacing
        panel.setBackground(Color.WHITE);

        JLabel headingLabel = new JLabel("Blood Requests", JLabel.CENTER);
        headingLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Large font for heading
        headingLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0)); // Padding around heading
        panel.add(headingLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10)); // 10px gap between fields
        formPanel.setBackground(Color.WHITE);

        JLabel bloodTypeLabel = new JLabel("Blood Type:");
        JTextField bloodTypeField = new JTextField();
        JLabel unitsLabel = new JLabel("Units Required:");
        JTextField unitsField = new JTextField();

        formPanel.add(bloodTypeLabel);
        formPanel.add(bloodTypeField);
        formPanel.add(unitsLabel);
        formPanel.add(unitsField);

        panel.add(formPanel, BorderLayout.CENTER);

        JButton submitBtn = new JButton("Submit Request");
        submitBtn.setFont(new Font("Arial", Font.BOLD, 16)); 
        submitBtn.setBackground(new Color(0, 123, 255)); 
        submitBtn.setForeground(Color.WHITE); 
        submitBtn.setFocusPainted(false); 
        submitBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding around the button

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(submitBtn);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        submitBtn.addActionListener((ActionEvent e) -> {
            String bloodType = bloodTypeField.getText().trim();
            String unitsText = unitsField.getText().trim();

            if (bloodType.isEmpty() || unitsText.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int unitsRequired = Integer.parseInt(unitsText);

                Connection conn = DBConnection.getConnection();
                if (conn == null) {
                    JOptionPane.showMessageDialog(frame, "Unable to connect to the database.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String checkSql = "SELECT units FROM inventory WHERE blood_type = ?";
                PreparedStatement checkStmt = conn.prepareStatement(checkSql);
                checkStmt.setString(1, bloodType);
                ResultSet rs = checkStmt.executeQuery();

                if (rs.next()) {
                    int availableUnits = rs.getInt("units");
                    if (availableUnits >= unitsRequired) {
                        String updateSql = "UPDATE inventory SET units = units - ? WHERE blood_type = ?";
                        PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                        updateStmt.setInt(1, unitsRequired);
                        updateStmt.setString(2, bloodType);
                        updateStmt.executeUpdate();

                        JOptionPane.showMessageDialog(frame, "Blood request fulfilled successfully!");
                    } else {
                        JOptionPane.showMessageDialog(frame, "Insufficient stock for the requested blood type.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Blood type not found in inventory.", "Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Please enter a valid number for units.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Error handling request: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.add(panel);
        frame.setVisible(true);
    }
}
