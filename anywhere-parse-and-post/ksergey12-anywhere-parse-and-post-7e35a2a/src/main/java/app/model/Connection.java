package app.model;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


public class Connection {
    private String properties;
    private String username;
    private String password;
    private String xmlRpcUrl;

    public Connection(String properties) {
        this.properties = properties;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getXmlRpcUrl() {
        return xmlRpcUrl;
    }

    public Connection invoke() {
        FileInputStream fis;
        Properties property = new Properties();

        try {
            fis = new FileInputStream(properties);
            property.load(fis);
        } catch (IOException e) {
            System.err.println("ОШИБКА: Файл свойств отсуствует!");
        }

        username = property.getProperty("username");
        password = property.getProperty("password");
        xmlRpcUrl = property.getProperty("xmlRpcUrl");

        return this;
    }
}
