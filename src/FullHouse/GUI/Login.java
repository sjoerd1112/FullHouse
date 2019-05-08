package FullHouse.GUI;

/**
 * Created by sjoer on 8-5-2019.
 */

import java.sql.*;

import static FullHouse.DBConnector.DBConnector.query;

public class Login {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        ResultSet rs = query("SELECT * FROM aantal");
        int i = 1;
        while(rs.next()){
            System.out.println(rs.getInt(i));
            i++;
        }
    }
}
