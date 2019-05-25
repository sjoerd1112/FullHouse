package FullHouse.GUI;

import FullHouse.DB.DBConnector;
import java.sql.SQLException;

/**
 * Created by sjoer on 13-5-2019.
 */
public class Start {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        DBConnector.startConnection();
        Login.showLogin();
    }
}