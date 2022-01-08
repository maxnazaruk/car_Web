package com.carm.jdbc.mapper;

import com.carm.entity.Worker;

import java.sql.ResultSet;
import java.sql.SQLException;

public class WorkerMapper {
    public Worker mapResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String fname = resultSet.getString("fname");
        String lname = resultSet.getString("lname");

        Worker worker = Worker.builder().
                id(id)
                .firstName(fname)
                .lastName(lname)
                .build();

        return worker;
    }
}
