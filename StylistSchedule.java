package com.roxana.salonoop.model;

import java.time.LocalTime;

public class StylistSchedule {

    private int stylistId;
    private int dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;

    public StylistSchedule(int stylistId, int dayOfWeek,
                           LocalTime startTime, LocalTime endTime) {
        this.stylistId = stylistId;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getStylistId() {
        return stylistId;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

}

