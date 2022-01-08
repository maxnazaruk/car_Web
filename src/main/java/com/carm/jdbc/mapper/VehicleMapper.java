package com.carm.jdbc.mapper;

import com.carm.entity.Vehicle;
import com.carm.entity.Worker;

import java.sql.ResultSet;
import java.sql.SQLException;

public class VehicleMapper {
    public Vehicle mapResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String model = resultSet.getString("model");
        String owner = resultSet.getString("owner");
        String VIN = resultSet.getString("vin");

        Vehicle vehicle = Vehicle.builder().
                id(id)
                .model(model)
                .owner(owner)
                .VIN(VIN)
                .build();

        return vehicle;
    }
}
