package com.compscitutorials.basigarcia.navigationdrawervideotutorial;

import java.sql.Time;
import java.util.GregorianCalendar;

/**
 * Created by Rohit-Blade on 9/18/2017.
 */

public class TaskJSON {
    String name;
    String duration;
    String address;
    String city;
    Cal dueDate;
    String index;
    String firebaseaddress;

    public TaskJSON(){

    }
    public TaskJSON(String name, String duration, String address, String city, Cal dueDate, String index, String firebaseaddress) {
        this.name = name;
        this.duration = duration;
        this.address = address;
        this.city = city;
        this.dueDate = dueDate;
        this.index = index;
        this.firebaseaddress = firebaseaddress;
    }

    public String getFirebaseaddress() {
        return firebaseaddress;
    }

    public void setFirebaseaddress(String firebaseaddress) {
        this.firebaseaddress = firebaseaddress;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public String getDuration() {
        return duration;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public Cal getDueDate() {
        return dueDate;
    }

    public String getIndex() {
        return index;
    }

    public void setDueDate(Cal dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public String toString() {
        return name + "," + duration;
    }
}
