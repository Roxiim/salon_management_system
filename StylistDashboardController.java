package com.roxana.salonoop.controller;

import com.roxana.salonoop.model.*;
import com.roxana.salonoop.repository.*;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import java.time.LocalDate;

public class StylistDashboardController {

    @FXML
    private DatePicker filterDatePicker;

    @FXML
    private ListView<Appointment> appointmentsList;

    private final AppointmentRepository appointmentRepository = new AppointmentRepository();

    private User loggedUser;

    private final StylistRepository stylistRepository = new StylistRepository();

    private Stylist stylist;

    public void setLoggedUser(User user) {
        this.loggedUser = user;
        this.stylist = stylistRepository.findByUserId(user.getId());

        if (stylist == null) {
            showError("No stylist profile linked to this account.");
            return;
        }

        loadAppointments();
    }

    private void loadAppointments() {
        appointmentsList.getItems().setAll(
                appointmentRepository.getAppointmentsForStylist(stylist.getId())
        );
    }

    @FXML
    private void onFilterAppointments() {
        LocalDate selectedDate = filterDatePicker.getValue();

        if (selectedDate == null) {
            showError("Please select a date.");
            return;
        }

        appointmentsList.getItems().setAll(
                appointmentRepository.findByDateAndStylist(
                        selectedDate,
                        stylist.getId()
                )
        );
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void onClearFilter() {
        filterDatePicker.setValue(null);
        loadAppointments();
    }

}
