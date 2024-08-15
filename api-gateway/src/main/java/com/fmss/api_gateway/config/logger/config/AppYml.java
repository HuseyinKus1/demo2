package com.fmss.api_gateway.config.logger.config;

import com.fmss.api_gateway.config.logger.Level;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class AppYml implements ConfigFile {
    public Level getLoggingLevelFromClasspath() {
        Yaml yaml = new Yaml();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.yml")) {
            if (input == null) {
                System.out.println("Sorry, unable to find application.yml");
                return Level.INFO;
            }

            Map<String, Object> obj = yaml.load(input);
            String loggingLevel = extractLoggingLevel(obj);

            return (loggingLevel != null) ? Level.valueOf(loggingLevel.toUpperCase()) : Level.INFO;
        } catch (Exception ex) {
            ex.printStackTrace();
            return Level.INFO;
        }
    }

    public String extractLoggingLevel(Map<String, Object> yamlMap) {
        if (yamlMap.containsKey("custom")) {
            Map<String, Object> customMap = (Map<String, Object>) yamlMap.get("custom");
            if (customMap.containsKey("logging")) {
                Map<String, Object> loggingMap = (Map<String, Object>) customMap.get("logging");
                if (loggingMap.containsKey("level")) {
                    return (String) loggingMap.get("level");
                }
            }
        }
        return null;
    }
}
