package com.carm.jdbc;

import com.carm.entity.SparePart;
import com.carm.entity.Vehicle;
import com.carm.jdbc.mapper.SparePartMapper;
import com.carm.jdbc.mapper.VehicleMapper;
import org.postgresql.util.PSQLException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcSparePartDao {
    private static final SparePartMapper SPARE_PART_MAPPER = new SparePartMapper();
    private static final String SELECT_ALL = "SELECT id, number, cost, name FROM parts";
    private static final String SELECT_SINGLE = "SELECT id, number, cost, name FROM parts WHERE name = ?";
    private static final String SELECT_SINGLE_ID = "SELECT id, number, cost, name FROM parts WHERE id = ?";
    private static final String INSERT_INTO = "INSERT INTO parts (number, cost, name) VALUES (?, ?, ?);";
    private static final String UPDATE = "UPDATE cars SET number = ?, cost = ?, name = ? WHERE id = ?;";
    private static final String REMOVE = "DELETE FROM parts WHERE id = ?;";
    private static final String CREATE_TABLE = "CREATE TABLE parts (id SERIAL, number int, cost int, name varchar(255));";
    private static final String DROP_TALBE = "DROP TABLE parts;";

    private Connection getConnection() throws SQLException, PSQLException {
        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/cars",
                "user", "pswd");
    }

    public void addSparePart(SparePart sparePart) throws SQLException {
        if(!checkTableExists()){
            createUsersTable();
        }
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_INTO);
            preparedStatement.setInt(1, sparePart.getNumber());
            preparedStatement.setInt(2, sparePart.getCost());
            preparedStatement.setString(3, sparePart.getName());
            preparedStatement.executeUpdate();
        }
    }

    public SparePart findSparePart(String name) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_SINGLE)) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();

            SparePart sparePart = null;
            while (resultSet.next()) {
                sparePart = SPARE_PART_MAPPER.mapResultSet(resultSet);
            }
            resultSet.close();
            return sparePart;
        }
    }

    public void delete() throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DROP_TALBE)) {
             preparedStatement.executeUpdate();
            createUsersTable();
        }
    }

    public SparePart selectById(int id) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_SINGLE_ID)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            SparePart sparePart = null;
            while (resultSet.next()) {
                sparePart = SPARE_PART_MAPPER.mapResultSet(resultSet);
            }
            resultSet.close();
            return sparePart;
        }
    }

    public List<SparePart> findAll() throws SQLException {
        if(!checkTableExists()){
            createUsersTable();
        }
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            List<SparePart> parts = new ArrayList<>();
            while (resultSet.next()) {
                SparePart sparePart = SPARE_PART_MAPPER.mapResultSet(resultSet);
                parts.add(sparePart);
            }
            return parts;
        }
    }

    public void update(SparePart sparePart, int id) throws SQLException {
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE);
            preparedStatement.setInt(1, sparePart.getNumber());
            preparedStatement.setInt(2, sparePart.getCost());
            preparedStatement.setString(3, sparePart.getName());
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
            ResultSet resultSet = databaseMetaData.getTables(null, null, "parts", null);
            if(resultSet.next()){
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
