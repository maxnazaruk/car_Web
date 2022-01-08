package com.carm.jdbc;

import com.carm.entity.Order;
import com.carm.entity.SparePart;
import com.carm.jdbc.mapper.OrderMapper;
import com.carm.jdbc.mapper.SparePartMapper;
import org.postgresql.util.PSQLException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcOrderDao {
    private static final OrderMapper ORDER_MAPPER = new OrderMapper();
    private static final String SELECT_ALL = "SELECT id, vin, sparePart, complexity, fname, start, finish, price FROM orders";
    private static final String SELECT_SINGLE = "SELECT id, vin, sparePart, complexity, fname, start, finish, price FROM orders WHERE vin = ?";
    private static final String INSERT_INTO = "INSERT INTO orders (vin, sparePart, complexity, fname, start, finish, price) VALUES (?, ?, ?, ?, ?, ?, ?);";
    private static final String UPDATE = "UPDATE orders SET vin = ?, sparePart = ?, complexity = ?, fname = ?, start = ?, finish = ?, price = ? WHERE id = ?;";
    private static final String REMOVE = "DELETE FROM orders WHERE id = ?;";
    private static final String CREATE_TABLE = "CREATE TABLE orders (id SERIAL, vin varchar(255), sparePart varchar(255), complexity int, fname varchar(255), start DATE, finish DATE, price int);";
    private static final String DROP_TABLE = "DROP TABLE orders";

    private Connection getConnection() throws SQLException, PSQLException {
        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/cars",
                "user", "pswd");
    }

    public void addOrder(Order order) throws SQLException {
        if(!checkTableExists()){
            createOrdersTable();
        }
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_INTO);
            preparedStatement.setString(1, order.getVehicle().getVIN());
            preparedStatement.setString(2, order.getSparePart().getName());
            preparedStatement.setInt(3, order.getComplexity());
            preparedStatement.setString(4, order.getWorker().getFirstName());
            preparedStatement.setTimestamp(5, Timestamp.valueOf(order.getStartDate()));
            preparedStatement.setTimestamp(6, Timestamp.valueOf(order.getEndDate()));
            preparedStatement.setInt(7, order.getPrice());
            preparedStatement.executeUpdate();
        }
    }

    public Order findOrder(String vin) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_SINGLE)) {
            preparedStatement.setString(1, vin);
            ResultSet resultSet = preparedStatement.executeQuery();

            Order order = null;
            while (resultSet.next()) {
                order = ORDER_MAPPER.mapResultSet(resultSet);
            }
            resultSet.close();
            return order;
        }
    }

    public void delete() throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DROP_TABLE)) {
            preparedStatement.executeUpdate();
            createOrdersTable();
        }
    }

    public List<Order> findAll() throws SQLException {
        if(!checkTableExists()){
            createOrdersTable();
        }
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            List<Order> orders = new ArrayList<>();
            while (resultSet.next()) {
                Order order = ORDER_MAPPER.mapResultSet(resultSet);
                orders.add(order);
            }
            return orders;
        }
    }

    public void update(Order order, int id) throws SQLException {
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE);
            preparedStatement.setString(1, order.getVehicle().getVIN());
            preparedStatement.setString(2, order.getSparePart().getName());
            preparedStatement.setInt(3, order.getComplexity());
            preparedStatement.setString(4, order.getWorker().getFirstName());
            preparedStatement.setTimestamp(5, Timestamp.valueOf(order.getStartDate()));
            preparedStatement.setTimestamp(6, Timestamp.valueOf(order.getEndDate()));
            preparedStatement.setInt(7, order.getPrice());
            preparedStatement.setInt(8, id);
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

    private void createOrdersTable() {
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
            ResultSet resultSet = databaseMetaData.getTables(null, null, "orders", null);
            if(resultSet.next()){
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
