package com.carm.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
public class Worker {
    private int id;
    private String firstName;
    private String lastName;

    @Override
    public String toString() {
        return firstName;
    }
}
