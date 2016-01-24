package com.epam.advent_hotel.db.connection_pool;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Elizaveta Kapitonova on 13.01.16.
 */
public class Pool {

    public static final int GET_CONNECTION_MILLIS = 1000;
    public static final String PROPERTIES_PATH =
            "/media/kapikapi/Lisusha/Java/AdventHotel/src/main/webapp/WEB-INF/pool-config.properties";
    private static Pool INSTANCE;

    public static Pool getInstance() {
        if (INSTANCE == null)
            synchronized (Pool.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Pool();
                }
            }
        return INSTANCE;
    }

    private Map<Connection, Boolean> connections;

    private final String URL;
    private final String USER;
    private final String PASSWORD;

    private Pool() {
        Properties properties = new Properties();
        try {
            //properties.load(getClass().getResourceAsStream(PROPERTIES_PATH));
            properties.load(new FileInputStream(PROPERTIES_PATH));
            Class.forName(properties.getProperty("db.driver"));

            URL = properties.getProperty("db.url");
            USER = properties.getProperty("db.user");
            PASSWORD = properties.getProperty("db.password");

            int capacity = Integer.parseInt(properties.getProperty("db.poolsize"));
            connections = new HashMap<>(capacity);
            for (int i = 0; i < capacity; i++) {
                connections.put(createConnection(), true);
            }

        } catch (IOException | ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private PoolConnection createConnection() throws SQLException {
        return new PoolConnection(DriverManager.getConnection(URL, USER, PASSWORD), this);
    }

    public Connection getConnection() {
        Connection result = null;
        for (Map.Entry<Connection, Boolean> entry : connections.entrySet()) {
            if (entry.getValue()) {
                synchronized (this) {
                    if (entry.getValue()) {
                        Connection key = entry.getKey();
                        connections.put(key, false);
                        result = key;
                    }
                }
            }
        }

        try {
            Thread.sleep(GET_CONNECTION_MILLIS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return result == null ? getConnection() : result;
    }



    public void free(PoolConnection poolConnection) {
        connections.put(poolConnection, true);
    }
}
