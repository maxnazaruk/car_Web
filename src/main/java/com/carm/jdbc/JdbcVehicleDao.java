package com.carm.jdbc;

import com.carm.entity.Vehicle;
import com.carm.entity.Worker;
import com.carm.jdbc.mapper.VehicleMapper;
import com.carm.jdbc.mapper.WorkerMapper;
import org.postgresql.util.PSQLException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcVehicleDao {
    private static final VehicleMapper VEHICLE_MAPPER = new VehicleMapper();
    private static final String SELECT_ALL = "SELECT id, model, owner, vin FROM cars";
    private static final String SELECT_SINGLE = "SELECT id, model, owner, vin FROM cars WHERE vin = ?";
    private static final String SELECT_SINGLE_ID = "SELECT id, model, owner, vin FROM cars WHERE id = ?";
    private static final String INSERT_INTO = "INSERT INTO cars (model, owner, vin) VALUES (?, ?, ?);";
    private static final String UPDATE = "UPDATE cars SET model = ?, owner = ?, vin = ? WHERE id = ?;";
    private static final String REMOVE = "DELETE FROM cars WHERE id = ?;";
    private static final String CREATE_TABLE = "CREATE TABLE cars (id SERIAL, model varchar(255), owner varchar(255), vin varchar(255));";
    private static final String DROP_TABLE = "DROP TABLE cars;";

    private Connection getConnection() throws SQLException, PSQLException {
        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/cars",
                "user", "pswd");
    }

    public void addVehicle(Vehicle vehicle) throws SQLException {
        if(!checkTableExists()){
            createUsersTable();
        }
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_INTO);
            preparedStatement.setString(1, vehicle.getModel());
            preparedStatement.setString(2, vehicle.getOwner());
            preparedStatement.setString(3, vehicle.getVIN());
            preparedStatement.executeUpdate();
        }
    }

    public Vehicle findVehicle(String vin) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_SINGLE)) {
            preparedStatement.setString(1, vin);
            ResultSet resultSet = preparedStatement.executeQuery();

            Vehicle vehicle = null;
            while (resultSet.next()) {
                vehicle = VEHICLE_MAPPER.mapResultSet(resultSet);
            }
            resultSet.close();
            return vehicle;
        }
    }

    public void delete() throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DROP_TABLE)) {
            preparedStatement.executeUpdate();
            createUsersTable();
        }
    }

    public Vehicle selectVehicleById(int id) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_SINGLE_ID)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            Vehicle vehicle = null;
            while (resultSet.next()) {
                vehicle = VEHICLE_MAPPER.mapResultSet(resultSet);
            }
            resultSet.close();
            return vehicle;
        }
    }

    public List<Vehicle> findAll() throws SQLException {
        if(!checkTableExists()){
            createUsersTable();
        }
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            List<Vehicle> cars = new ArrayList<>();
            while (resultSet.next()) {
                Vehicle vehicle = VEHICLE_MAPPER.mapResultSet(resultSet);
                cars.add(vehicle);
            }
            return cars;
        }
    }

    public void update(Vehicle vehicle, int id) throws SQLException {
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE);
            preparedStatement.setString(1, vehicle.getModel());
            preparedStatement.setString(2, vehicle.getOwner());
            preparedStatement.setString(3, vehicle.getVIN());
            preparedStatement.setInt(4, id);
            preparedStatement.executeUpdate();
        }
    }

    public void remove(int id) throws SQLException {
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(REMOVE);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    private void createUsersTable() {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(CREATE_TABLE)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private  boolean checkTableExists() {
        try (Connection connection = getConnection();
        ) {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            ResultSet resultSet = databaseMetaData.getTables(null, null, "cars", null);
            if(resultSet.next()){
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
