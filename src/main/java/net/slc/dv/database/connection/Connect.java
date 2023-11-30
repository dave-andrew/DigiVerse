package net.slc.dv.database.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 * <strong>
 * --------------------------------------------------------------- <br>
 * | ISYS6197 - BUSINESS APPLICATION DEVELOPMENT | <br>
 * --------------------------------------------------------------- <br>
 * </strong>
 * <br>
 * Connect.java | This class is used for connection to MySQL database
 * <br>
 * Copyright 2019 - Bina Nusantara University
 * <br>
 * Software Laboratory Center | Laboratory Center Alam Sutera
 * <br>
 * Kevin Surya Wahyudi (SW16-2), All rights reserved.
 * <br>
 */
public final class Connect {

    private static Statement st;
    private static Connect connect;
    private final String USERNAME = "root"; // change with your MySQL username, the default username is 'root'
    private final String PASSWORD = ""; // change with your MySQL password, the default password is empty
    private final String DATABASE = "DigiVerse"; // change with the database name that you use
    private final String HOST = "localhost:3306"; // change with your MySQL host, the default port is 3306
    private final String CONECTION = String.format("jdbc:mysql://%s/%s", HOST, DATABASE);
    private Connection con;
    private ConnectionChecker connectionChecker;

    /**
     * Constructor for Connect class
     * <br>
     * This class is used singleton design pattern, so this class only have one instance
     */
    private Connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            System.out.println("Failed to load the driver!");
        }

        try {
            con = DriverManager.getConnection(CONECTION, USERNAME, PASSWORD);
            st = con.createStatement();
            System.out.println("Connected to the database!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to connect the database!");
        }
    }

    /**
     * This method is used for get instance from Connect class
     *
     * @return Connect This returns instance from Connect class
     */
    public static synchronized Connect getConnection() {
        /**
         * If the connect is null then:
         *   - Create the instance from Connect class
         *   - Otherwise, just assign the previous instance of this class
         */
        return connect = (connect == null) ? new Connect() : connect;
    }

    public void reconnect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(CONECTION, USERNAME, PASSWORD);
            st = con.createStatement();
        } catch (Exception e) {
            System.out.println("Failed to connect the database, the system is terminated!");
        }
    }

    public PreparedStatement prepareStatement(String query) {
        if (con != null) {
            PreparedStatement ps = null;
            try {
                ps = con.prepareStatement(query);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return ps;
        }
        return null;
    }

    public Connection getConnect() {
        return con;
    }
}
