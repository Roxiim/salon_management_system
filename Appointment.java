package com.roxana.salonoop.model;

import java.time.LocalDateTime;

public class Appointment {

    private int id;
    private int customerId;
    private int stylistId;
    private int serviceId;
    private LocalDateTime appointmentDateTime;
    private String status;
    private String serviceName;
    private String stylistName;
    private String customerName;

    public Appointment(){}

    public Appointment(int id, int customerId, int stylistId, int serviceId, LocalDateTime appointmentDateTime, String status) {
        this.id = id;
        this.customerId = customerId;
        this.stylistId = stylistId;
        this.serviceId = serviceId;
        this.appointmentDateTime = appointmentDateTime;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getStylistId() {
        return stylistId;
    }

    public void setStylistId(int stylistId) {
        this.stylistId = stylistId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public LocalDateTime getAppointmentDateTime() {
        return appointmentDateTime;
    }

    public void setAppointmentDateTime(LocalDateTime appointmentDateTime) {
        this.appointmentDateTime = appointmentDateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getStylistName() {
        return stylistName;
    }

    public void setStylistName(String stylistName) {
        this.stylistName = stylistName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    @Override
    public String toString() {
        String time = appointmentDateTime.toLocalDate() + " "
                + appointmentDateTime.toLocalTime().toString().substring(0,5);

        String base = time + " | " + serviceName;

        if (stylistName != null) {
            base += " | Stylist: " + stylistName;
        }

        if (customerName != null) {
            base += " | Client: " + customerName;
        }

        base += " | " + getDisplayStatus();

        return base;
    }

    public boolean isCompleted() {
        return appointmentDateTime.isBefore(LocalDateTime.now())
                && !"CANCELLED".equals(status);
    }

    public String getDisplayStatus() {
        if ("CANCELLED".equals(status)) {
            return "CANCELLED";
        }
        if (isCompleted()) {
            return "finalizata";
        }
        return "programata";
    }

}
