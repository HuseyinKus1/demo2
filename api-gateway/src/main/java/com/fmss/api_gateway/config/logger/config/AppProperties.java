package com.fmss.api_gateway.config.logger.config;


import com.fmss.api_gateway.config.logger.Level;

import java.io.InputStream;
import java.util.Properties;

public class AppProperties implements ConfigFile {
    public Level getLoggingLevelFromClasspath() {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find application.properties");
                return Level.INFO;
            }
            props.load(input);
            return Level.valueOf(props.getProperty("custom.logging.level"));
        } catch (Exception ex) {
            ex.printStackTrace();
            return Level.INFO;
        }
    }
}
