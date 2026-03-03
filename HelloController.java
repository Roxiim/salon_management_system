package com.roxana.salonoop;

import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import com.roxana.salonoop.model.User;
import com.roxana.salonoop.repository.UserRepository;
import com.roxana.salonoop.controller.CustomerDashboardController;
import com.roxana.salonoop.controller.StylistDashboardController;

public class HelloController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    private final UserRepository userRepository = new UserRepository();

    @FXML
    protected void onLoginClick() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter username and password!");
            return;
        }

        User user = userRepository.login(username, password);

        if (user == null) {
            errorLabel.setText("Invalid username or password!");
            return;
        }

        switch (user.getRole()) {
            case "customer" -> openCustomerDashboard(user);
            case "stylist" -> openStylistDashboard(user);
            case "admin" -> openAdminDashboard(user);
            default -> errorLabel.setText("Unknown role: " + user.getRole());
        }
    }

    private void openCustomerDashboard(User loggedUser) {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("customer.fxml"));
            Parent root = loader.load();

            CustomerDashboardController controller = loader.getController();
            controller.setLoggedUser(loggedUser);

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root, 680, 560));
            stage.setTitle("Customer Dashboard");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Failed to load dashboard.");
        }
    }

    private void openStylistDashboard(User loggedUser) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    HelloApplication.class.getResource("stylist.fxml")
            );
            Parent root = loader.load();

            StylistDashboardController controller = loader.getController();
            controller.setLoggedUser(loggedUser);

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root, 600, 307));
            stage.setTitle("Stylist Dashboard");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openAdminDashboard(User loggedUser) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    HelloApplication.class.getResource("admin.fxml")
            );
            Parent root = loader.load();

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root, 600, 400));
            stage.setTitle("Admin Dashboard");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Failed to load admin dashboard.");
        }
    }

}
