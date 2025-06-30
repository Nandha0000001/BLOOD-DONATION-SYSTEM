package com.blooddonationsystem.ui;
import com.blooddonationsystem.db.DBConnection;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SearchUI {

    public static void display() {
        JFrame frame = new JFrame("Search");
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null); 
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(20, 20)); 
        panel.setBackground(Color.WHITE);


        JLabel headingLabel = new JLabel("Search Options", JLabel.CENTER);
        headingLabel.setFont(new Font("Arial", Font.BOLD, 24)); 
        headingLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0)); 
        panel.add(headingLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1, 10, 10)); 
        buttonPanel.setBackground(Color.WHITE);


        JButton searchDonorBtn = new JButton("Search Donor by Blood Type");
        JButton viewHistoryBtn = new JButton("View Donation History");


        styleButton(searchDonorBtn);
        styleButton(viewHistoryBtn);

        buttonPanel.add(searchDonorBtn);
        buttonPanel.add(viewHistoryBtn);

        panel.add(buttonPanel, BorderLayout.CENTER);


        frame.add(panel);


        searchDonorBtn.addActionListener(e -> searchDonorByBloodType(frame));
        viewHistoryBtn.addActionListener(e -> viewDonationHistory(frame));


        frame.setVisible(true);
    }


    private static void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 16)); 
        button.setBackground(new Color(0, 123, 255)); 
        button.setForeground(Color.WHITE); 
        button.setFocusPainted(false); 
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); 
    }

   
    private static void searchDonorByBloodType(JFrame frame) {
        JFrame searchFrame = new JFrame("Search Donor by Blood Type");
        searchFrame.setSize(350, 200);
        searchFrame.setLocationRelativeTo(frame); 
        JPanel searchPanel = new JPanel(new GridLayout(3, 2, 10, 10)); 
        searchPanel.setBackground(Color.WHITE);

        JLabel bloodTypeLabel = new JLabel("Enter Blood Type: ");
        JTextField bloodTypeField = new JTextField();
        JButton searchBtn = new JButton("Search");

      
        styleButton(searchBtn);

        
        searchPanel.add(bloodTypeLabel);
        searchPanel.add(bloodTypeField);
        searchPanel.add(new JLabel()); 
        searchPanel.add(searchBtn);

        searchFrame.add(searchPanel);

        searchBtn.addActionListener((ActionEvent e) -> {
            String bloodType = bloodTypeField.getText().trim();
            if (bloodType.isEmpty()) {
                JOptionPane.showMessageDialog(searchFrame, "Please enter a blood type.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (Connection conn = DBConnection.getConnection()) {
                String sql = "SELECT * FROM donors WHERE blood_type = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, bloodType);
                ResultSet rs = stmt.executeQuery();

                StringBuilder result = new StringBuilder();
                while (rs.next()) {
                    result.append("ID: ").append(rs.getInt("id"))
                          .append(", Name: ").append(rs.getString("name"))
                          .append(", Email: ").append(rs.getString("email"))
                          .append("\n");
                }

                if (result.length() == 0) {
                    JOptionPane.showMessageDialog(searchFrame, "No donors found with this blood type.", "Search Result", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(searchFrame, result.toString(), "Search Result", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(searchFrame, "Error while searching for donors: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        searchFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        searchFrame.setVisible(true);
    }

    private static void viewDonationHistory(JFrame frame) {
        JFrame historyFrame = new JFrame("View Donation History");
        historyFrame.setSize(350, 200);
        historyFrame.setLocationRelativeTo(frame); 

        JPanel historyPanel = new JPanel(new GridLayout(3, 2, 10, 10)); // 10px gap between elements
        historyPanel.setBackground(Color.WHITE);

        JLabel donorIdLabel = new JLabel("Enter Donor ID: ");
        JTextField donorIdField = new JTextField();
        JButton viewHistoryBtn = new JButton("View History");


        styleButton(viewHistoryBtn);


        historyPanel.add(donorIdLabel);
        historyPanel.add(donorIdField);
        historyPanel.add(new JLabel()); 
        historyPanel.add(viewHistoryBtn);

        historyFrame.add(historyPanel);


        viewHistoryBtn.addActionListener((ActionEvent e) -> {
            String donorIdText = donorIdField.getText().trim();
            if (donorIdText.isEmpty()) {
                JOptionPane.showMessageDialog(historyFrame, "Please enter a donor ID.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int donorId = Integer.parseInt(donorIdText);
                try (Connection conn = DBConnection.getConnection()) {
                    String sql = "SELECT * FROM donations WHERE donor_id = ?";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setInt(1, donorId);
                    ResultSet rs = stmt.executeQuery();


                    StringBuilder result = new StringBuilder();
                    while (rs.next()) {
                        result.append("Blood Type: ").append(rs.getString("blood_type"))
                              .append(", Units: ").append(rs.getInt("units"))
                              .append("\n");
                    }

                    if (result.length() == 0) {
                        JOptionPane.showMessageDialog(historyFrame, "No donation history found for this donor.", "Donation History", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(historyFrame, result.toString(), "Donation History", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(historyFrame, "Please enter a valid donor ID.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(historyFrame, "Error while fetching donation history: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        historyFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        historyFrame.setVisible(true);
    }
}
