package com.fmss.api_gateway.config.logger.config;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class ConfigFileFactory {
    public ConfigFile createLoader() {
        if (isResourcePresent("application.yml")) {
            return new AppYml();
        } else if (isResourcePresent("application.properties")) {
            return new AppProperties();
        } else {
            throw new IllegalStateException("No valid configuration file found");
        }
    }

    private boolean isResourcePresent(String resourceName) {
        Resource resource = new ClassPathResource(resourceName);
        return resource.exists();
    }
}
