package com.carm.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
public class Vehicle {
    private int id;
    private String model;
    private String owner;

    @Override
    public String toString() {
        return VIN;
    }

    private String VIN;
}
