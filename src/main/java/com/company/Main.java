package com.company;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) throws IOException, SQLException{
        Garage.db();

        Garage garage = new Garage();
        //System.out.println(garage.show());
        garage.dbshow();
        boolean f = true;
        while(f){
            System.out.println("s - show garage, a - add vehicle, d - delete vehicle, e - exit");
            char ch = (char) System.in.read();
            switch (ch){
                case('e'): {
                    f = false;
                    break;
                }
                case('s'): {
                    //System.out.println(garage.show());
                    garage.dbshow();
                    break;
                }
                case('a'): {
                    //System.out.println(garage.show());
                    Scanner sc = new Scanner(System.in);
                    String veh = sc.next();
                    while (veh.split(" ").length != Vehicle.getP()) {
                        veh += " " + sc.next();
                    }
                    Vehicle vehicle = Garage.newVehicle(veh);

                    //garage.addVehicle(vehicle);
                    garage.addVehicledb(vehicle);
                    break;
                }
                case('d'): {
                    //System.out.println(garage.show());
                    garage.dbshow();
                    Scanner sc = new Scanner(System.in);

                    String veh = sc.next();
                    while (veh.split(" ").length != Vehicle.getP()) {
                        veh += " " + sc.next();
                    }
                    Vehicle vehicle = Garage.newVehicle(veh);
                    //garage.deleteVehicle(vehicle);
                    garage.deleteVehicledb(vehicle);
                    break;
                }
            }

        }
    }
}

