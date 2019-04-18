package com.company;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.function.Predicate;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.postgresql.core.SqlCommand;


public class Garage {
    static final String DB_URL = "jdbc:postgresql://localhost:5432/cars";
    static final String USER = "postgres";
    static final String PASS = "123";
    static Connection connection = null;
    private List<Vehicle> listCars = new ArrayList<>();

    public Garage() throws FileNotFoundException, ParseException, IOException{
        JSONObject obj = (JSONObject) new JSONParser().parse(new FileReader("resources/cars.json"));

        JSONArray vehicles = (JSONArray) obj.get("garage");

        Iterator it = vehicles.iterator();
        while (it.hasNext()) {
            JSONObject vehicle = (JSONObject) it.next();
            String model = vehicle.get("model").toString();
            String brand = vehicle.get("brand").toString();
            String year = vehicle.get("year").toString();
            Vehicle v = new Vehicle(model, brand, year);
            listCars.add(v);
        }

    }

    public String show(){
        if(listCars.isEmpty()) {
            return "гараж пуст!";
        }

        String show = "";
        for(Vehicle vehicle: listCars){
            show += vehicle.toString() + "\n";
        }
        return show;
    }

    public static Vehicle newVehicle(String veh) {
        String[] list = veh.split(" ");
        if(list.length == 3) {
            Vehicle vehicle = new Vehicle(list[0], list[1], list[2]);
            return vehicle;
        }
        else
            throw new IllegalArgumentException("vehicle includes three fields: model, brand and year");
    }

    public void addVehicle(Vehicle vehicle) throws FileNotFoundException, ParseException, IOException {
        if (listCars.contains(vehicle))
            System.out.println("this vehicle in garage");
        else {
            listCars.add(vehicle);
            JSONObject obj = (JSONObject) new JSONParser().parse(new FileReader("cars.json"));
            JSONArray vehicles = (JSONArray) obj.get("garage");

            JSONObject veh = new JSONObject();
            veh.put("model", vehicle.getModel());
            veh.put("brand", vehicle.getBrand());
            veh.put("year", vehicle.getYear());


            vehicles.add(veh);

            obj.put("garage", vehicles);

            FileWriter writer = new FileWriter("cars.json");
            writer.flush();
            writer.write(obj.toJSONString());
            writer.close();

            System.out.print(this.show());
        }
    }

    public void deleteVehicle(Vehicle vehicle) throws FileNotFoundException, ParseException, IOException{
        Predicate<Vehicle> vehiclePredicate = p -> p.toString().equals(vehicle.toString());
        listCars.removeIf(vehiclePredicate);
        JSONObject obj = (JSONObject) new JSONParser().parse(new FileReader("cars.json"));

        JSONArray vehicles = (JSONArray) obj.get("garage");
        JSONArray newVehicles = new JSONArray();

        Iterator it = vehicles.iterator();
        while (it.hasNext()) {
            JSONObject veh = (JSONObject) it.next();
            String model = veh.get("model").toString();
            String brand = veh.get("brand").toString();
            String year = veh.get("year").toString();
            Vehicle v = new Vehicle(model, brand, year);
            if(!v.toString().equals(vehicle.toString()))
                newVehicles.add(veh);
        }

        obj.put("garage", newVehicles);

        FileWriter writer = new FileWriter("cars.json");
        writer.flush();
        writer.write(obj.toJSONString());
        writer.close();
    }
    public void dbshow() throws SQLException{

        Statement stmt= null;
        ResultSet rs = null;
        Statement stmt1= null;
        ResultSet rs1 = null;

        stmt = connection.createStatement();
        rs = stmt.executeQuery( "SELECT * FROM cars;" );
        stmt1 = connection.createStatement();
        rs1 = stmt1.executeQuery( "SELECT * FROM models;" );

        while (rs.next() && rs1.next()) {
            System.out.print(rs1.getString(1) + " ");
            System.out.print(rs.getString(2) + " ");
            System.out.print(rs.getString(1) + " ");
            System.out.println();
        }


    }
    public void deleteVehicledb(Vehicle veh) throws SQLException{

        Statement stmt= null;
        ResultSet rs = null;
        Statement stmt1= null;
        ResultSet rs1 = null;

        stmt = connection.createStatement();
        rs = stmt.executeQuery( "SELECT * FROM cars;" );
        stmt1 = connection.createStatement();
        rs1 = stmt1.executeQuery( "SELECT * FROM models;" );


        int id;
        while (rs.next() && rs1.next()) {
            String s = "";
            s += (rs1.getString(1) + " ");
            s += (rs.getString(2) + " ");
            s += (rs.getString(1));
            Vehicle vehicle = newVehicle(s);
            id = (rs.getInt(3));
            if(vehicle.equals(veh)) {
                stmt1.executeUpdate("DELETE FROM cars WHERE id = "+ id +";");
                stmt1.executeUpdate("DELETE FROM models WHERE id = "+ id +";");
                System.out.println("Успешно удалено");
                return;
            }
        }
        System.out.println("В гараже нет этой машины");

    }

    public void addVehicledb(Vehicle veh) throws SQLException{

        Statement stmt= null;
        ResultSet rs = null;
        stmt = connection.createStatement();
        ResultSet lastval;
        stmt.executeUpdate( "insert into cars values ('" + veh.getYear() + "', '"  + veh.getBrand() + "');");
        lastval = stmt.executeQuery("SELECT LASTVAL()");
        lastval.next();
        stmt.executeUpdate( "insert into models values ('" + veh.getModel() + "', " + lastval.getInt(1) + ");");

    }

    public static void db() throws SQLException{
        System.out.println("Testing connection to PostgreSQL JDBC");

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver is not found. Include it in your library path ");
            e.printStackTrace();
            return;
        }

        System.out.println("PostgreSQL JDBC Driver successfully connected");

        try {
            connection = DriverManager
                    .getConnection(DB_URL, USER, PASS);

        } catch (SQLException e) {
            System.out.println("Connection Failed");
            e.printStackTrace();
            return;
        }

        if (connection != null) {
            System.out.println("You successfully connected to database now");
        } else {
            System.out.println("Failed to make connection to database");
        }
    }
}
