package com.parkit.parkingsystem.util;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesConfig {

    private Properties propertiesOfApp = new Properties();

    public PropertiesConfig(Properties properties) {
        String appConfigPath = "src\\main\\resources\\config.properties";
        try {
            this.propertiesOfApp.load(new FileInputStream(appConfigPath));
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public String getProperty(String key) {
        return this.propertiesOfApp.getProperty(key);
    }
}