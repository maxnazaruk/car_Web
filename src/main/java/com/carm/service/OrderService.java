package com.carm.service;

import com.carm.entity.Order;
import com.carm.entity.SparePart;
import com.carm.entity.Vehicle;
import com.carm.entity.Worker;
import com.carm.jdbc.JdbcOrderDao;
import com.carm.jdbc.JdbcSparePartDao;
import com.carm.jdbc.JdbcVehicleDao;
import com.carm.jdbc.JdbcWorkerDao;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderService {
    private JdbcOrderDao jdbcOrderDao;
    private JdbcVehicleDao jdbcVehicleDao;
    private JdbcSparePartDao jdbcSparePartDao;
    private JdbcWorkerDao jdbcWorkerDao;

    public OrderService(JdbcOrderDao jdbcOrderDao, JdbcVehicleDao jdbcVehicleDao, JdbcSparePartDao jdbcSparePartDao, JdbcWorkerDao jdbcWorkerDao) {
        this.jdbcOrderDao = jdbcOrderDao;
        this.jdbcVehicleDao = jdbcVehicleDao;
        this.jdbcSparePartDao = jdbcSparePartDao;
        this.jdbcWorkerDao = jdbcWorkerDao;
    }

    public void add(Order order) throws SQLException {
        jdbcOrderDao.addOrder(order);
    }

    public List<Order> findAll() throws SQLException {
        return jdbcOrderDao.findAll();
    }

    public List<Integer> findAllOrdersById() throws SQLException {
        List<Integer> orders = new ArrayList<>();

        for (Order order : jdbcOrderDao.findAll()){
            orders.add(order.getId());
        }
        return orders;
    }

    public void removeorder(String id) throws SQLException {
        jdbcOrderDao.remove(Integer.parseInt(id));
    }


    public void addRandomOrders() throws SQLException {
        for (int i = 0; i < 20; i++) {
            Order order = Order.builder()
                    .vehicle(jdbcVehicleDao.selectVehicleById(((int)(Math.random() * 10) + 1)))
                    .sparePart(jdbcSparePartDao.selectById(((int)(Math.random() * 10) + 1)))
                    .complexity((int)(Math.random() * 5) + 1)
                    .worker(jdbcWorkerDao.selectById(((int)(Math.random() * 5) + 1)))
                    .startDate(LocalDateTime.now())
                    .endDate(LocalDateTime.now())
                    .price(((int)(Math.random() * 5000) + 100))
                    .build();
            jdbcOrderDao.addOrder(order);
        }
    }

    public void deleteData() throws SQLException {
            jdbcOrderDao.delete();
    }

}
