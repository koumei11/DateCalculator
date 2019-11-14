package com.example.DateManager.service;

import com.example.DateManager.utilities.Formula;
import com.example.DateManager.utilities.Type;

import java.util.List;
import java.util.Map;

public interface DateManageService {
    List<Formula> getAll();
    Formula getFormula(String dateId);
    void insertData(Formula formula, Type type);
    void updateData(Formula formula);
    void deleteData(Formula formula);
    List<Map<String, Object>> getAllJsonData();
    Map<String, Object> getJsonData(String dateId);
}
