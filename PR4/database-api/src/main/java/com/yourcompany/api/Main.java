package com.yourcompany.api;

import com.yourcompany.api.impl.MyDatabaseConnector;

public class Main {
    public static void main(String[] args) {
        // Создаем реализацию соединителя с БД
        DatabaseAPI.DatabaseConnector connector = new MyDatabaseConnector();
        
        // Инициализируем API
        DatabaseAPI api = new DatabaseAPI(connector);
        
        try {
            // Подключаем пользователя
            api.connectUser();
            
            // Пример работы с API
            Object data1 = api.loadData("config_settings");
            Object data2 = api.loadData("user_preferences");
            api.saveData("user_preferences", "new value");
            Object report = api.generateReport("monthly_report");
            api.refreshData();
            
        } finally {
            // Отключаем пользователя
            api.disconnectUser();
        }
    }
}