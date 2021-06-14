package com.parkit.parkingsystem.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesConfig {

    private Properties propertiesOfApp = new Properties();

    public PropertiesConfig(Properties properties) {

        String appConfigPath = "src/main/resources/config.properties";
        try {
            FileInputStream in = new FileInputStream(appConfigPath);
            this.propertiesOfApp.load(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //returns the file with properties to use in another class
    public String getProperty(String key) {
        return this.propertiesOfApp.getProperty(key);
    }

}
