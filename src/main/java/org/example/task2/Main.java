package org.example.task2;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        File propertiesFile = new File("src/main/resources/prop.properties");
            Movie person = Utility.loadFromProperties(Movie.class, propertiesFile);
            System.out.println(person);
    }
}
