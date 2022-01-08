package com.carm.service;

import com.carm.entity.Vehicle;
import com.carm.entity.Worker;
import com.carm.jdbc.JdbcWorkerDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WorkerService {

    private JdbcWorkerDao jdbcWorkerDao;

    public WorkerService(JdbcWorkerDao jdbcWorkerDao) {
        this.jdbcWorkerDao = jdbcWorkerDao;
    }

    public List<Worker> findAll() throws SQLException {
        return jdbcWorkerDao.findAll();
    }

    public void add(Worker worker) {
        try {
            jdbcWorkerDao.addWorker(worker);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public List<String> findAllWorkers() throws SQLException {
        List<String> workers = new ArrayList<>();
        for (Worker worker : findAll()){
            workers.add(worker.getFirstName());
        }
        return workers;
    }

    public Worker findWorker(String fname) throws SQLException {
        return jdbcWorkerDao.findWorker(fname);
    }

    public void generateWorker() throws SQLException {
        for (int q = 0; q < 5; q++) {
            Worker worker = Worker.builder()
                            .firstName(generateName())
                    .lastName(generateName())
                    .build();
            jdbcWorkerDao.addWorker(worker);
        }
    }

    public String generateName(){
        StringBuilder name = new StringBuilder();
        char c;
        for (int i = 0; i < 5; i++) {
            c = (char) ((Math.random() * 25) + 97);
            name.append(c);
        }

        return name.toString();
    }

    public void delete() throws SQLException {
        jdbcWorkerDao.delete();
    }
}
