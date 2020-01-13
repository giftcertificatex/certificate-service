package com.giftok.certificate;

import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public class PropertiesHolder {
    private static final String propFileName = "application.properties";
    private static Properties properties = new Properties();
    static {
        try (InputStream is = PropertiesHolder.class.getClassLoader().getResourceAsStream(propFileName)) {
            properties.load(Objects.requireNonNull(is));
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
