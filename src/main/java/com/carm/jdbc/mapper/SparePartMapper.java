package com.carm.jdbc.mapper;

import com.carm.entity.SparePart;
import com.carm.entity.Vehicle;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SparePartMapper {
    public SparePart mapResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        int number = resultSet.getInt("number");
        int cost = resultSet.getInt("cost");
        String name = resultSet.getString("name");

       SparePart sparePart = SparePart.builder().
               id(id)
               .number(number)
               .cost(cost)
               .name(name)
               .build();


        return sparePart;
    }
}
