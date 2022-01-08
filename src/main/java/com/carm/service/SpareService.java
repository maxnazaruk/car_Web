package com.carm.service;

import com.carm.entity.SparePart;
import com.carm.jdbc.JdbcSparePartDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SpareService {

    private JdbcSparePartDao jdbcSparePartDao;

    public SpareService(JdbcSparePartDao jdbcSparePartDao) {
        this.jdbcSparePartDao = jdbcSparePartDao;
    }

    public List<SparePart> findAll() throws SQLException {
        return jdbcSparePartDao.findAll();
    }

    public List<String> getPartsByName() throws SQLException {
        List<String> parts = new ArrayList<>();
        for (SparePart sparePart : jdbcSparePartDao.findAll()){
            parts.add(sparePart.getName());
        }
        return parts;
    }

    public void add(SparePart sparePart) throws SQLException {
        jdbcSparePartDao.addSparePart(sparePart);
    }

    public SparePart findPartByName(String name) throws SQLException {
        return jdbcSparePartDao.findSparePart(name);
    }

    private int number;
    private int cost;
    private String name;

    public void generateSpareParts() throws SQLException {
        for (int i = 0; i < 10; i++) {
            SparePart sparePart = SparePart.builder()
                    .number((int)(Math.random() * 100) + 0)
                    .cost((int)(Math.random() * 2000) + 100)
                    .name(generateSparePartName())
                    .build();
            jdbcSparePartDao.addSparePart(sparePart);
        }
    }
    public static String generateSparePartName() {
        StringBuilder part = new StringBuilder();
        char c;
        for (int i = 0; i < 10; i++) {
            c = (char) ((Math.random() * 25) + 97);
            part.append(c);
        }

        return part.toString();
    }

    public void delete() throws SQLException {
        jdbcSparePartDao.delete();
    }
}
