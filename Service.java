package com.roxana.salonoop.model;

public class Service {

    private int id;
    private String name;
    private int durationMinutes;
    private double price;

    public Service(){}

    public Service(int id, String name, int durationMinutes, double price) {
        this.id = id;
        this.name = name;
        this.durationMinutes = durationMinutes;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public double getPrice() {
        return price;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return name + " - " +
                price + " lei (" +
                durationMinutes + " min)";
    }

}
