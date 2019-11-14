package com.example.DateManager.mapper;

import com.example.DateManager.utilities.Formula;
import com.example.DateManager.utilities.Type;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DateManageMapper {
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
