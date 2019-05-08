package FullHouse.GUI;

/**
 * Created by sjoer on 8-5-2019.
 */

import java.sql.*;

public class Login {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://18148875@meru.hhs.nl","18148875","ad3pauheef");
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM aantal");
        while(rs.next()){
            System.out.println("Aantal: " + rs.getInt(1));
        }
        con.close();
    }
}
