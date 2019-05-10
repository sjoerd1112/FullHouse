package FullHouse.Classes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import static FullHouse.DB.DBConnector.query;
import static FullHouse.DB.DBConnector.updateQuery;

/**
 * Created by sjoer on 10-5-2019.
 */
public class addSpeler {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Vul gebruikersnaam in: ");
        String gebruikersnaam = scanner.nextLine();
        System.out.println("Vul wachtwoord in: ");
        String wachtwoord = scanner.nextLine();
        int wachtwoordHash = wachtwoord.hashCode();
        String query = ("SELECT gebruikersnaam FROM login");

        ResultSet rs = query(query);
        boolean bestaatAl = false;
        while(rs.next()){
            if(rs.getString(1).equals(gebruikersnaam)){
                System.out.println("Gebruiker "+gebruikersnaam+" bestaat al");
                bestaatAl = true;
            }
        }
        if(!bestaatAl){
            query = ("INSERT INTO `login` (`ID`, `Gebruikersnaam`, `Wachtwoord`) VALUES (NULL, '"+gebruikersnaam+"', '"+wachtwoordHash+"')");
            if(updateQuery(query) == 1){
                System.out.println("Nieuwe gebruiker "+gebruikersnaam+" aangemaakt.");
            }
        }
    }
}
