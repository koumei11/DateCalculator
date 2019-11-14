package com.example.DateManager.service;

import com.example.DateManager.NotExecuteAnyMoreException;
import com.example.DateManager.mapper.DateManageMapper;
import com.example.DateManager.utilities.Formula;
import com.example.DateManager.utilities.Type;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DateManageServiceImpl implements DateManageService {

    private DateManageMapper mapper;

    @Autowired
    public DateManageServiceImpl(DateManageMapper mapper){
        this.mapper = mapper;
    }

    @Override
    public List<Formula> getAll() {
        List<Formula> formulas = mapper.getAll();
        return formulas;
    }

    @Override
    public Formula getFormula(String dateId) {
        try {
            Formula formula = mapper.getFormula(dateId);
            return formula;
        } catch(EmptyResultDataAccessException e){
            throw new NotExecuteAnyMoreException("指定された日付IDは存在しません");
        }
    }

    @Override
    public void insertData(Formula formula, Type type) {
        mapper.insertType(type);
        formula.setTypeId(type.getId());
        mapper.insertFormula(formula);
    }

    @Override
    public void updateData(Formula formula){
        if(mapper.updateFormula(formula) == 0){
            throw new NotExecuteAnyMoreException("指定されたデータは存在しません");
        }

        if(mapper.updateType(formula.getType()) == 0){
            throw new NotExecuteAnyMoreException("指定されたデータは存在しません");
        }

    }

    @Override
    public void deleteData(Formula formula){
        if(mapper.deleteFormula(formula) == 0){
            throw new NotExecuteAnyMoreException("指定されたデータは存在しません");
        }
        if(mapper.deleteType(formula.getType()) == 0){
            throw new NotExecuteAnyMoreException("指定されたデータは存在しません");
        }
    }

    @Override
    public List<Map<String, Object>> getAllJsonData(){
        List<Map<String, Object>> maps = mapper.getAllJsonData();
        return maps;
    }

    @Override
    public Map<String, Object> getJsonData(String dateId){
        Map<String, Object> map = mapper.getJsonData(dateId);
        return map;
    }

}
