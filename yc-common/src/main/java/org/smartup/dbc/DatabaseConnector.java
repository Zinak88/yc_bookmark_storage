package org.smartup.dbc;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartup.exception.DatabaseException;
import yandex.cloud.sdk.functions.Context;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

public class DatabaseConnector {
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseConnector.class);

    public Connection get (Context c) throws DatabaseException{
        Map<String, String> tokenMap = new Gson().fromJson(c.getTokenJson(), new TypeToken<Map<String, String>>() {}.getType());
        String token = tokenMap.get("access_token");
        LOGGER.info(" # Token is parsed: " + token);

        var proxyId = System.getenv().get("PROXY"); // Идентификатор подключения
        var proxyEndpoint = System.getenv().get("ENDPOINT");// Точка входа
        var user = System.getenv().get("USER"); // Пользователь БД
        LOGGER.info("# ProxyId: "+proxyId+". Endpoint: "+proxyEndpoint+". User: "+user+".");

        String url = String.format("jdbc:postgresql://%s/%s", proxyEndpoint, proxyId);
        var props = new Properties();
        props.setProperty("ssl", "false");
        props.setProperty("user", user);
        props.setProperty("password", token);

        LOGGER.info("# URl database proxy: "+url);
        Connection connection;
        try {
            connection = DriverManager.getConnection(url, props);
            LOGGER.info("# Connected");
        } catch (SQLException e) {
            LOGGER.error("# Cannot initiate connection", e);
            throw new DatabaseException(e);
        }
        return connection;
    }
}
