package com.blooddonationsystem.ui;
import com.blooddonationsystem.db.DBConnection;


import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DonorRegistrationUI {

    public static void display() {
        JFrame frame = new JFrame("Register Donor");
        frame.setSize(400, 400); 
        frame.setLocationRelativeTo(null); 

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(20, 20)); 
        panel.setBackground(Color.WHITE);

        JLabel headingLabel = new JLabel("Donor Registration", JLabel.CENTER);
        headingLabel.setFont(new Font("Arial", Font.BOLD, 24)); 
        headingLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0)); 
        panel.add(headingLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10)); 
        formPanel.setBackground(Color.WHITE);

 
        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();
        JLabel bloodTypeLabel = new JLabel("Blood Type:");
        JTextField bloodTypeField = new JTextField();
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();

 
        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(bloodTypeLabel);
        formPanel.add(bloodTypeField);
        formPanel.add(emailLabel);
        formPanel.add(emailField);

        panel.add(formPanel, BorderLayout.CENTER);

        JButton submitBtn = new JButton("Register");
        submitBtn.setFont(new Font("Arial", Font.BOLD, 16));
        submitBtn.setBackground(new Color(0, 123, 255)); 
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setFocusPainted(false); 
        submitBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); 

      
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(submitBtn);
        panel.add(buttonPanel, BorderLayout.SOUTH);

       
        submitBtn.addActionListener(e -> {
            
            String name = nameField.getText().trim();
            String bloodType = bloodTypeField.getText().trim();
            String email = emailField.getText().trim();

            if (name.isEmpty() || bloodType.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill in all fields.");
                return;
            }

            String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
            if (!email.matches(emailRegex)) {
                JOptionPane.showMessageDialog(frame, "Please enter a valid email.");
                return;
            }

            SwingUtilities.invokeLater(() -> {
                try (Connection conn = DBConnection.getConnection()) {
                    if (conn == null) {
                        JOptionPane.showMessageDialog(frame, "Unable to connect to the database.");
                        return;
                    }

                    String sql = "INSERT INTO donors (name, blood_type, email) VALUES (?, ?, ?)";
                    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                        stmt.setString(1, name);
                        stmt.setString(2, bloodType);
                        stmt.setString(3, email);
                        stmt.executeUpdate();
                        JOptionPane.showMessageDialog(frame, "Donor Registered Successfully!");
                        frame.dispose(); 
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
                }
            });
        });

        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }
}

