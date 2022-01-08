package com.carm.jdbc;

import com.carm.entity.Vehicle;
import com.carm.entity.Worker;
import com.carm.jdbc.mapper.WorkerMapper;
import org.postgresql.util.PSQLException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcWorkerDao {
    private static final WorkerMapper WORK_MAPPER = new WorkerMapper();
    private static final String SELECT_ALL = "SELECT id, fname, lname FROM workers";
    private static final String SELECT_SINGLE = "SELECT id, fname, lname FROM workers WHERE fname = ?";
    private static final String SELECT_SINGLE_ID = "SELECT id, fname, lname FROM workers WHERE id = ?";
    private static final String INSERT_INTO = "INSERT INTO workers (fname, lname) VALUES (?, ?);";
    private static final String UPDATE = "UPDATE workers SET fname = ?, lname = ? WHERE id = ?;";
    private static final String REMOVE = "DELETE FROM workers WHERE id = ?;";
    private static final String CREATE_TABLE = "CREATE TABLE workers (id SERIAL, fname varchar(255), lname varchar(255));";
    private static final String DROP_TABLE = "DROP TABLE workers;";

    private Connection getConnection() throws SQLException, PSQLException {
        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/cars",
                "user", "pswd");
    }

    public void addWorker(Worker worker) throws SQLException {
        if(!checkTableExists()){
            createUsersTable();
        }
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_INTO);
            preparedStatement.setString(1, worker.getFirstName());
            preparedStatement.setString(2, worker.getLastName());
            preparedStatement.executeUpdate();
        }
    }

    public List<Worker> findAll() throws SQLException {
        if(!checkTableExists()){
            createUsersTable();
        }
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            List<Worker> workers = new ArrayList<>();
            while (resultSet.next()) {
                Worker worker = WORK_MAPPER.mapResultSet(resultSet);
                workers.add(worker);
            }
            return workers;
        }
    }

    public void update(Worker worker, int id) throws SQLException {
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE);
            preparedStatement.setString(1, worker.getFirstName());
            preparedStatement.setString(2, worker.getLastName());
            preparedStatement.setInt(3, id);
            preparedStatement.executeUpdate();
        }
    }

    public Worker findWorker(String name) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_SINGLE)) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();

            Worker worker = null;
            while (resultSet.next()) {
                worker = WORK_MAPPER.mapResultSet(resultSet);
            }
            resultSet.close();
            return worker;
        }
    }

    public void delete() throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DROP_TABLE)) {
           preparedStatement.executeUpdate();
            createUsersTable();
        }
    }

    public Worker selectById(int id) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_SINGLE_ID)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            Worker worker = null;
            while (resultSet.next()) {
                worker = WORK_MAPPER.mapResultSet(resultSet);
            }
            resultSet.close();
            return worker;
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
            System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private  boolean checkTableExists() {
        try (Connection connection = getConnection();
        ) {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            ResultSet resultSet = databaseMetaData.getTables(null, null, "workers", null);
            if(resultSet.next()){
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
