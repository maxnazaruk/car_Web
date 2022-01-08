package com.carm.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
public class SparePart {
    private int id;
    private int number;
    private int cost;
    private String name;

    public void addSparePart(){
        number++;
    }

    public void removeSparePart(){
        number--;
    }

    @Override
    public String toString() {
        return name;
    }
}
