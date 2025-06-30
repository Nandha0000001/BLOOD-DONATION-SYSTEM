package com.blooddonationsystem.ui;

import javax.swing.*;
import java.awt.*;

public class BloodDonationManagementUI {
    public static void main(String[] args) {
        
        JFrame frame = new JFrame("Blood Donation Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null); 

        
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(20, 20)); 
        panel.setBackground(Color.BLACK);

        JLabel headingLabel = new JLabel("Blood Donation Management System", JLabel.CENTER);
        headingLabel.setFont(new Font("Arial", Font.BOLD, 24)); 
        headingLabel.setForeground(new Color(139,0,0)); 
        headingLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0)); 
        panel.add(headingLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(6, 1, 10, 10)); 
        buttonPanel.setBackground(Color.BLACK); 

        JButton registerDonorBtn = createStyledButton("Register Donor");
        JButton viewInventoryBtn = createStyledButton("View Blood Inventory");
        JButton addDonationBtn = createStyledButton("Add Donation");
        JButton handleRequestBtn = createStyledButton("Handle Blood Request");
        JButton searchBtn = createStyledButton("Search");
        JButton exitBtn = createStyledButton("Exit");

        buttonPanel.add(registerDonorBtn);
        buttonPanel.add(viewInventoryBtn);
        buttonPanel.add(addDonationBtn);
        buttonPanel.add(handleRequestBtn);
        buttonPanel.add(searchBtn);
        buttonPanel.add(exitBtn);

        panel.add(buttonPanel, BorderLayout.CENTER);

        frame.add(panel);

        registerDonorBtn.addActionListener(e -> DonorRegistrationUI.display());
        viewInventoryBtn.addActionListener(e -> BloodInventoryUI.viewInventory());
        addDonationBtn.addActionListener(e -> BloodInventoryUI.addDonation());
        handleRequestBtn.addActionListener(e -> BloodRequestUI.handleRequest());
        searchBtn.addActionListener(e -> SearchUI.display());
        exitBtn.addActionListener(e -> System.exit(0));

        frame.setVisible(true);
    }

    private static JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16)); 
        button.setBackground(new Color(139, 0, 0)); 
        button.setForeground(Color.WHITE); 
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30)); 
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); 
        return button;
    }
}
