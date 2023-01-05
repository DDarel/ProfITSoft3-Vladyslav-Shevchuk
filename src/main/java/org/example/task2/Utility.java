package org.example.task2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class Utility {
    public static <T>T loadFromProperties(Class<T> _class, File propertiesPath) throws FileNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Properties properties = new Properties();

        try(FileInputStream file = new FileInputStream(propertiesPath)) {
            properties.load(file);

            if (properties.isEmpty()) {
                throw new Exception("File is empty");
            }
        } catch (Exception e) {
            throw new FileNotFoundException("File " + propertiesPath.getPath() + " not found");
        }

        T object = null;
        object = _class.getDeclaredConstructor().newInstance();

        Field[] fields = _class.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            field.set(object, getValue(field, properties));
            field.setAccessible(false);
        }
        return object;
    }
    private static Object getValue(Field field, Properties properties) {
        String key = field.getName();

        if (field.isAnnotationPresent(Property.class)) {
            String propertyName = field.getAnnotation(Property.class).name().trim();

            if (!propertyName.isEmpty()) {
                key = propertyName;
            }
        }

        String propertyValue = properties.getProperty(key);
        return parseProperty(field, propertyValue);
    }
    private static Object parseProperty(Field field, String propertyValue) {
        Object value = null;

        Class<?> fieldClass = field.getType();

        if (fieldClass.isAssignableFrom(String.class)) {
            value = propertyValue;
        }

        if (fieldClass.isAssignableFrom(Integer.class) || fieldClass.isAssignableFrom(int.class)) {
            value = Integer.parseInt(propertyValue);
        }

        if (fieldClass.isAssignableFrom(Instant.class)) {
            String time = "";

            if (field.isAnnotationPresent(Property.class)) {
                time = field.getAnnotation(Property.class).timeFormat().trim();
            }

            value = time.isEmpty()
                    ? Instant.parse(propertyValue)
                    : LocalDateTime.parse(propertyValue, DateTimeFormatter.ofPattern(time))
                    .atZone(ZoneId.systemDefault())
                    .toInstant();
        }
        return value;
    }
}