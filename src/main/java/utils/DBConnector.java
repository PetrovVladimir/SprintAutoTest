package utils;

import application.ApplicationConfig;
import logic.database.DataBaseService;
import logic.database.DataBaseWrapper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnector {
    private static DBConnector ourInstance = new DBConnector();

    public Connection connection;
    public Statement statement;

    public DataBaseService dataBaseService;

    public static DBConnector connector() {
        return ourInstance;
    }

    private DBConnector() {
        try {
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            connection = DriverManager.getConnection("jdbc:oracle:thin:@" +
                                                        ApplicationConfig.getDb_url() + ":1521:" +
                                                        ApplicationConfig.getDb_sid(),
                                                        ApplicationConfig.getDb_user(),
                                                        ApplicationConfig.getDb_user_pass());
            connection.setAutoCommit(true);
            statement = connection.createStatement();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        dataBaseService = new DataBaseWrapper();
    }
}
