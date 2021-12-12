package com.bqmz001.watchboard.alarm;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

public class AlarmBean extends LitePalSupport {
    @Column(unique = true)
    private long id;
    @Column(nullable = false)
    private String uuid;
    @Column(nullable = false)
    private boolean isEnabled;
    @Column(nullable = false)
    private int hour;
    @Column(nullable = false)
    private int minute;
    @Column(nullable = false)
    private boolean isOnce;
    @Column(nullable = false)
    private boolean mondayEnabled;
    @Column(nullable = false)
    private boolean tuesdayEnabled;
    @Column(nullable = false)
    private boolean wednesdayEnabled;
    @Column(nullable = false)
    private boolean thursdayEnabled;
    @Column(nullable = false)
    private boolean fridayEnabled;
    @Column(nullable = false)
    private boolean saturdayEnabled;
    @Column(nullable = false)
    private boolean sundayEnabled;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public boolean isOnce() {
        return isOnce;
    }

    public void setOnce(boolean once) {
        isOnce = once;
    }

    public boolean isMondayEnabled() {
        return mondayEnabled;
    }

    public void setMondayEnabled(boolean mondayEnabled) {
        this.mondayEnabled = mondayEnabled;
    }

    public boolean isTuesdayEnabled() {
        return tuesdayEnabled;
    }

    public void setTuesdayEnabled(boolean tuesdayEnabled) {
        this.tuesdayEnabled = tuesdayEnabled;
    }

    public boolean isWednesdayEnabled() {
        return wednesdayEnabled;
    }

    public void setWednesdayEnabled(boolean wednesdayEnabled) {
        this.wednesdayEnabled = wednesdayEnabled;
    }

    public boolean isThursdayEnabled() {
        return thursdayEnabled;
    }

    public void setThursdayEnabled(boolean thursdayEnabled) {
        this.thursdayEnabled = thursdayEnabled;
    }

    public boolean isFridayEnabled() {
        return fridayEnabled;
    }

    public void setFridayEnabled(boolean fridayEnabled) {
        this.fridayEnabled = fridayEnabled;
    }

    public boolean isSaturdayEnabled() {
        return saturdayEnabled;
    }

    public void setSaturdayEnabled(boolean saturdayEnabled) {
        this.saturdayEnabled = saturdayEnabled;
    }

    public boolean isSundayEnabled() {
        return sundayEnabled;
    }

    public void setSundayEnabled(boolean sundayEnabled) {
        this.sundayEnabled = sundayEnabled;
    }
}
