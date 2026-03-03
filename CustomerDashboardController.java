package com.roxana.salonoop.controller;

import com.roxana.salonoop.repository.*;
import com.roxana.salonoop.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class CustomerDashboardController {

    @FXML
    private ListView<Service> serviceList;

    @FXML
    private ListView<Stylist> stylistList;

    @FXML
    private DatePicker datePicker;

    @FXML
    private ComboBox<String> timeComboBox;

    @FXML
    private Label statusLabel;

    @FXML
    private ListView<Appointment> appointmentsList;

    @FXML
    private Button cancelAppointmentBtn;

    @FXML
    private ComboBox<String> statusFilterBox;

    private List<Appointment> allAppointments;

    private final ServiceRepository serviceRepository = new ServiceRepository();

    private final StylistRepository stylistRepository = new StylistRepository();

    private final AppointmentRepository appointmentRepository = new AppointmentRepository();

    private final StylistScheduleRepository scheduleRepository = new StylistScheduleRepository();

    private User loggedUser;

    public void setLoggedUser(User user) {
        this.loggedUser = user;
        loadAppointments();
    }

    @FXML
    public void initialize() {
        statusLabel.setText("");

        serviceList.getItems().addAll(serviceRepository.getAllServices());

        stylistList.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldStylist, newStylist) -> updateAvailableTimes()
        );

        datePicker.valueProperty().addListener(
                (obs, oldDate, newDate) -> updateAvailableTimes()
        );

        statusFilterBox.getItems().addAll(
                "ALL",
                "programata",
                "finalizata",
                "CANCELLED"
        );

        statusFilterBox.setValue("ALL");
    }

    private void updateAvailableTimes() {
        Stylist stylist = stylistList.getSelectionModel().getSelectedItem();
        LocalDate date = datePicker.getValue();

        timeComboBox.getItems().clear();

        if (stylist == null || date == null) {
            return;
        }

        int dayOfWeek = date.getDayOfWeek().getValue(); // 1 = Monday
        LocalTime[] workingHours = scheduleRepository.getWorkingHours(
                stylist.getId(),
                dayOfWeek
        );

        if (workingHours == null) {
            statusLabel.setText("Stylist does not work on this day!");
            return;
        }
        List<String> allHours = new ArrayList<>();

        LocalTime time = workingHours[0];
        LocalTime end = workingHours[1];

        while (time.isBefore(end)) {
            allHours.add(time.toString().substring(0, 5));
            time = time.plusMinutes(30);
        }


        List<String> busyHours = appointmentRepository.getBusyHours(
                stylist.getId(),
                date
        );

        List<String> freeHours = new ArrayList<>(allHours);
        freeHours.removeAll(busyHours);

        timeComboBox.getItems().addAll(freeHours);
    }

    @FXML
    private void onSelectService() {

        Service selected = serviceList.getSelectionModel().getSelectedItem();

        if (selected == null) {
            statusLabel.setText("Please select a service!");
            return;
        }

        stylistList.getItems().clear();
        timeComboBox.getItems().clear();

        List<Stylist> stylists = stylistRepository.getStylistsForService(selected.getId());
        stylistList.getItems().addAll(stylists);

        statusLabel.setText("Service selected: " + selected.getName());

    }

    @FXML
    private void onMakeAppointment() {

        Service service = serviceList.getSelectionModel().getSelectedItem();
        Stylist stylist = stylistList.getSelectionModel().getSelectedItem();
        LocalDate date = datePicker.getValue();
        String time = timeComboBox.getSelectionModel().getSelectedItem();

        if (service == null) {
            statusLabel.setText("Please select a service!");
            return;
        }
        if (stylist == null) {
            statusLabel.setText("Please select a stylist!");
            return;
        }
        if (date == null) {
            statusLabel.setText("Please select a date!");
            return;
        }
        if (time == null) {
            statusLabel.setText("Please select a time!");
            return;
        }

        LocalDate today = LocalDate.now();

        if (date.isBefore(today)) {
            statusLabel.setText("You cannot book appointments in the past!");
            return;
        }

        if (date.isEqual(today)) {
            LocalTime selectedTime = LocalTime.parse(time);
            LocalTime now = LocalTime.now();

            if (selectedTime.isBefore(now)) {
                statusLabel.setText("You cannot book an appointment in the past!");
                return;
            }
        }

        String dateTimeString = date + " " + time + ":00";
        Timestamp timestamp = Timestamp.valueOf(dateTimeString);

        boolean success = appointmentRepository.createAppointment(
                loggedUser.getId(),
                stylist.getId(),
                service.getId(),
                timestamp
        );

        if (success) {
            statusLabel.setText("Appointment created successfully!");
            timeComboBox.getItems().clear();
            timeComboBox.getSelectionModel().clearSelection();
            datePicker.setValue(null);
            loadAppointments();
        } else {
            statusLabel.setText("ERROR: This time might already be booked!");
        }
    }

    private void loadAppointments() {
        allAppointments =
                appointmentRepository.getAppointmentsForCustomer(loggedUser.getId());

        appointmentsList.getItems().setAll(allAppointments);
    }


    @FXML
    private void onCancelAppointment() {

        Appointment selected = appointmentsList
                .getSelectionModel()
                .getSelectedItem();

        if (selected == null) {
            statusLabel.setText("Please select an appointment to cancel!");
            return;
        }

        if ("CANCELLED".equals(selected.getStatus())) {
            statusLabel.setText("This appointment is already cancelled.");
            return;
        }

        boolean success = appointmentRepository.cancelAppointment(
                selected.getId(),
                loggedUser.getId()
        );

        if (success) {
            statusLabel.setText("Appointment cancelled successfully!");
            loadAppointments();
        } else {
            statusLabel.setText("Error cancelling appointment.");
        }
    }

    @FXML
    private void onFilterByStatus() {
        String selectedStatus = statusFilterBox.getValue();

        if (selectedStatus == null || selectedStatus.equals("ALL")) {
            appointmentsList.getItems().setAll(allAppointments);
            return;
        }

        appointmentsList.getItems().setAll(
                allAppointments.stream()
                        .filter(a -> {
                            switch (selectedStatus) {
                                case "programata":
                                    return "programata".equals(a.getStatus())
                                            && !a.isCompleted();
                                case "finalizata":
                                    return a.isCompleted();
                                case "CANCELLED":
                                    return "CANCELLED".equals(a.getStatus());
                                default:
                                    return true;
                            }
                        })
                        .toList()
        );
    }

    @FXML
    private void onClearFilter() {
        statusFilterBox.setValue("ALL");
        appointmentsList.getItems().setAll(allAppointments);
    }

}
