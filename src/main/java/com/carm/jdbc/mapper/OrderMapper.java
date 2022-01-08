package com.carm.jdbc.mapper;

import com.carm.entity.Order;
import com.carm.entity.SparePart;
import com.carm.entity.Vehicle;
import com.carm.entity.Worker;
import com.carm.jdbc.JdbcSparePartDao;
import com.carm.jdbc.JdbcVehicleDao;
import com.carm.jdbc.JdbcWorkerDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class OrderMapper {

    private JdbcVehicleDao jdbcVehicleDao = new JdbcVehicleDao();
    private JdbcSparePartDao jdbcSparePartDao = new JdbcSparePartDao();
    private JdbcWorkerDao jdbcWorkerDao = new JdbcWorkerDao();

    public Order mapResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        Vehicle vehicle = jdbcVehicleDao.findVehicle(resultSet.getString("vin"));
        SparePart sparePart = jdbcSparePartDao.findSparePart(resultSet.getString("sparePart"));
        int complexity = resultSet.getInt("complexity");
        Worker worker = jdbcWorkerDao.findWorker(resultSet.getString("fname"));
        LocalDateTime startDate = resultSet.getTimestamp("start").toLocalDateTime();
        LocalDateTime endDate = resultSet.getTimestamp("finish").toLocalDateTime();
        int price = resultSet.getInt("price");

        Order order = Order.builder()
                .id(id)
                .vehicle(vehicle)
                .sparePart(sparePart)
                .complexity(complexity)
                .worker(worker)
                .startDate(startDate)
                .endDate(endDate)
                .price(price)
                .build();


        return order;
    }
}
