package com.example.DateCalculator.service;

import com.example.DateCalculator.TaskNotFoundException;
import com.example.DateCalculator.mapper.DateCalculatorMapper;
import com.example.DateCalculator.entity.Formula;
import com.example.DateCalculator.entity.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class DateCalculatorServiceImpl implements DateCalculatorService {

    private DateCalculatorMapper mapper;

    @Autowired
    public DateCalculatorServiceImpl(DateCalculatorMapper mapper){
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public List<Formula> getAll() {
        List<Formula> formulas = mapper.getAll();
        return formulas;
    }

    @Override
    @Transactional
    public Formula getFormula(String dateId) {
        try {
            Formula formula = mapper.getFormula(dateId);
            return formula;
        } catch(EmptyResultDataAccessException e){
            throw new TaskNotFoundException("指定された日付IDは存在しません");
        }
    }

    @Override
    @Transactional
    public void insertData(Formula formula, Type type) {
        mapper.insertType(type);
        formula.setTypeId(type.getId());
        mapper.insertFormula(formula);
    }

    @Override
    @Transactional
    public void updateData(Formula formula){
        if(mapper.updateFormula(formula) == 0){
            throw new TaskNotFoundException("指定されたデータは存在しません");
        }

        if(mapper.updateType(formula.getType()) == 0){
            throw new TaskNotFoundException("指定されたデータは存在しません");
        }

    }

    @Override
    @Transactional
    public void deleteData(Formula formula){
        if(mapper.deleteFormula(formula) == 0){
            throw new TaskNotFoundException("指定されたデータは存在しません");
        }
        if(mapper.deleteType(formula.getType()) == 0){
            throw new TaskNotFoundException("指定されたデータは存在しません");
        }
    }

    @Override
    @Transactional
    public List<Map<String, Object>> getAllJsonData(){
        List<Map<String, Object>> maps = mapper.getAllJsonData();
        return maps;
    }

    @Override
    @Transactional
    public Map<String, Object> getJsonData(String dateId){
        Map<String, Object> map = mapper.getJsonData(dateId);
        return map;
    }
}
