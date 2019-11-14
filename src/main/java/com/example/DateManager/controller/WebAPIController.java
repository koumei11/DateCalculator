package com.example.DateManager.controller;

import com.example.DateManager.service.DateManageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/datemanagerapi")
public class WebAPIController {

    private DateManageServiceImpl service;

    @Autowired
    public WebAPIController(DateManageServiceImpl service){
        this.service = service;
    }

    @GetMapping("/all")
    public List<Map<String, Object>> getAllJsonData(){
        List<Map<String, Object>> maps = service.getAllJsonData();
        return maps;
    }

    @GetMapping("/{id}")
    public Map<String, Object> getJsonData(@PathVariable String id){
        Map<String, Object> map = service.getJsonData(id);
        return map;
    }
}
