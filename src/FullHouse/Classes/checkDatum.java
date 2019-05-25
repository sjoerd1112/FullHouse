package FullHouse.Classes;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sjoer on 24-5-2019.
 */
public class checkDatum {
    public static String checkDateFormat(String date) {
        if (date.length() == 10) {
            if (date.charAt(2) == '-' && date.charAt(5) == '-') {
                String jaar = String.valueOf(date.charAt(6));
                jaar += String.valueOf(date.charAt(7));
                jaar += String.valueOf(date.charAt(8));
                jaar += String.valueOf(date.charAt(9));
                int jaarInt = Integer.parseInt(jaar);
                if (jaarInt < 0) {
                    return "incorrect";
                }

                String maand = String.valueOf(date.charAt(3));
                maand += String.valueOf(date.charAt(4));
                int maandInt = Integer.parseInt(maand);
                if (maandInt > 12 || maandInt < 1) {
                    return "incorrect";
                }

                String dag = String.valueOf(date.charAt(0));
                dag += String.valueOf(date.charAt(1));
                int dagInt = Integer.parseInt(dag);
                if (dagInt > 31 || dagInt < 1) {
                    return "incorrect";
                }

                Date parseDate = new Date(jaarInt - 1900, maandInt - 1, dagInt);
                String datum = new SimpleDateFormat("yyyy-MM-dd").format(parseDate);
                return datum;
            } else {
                return "incorrect";
            }
        } else {
            return "incorrect";
        }
    }
}
