package com.yourcompany.api.impl;

import com.yourcompany.api.DatabaseAPI;

public class MyDatabaseConnector implements DatabaseAPI.DatabaseConnector {
    @Override
    public Object fetchData(String dataId) {
        // Реальная реализация получения данных из БД
        System.out.println("Fetching data from DB: " + dataId);
        return "Data for " + dataId;
    }
    
    @Override
    public void storeData(String dataId, Object data) {
        // Реальная реализация сохранения данных в БД
        System.out.println("Storing data to DB: " + dataId + ", value: " + data);
    }
    
    @Override
    public boolean isReadOnly(String dataId) {
        // Пример: считаем данные read-only если их ID содержит "config"
        return dataId.contains("config");
    }
    
    @Override
    public Object generateReport(String reportId) {
        // Реальная реализация формирования отчета
        System.out.println("Generating report: " + reportId);
        return "Report for " + reportId;
    }
}