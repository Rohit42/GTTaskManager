package com.compscitutorials.basigarcia.navigationdrawervideotutorial;

/**
 * Created by Rohit-Blade on 9/21/2017.
 */

class Cal {
    private int year_int;
    private int month_int;
    private int day_int;
    private int hour_int;
    private int minute_int;
    public Cal() {
    }

    public Cal(int year_int, int month_int, int day_int, int hour_int, int minute_int) {
        this.year_int = year_int;
        this.month_int = month_int;
        this.day_int = day_int;
        this.hour_int = hour_int;
        this.minute_int = minute_int;
    }

    public int getYear_int() {
        return year_int;
    }

    public void setYear_int(int year_int) {
        this.year_int = year_int;
    }

    public int getMonth_int() {
        return month_int;
    }

    public void setMonth_int(int month_int) {
        this.month_int = month_int;
    }

    public int getDay_int() {
        return day_int;
    }

    public void setDay_int(int day_int) {
        this.day_int = day_int;
    }

    public int getHour_int() {
        return hour_int;
    }

    public void setHour_int(int hour_int) {
        this.hour_int = hour_int;
    }

    public int getMinute_int() {
        return minute_int;
    }

    public void setMinute_int(int minute_int) {
        this.minute_int = minute_int;
    }
}
