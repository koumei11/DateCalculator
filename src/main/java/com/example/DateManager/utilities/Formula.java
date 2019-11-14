package com.example.DateManager.utilities;

import com.example.DateManager.form.DateInputForm;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

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
