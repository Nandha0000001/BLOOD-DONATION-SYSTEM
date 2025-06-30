package com.blooddonationsystem.main;
import java.util.Scanner;
import com.blooddonationsystem.modules.DonorRegistrationModule;
import com.blooddonationsystem.modules.BloodInventoryManagementModule;
import com.blooddonationsystem.modules.BloodRequestHandlingModule;
import com.blooddonationsystem.modules.SearchFunctionalitiesModule;


public class BloodDonationManagement {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;
        do {
            System.out.println("\n--- Blood Donation Management System ---");
            System.out.println("1. Donor Registration");
            System.out.println("2. View Blood Inventory");
            System.out.println("3. Add Donation");
            System.out.println("4. Handle Blood Request");
            System.out.println("5. Search Functionality");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            switch (choice) {
                case 1 -> DonorRegistrationModule.registerDonor();
                case 2 -> BloodInventoryManagementModule.viewInventory();
                case 3 -> BloodInventoryManagementModule.addDonation(scanner);
                case 4 -> BloodRequestHandlingModule.handleRequest();
                case 5 -> SearchFunctionalitiesModule.search();
                case 0 -> System.out.println("Exiting the system...");
                default -> System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 0);
        scanner.close();
    }
}
