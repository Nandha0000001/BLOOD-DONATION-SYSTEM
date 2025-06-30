module BloodDonationManagement {
    requires java.sql;        // For JDBC Connection
    requires java.desktop;    // For ActionEvent and GUI classes
    exports com.blooddonationsystem.db;
    exports com.blooddonationsystem.main;
    exports com.blooddonationsystem.modules;
    exports com.blooddonationsystem.ui;
}
