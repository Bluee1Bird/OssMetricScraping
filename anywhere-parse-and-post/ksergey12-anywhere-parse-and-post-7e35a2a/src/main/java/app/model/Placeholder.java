package app.model;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


public class Placeholder {
    private String properties;
    private String title;
    private String excerpt;
    private String description;
    private String image;

    public Placeholder(String properties) {
        this.properties = properties;
    }

    public Placeholder invoke() {
        FileInputStream fis;
        Properties property = new Properties();

        try {
            fis = new FileInputStream(properties);
            property.load(fis);
        } catch (IOException e) {
            System.err.println("ОШИБКА: Файл свойств отсуствует!");
        }

        title = property.getProperty("title");
        excerpt = property.getProperty("excerpt");
        description = property.getProperty("description");
        image = property.getProperty("image");

        return this;
    }

    public String getTitle() {
        return title;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }
}
