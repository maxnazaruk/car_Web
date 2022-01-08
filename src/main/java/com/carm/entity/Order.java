package com.carm.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@ToString
@Getter
@Setter
@Builder
public class Order {
    private int id;
    private Vehicle vehicle;
    private SparePart sparePart;
    private int complexity;
    private Worker worker;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int price;
}
