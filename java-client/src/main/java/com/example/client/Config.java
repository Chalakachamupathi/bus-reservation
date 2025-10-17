package com.example.client;

import java.io.InputStream;
import java.util.Properties;

public class Config {
    private final Properties props = new Properties();

    public Config() {
        try (InputStream in = Config.class.getResourceAsStream("/application.properties")) {
            if (in != null) props.load(in);
        } catch (Exception ignored) {}
    }

    public String getBaseUrl() {
        return System.getProperty("baseUrl",
                System.getenv().getOrDefault("BASE_URL",
                        props.getProperty("baseUrl", "http://localhost:8080"))
        );
    }
}
