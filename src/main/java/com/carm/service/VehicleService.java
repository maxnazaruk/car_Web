package com.carm.service;

import com.carm.entity.Vehicle;
import com.carm.jdbc.JdbcVehicleDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VehicleService {
    private JdbcVehicleDao jdbcVehicleDao;

    public VehicleService(JdbcVehicleDao jdbcVehicleDao) {
        this.jdbcVehicleDao = jdbcVehicleDao;
    }

    public List<String> findAllVehiclesByVin() throws SQLException {
        List<String> vins = new ArrayList<>();
        for (Vehicle vehicle : jdbcVehicleDao.findAll()) {
            vins.add(vehicle.getVIN());
        }
        return vins;
    }

    public void add(Vehicle vehicle) {
        try {
            jdbcVehicleDao.addVehicle(vehicle);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public Vehicle findByVin(String vin) throws SQLException {
        return jdbcVehicleDao.findVehicle(vin);
    }

    public void generateVehicles() throws SQLException {

        for (int q = 0; q < 10; q++) {
            Vehicle vehicle = Vehicle.builder()
                    .VIN(generateVin())
                    .model(generateModel())
                    .owner(generateOwner())
                    .build();
            jdbcVehicleDao.addVehicle(vehicle);
        }
    }

    public static String generateVin() {
        StringBuilder vin = new StringBuilder();
        char c;
        for (int i = 0; i < 20; i++) {
            if (i < 5 || i > 14) {
                c = (char) ((Math.random() * 10) + 48);
            } else {
                c = (char) ((Math.random() * 25) + 65);
            }
            vin.append(c);
        }

        return vin.toString();
    }

    public static String generateModel() {
        StringBuilder model = new StringBuilder();
        char c;
        for (int i = 0; i < 5; i++) {
            c = (char) ((Math.random() * 25) + 97);
            model.append(c);
        }

        return model.toString();
    }

    public static String generateOwner() {
        StringBuilder owner = new StringBuilder();
        char c;
        for (int i = 0; i < 5; i++) {
            c = (char) ((Math.random() * 25) + 97);
            owner.append(c);
        }

        return owner.toString();
    }

    public void delete() throws SQLException {
        jdbcVehicleDao.delete();
    }
}
