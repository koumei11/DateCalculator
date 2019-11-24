package com.example.DateCalculator.mapper;

import com.example.DateCalculator.entity.Formula;
import com.example.DateCalculator.entity.Type;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DateCalculatorMapper {
    List<Formula> getAll();
    Formula getFormula(String dateId);
    void insertFormula(Formula formula);
    void insertType(Type type);
    int updateFormula(Formula formula);
    int updateType(Type type);
    int deleteFormula(Formula formula);
    int deleteType(Type type);
    List<Map<String, Object>> getAllJsonData();
    Map<String, Object> getJsonData(String dateId);
}
