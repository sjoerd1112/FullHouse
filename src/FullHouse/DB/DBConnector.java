package FullHouse.DB;

import java.sql.*;

/**
 * Created by sjoer on 8-5-2019.
 */
public class DBConnector {
    private static Connection con;
    private static Statement stmt;

    public static ResultSet query(String query) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://meru.hhs.nl:3306/18148875", "18148875", "ad3pauheef");
        stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        return rs;
    }

    public static int updateQuery(String query) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://meru.hhs.nl:3306/18148875", "18148875", "ad3pauheef");
        Statement stmt = con.createStatement();
        int rs = stmt.executeUpdate(query);
        return rs;
    }
}