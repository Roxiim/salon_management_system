package com.roxana.salonoop.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.roxana.salonoop.repository.*;
import com.roxana.salonoop.model.*;
import java.util.List;


public class AdminDashboardController {

    @FXML
    private ListView<User> usersList;

    @FXML
    private TextField stylistNameField;

    @FXML
    private TextField stylistSpecializationField;

    @FXML
    private Label statusLabel;

    private final UserRepository userRepository = new UserRepository();

    private final StylistRepository stylistRepository = new StylistRepository();

    @FXML
    private ListView<Service> servicesListView;

    private final ServiceRepository serviceRepository = new ServiceRepository();

    @FXML
    public void initialize() {
        usersList.getItems().addAll(userRepository.getAllUsers());
        servicesListView.getSelectionModel()
                .setSelectionMode(SelectionMode.MULTIPLE);
        servicesListView.getItems()
                .addAll(serviceRepository.getAllServices());
    }

    @FXML
    private void handleCreateStylist() {
        String name = stylistNameField.getText();
        String specialization = stylistSpecializationField.getText();

        List<Service> selectedServices =
                servicesListView.getSelectionModel().getSelectedItems();

        if (name.isEmpty() || specialization.isEmpty() || selectedServices.isEmpty()) {
            showError("Please fill all fields and select at least one service.");
            return;
        }

        Stylist stylist = new Stylist(0, name, specialization);

        stylistRepository.createStylistWithServices(stylist, selectedServices);

        showInfo("Stylist created successfully!");
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
