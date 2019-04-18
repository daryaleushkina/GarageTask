package com.company;

public class Vehicle {
    private String model;
    private String brand;
    private String year;

    public Vehicle(String model, String brand, String year) {
        this.model = model;
        this.brand = brand;
        this.year = year;
    }

    public String toString() {
        return model + " " + brand + " " + year;
    }

    public Boolean equals(Vehicle veh) {
        if(this.model.equals(veh.getModel()) && this.brand.equals(veh.getBrand()) && this.year.equals(veh.getYear()))
            return true;
        else
            return false;
    }


    public String getModel() {
        return model;
    }
    public String getBrand() {
        return brand;
    }
    public String getYear() {
        return year;
    }

    public static int getP(){
        return 3;
    }

    public void setYear(String year) {
        this.year = year;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }
    public void setModel(String model) {
        this.model = model;
    }




}

