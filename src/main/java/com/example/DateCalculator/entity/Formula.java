package com.example.DateCalculator.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Formula {
    private int id;
    private int typeId;
    private Type type;
    private String resultDate;
    private int year;
    private int month;
    private int day;
}
