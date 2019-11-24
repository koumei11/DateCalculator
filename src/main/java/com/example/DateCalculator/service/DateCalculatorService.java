package com.example.DateCalculator.service;

import com.example.DateCalculator.entity.Formula;
import com.example.DateCalculator.entity.Type;

import java.util.List;
import java.util.Map;

public interface DateCalculatorService {
    List<Formula> getAll();
    Formula getFormula(String dateId);
    void insertData(Formula formula, Type type);
    void updateData(Formula formula);
    void deleteData(Formula formula);
    List<Map<String, Object>> getAllJsonData();
    Map<String, Object> getJsonData(String dateId);
}
