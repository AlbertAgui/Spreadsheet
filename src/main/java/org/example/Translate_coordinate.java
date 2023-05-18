package org.example;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Translate_coordinate {
    public static Boolean is_correct_coordinate(String s_coordinate) {
        System.out.println("Translate input: " + s_coordinate + "\n");
        String pattern = "^([A-Z]+)(\\d+)$";

        Pattern regexPattern = Pattern.compile(pattern);
        Matcher matcher = regexPattern.matcher(s_coordinate);

        if (matcher.matches()) {
            String characters = matcher.group(1);
            String numbers = matcher.group(2);

            System.out.println("Characters: " + characters + "\n");
            System.out.println("Numbers: " + numbers + "\n");
            return true;
        }
        else {
            System.out.println("Incorrect format\n");
            return false;
        }
    }

    public static Num_coordinate translate_coordinate_to_int(String s_coordinate) {
        String pattern = "^([A-Z]+)(\\d+)$";

        Pattern regexPattern = Pattern.compile(pattern);
        Matcher matcher = regexPattern.matcher(s_coordinate);

        if (matcher.matches()) {
            Num_coordinate num_coordinate = new Num_coordinate();
            String sn_column = matcher.group(1); //characters
            String sn_row = matcher.group(2);    //numbers
            int n_column;
            int n_row;

            //System.out.println("Characters: " + sn_column + "\n");
            System.out.println("sn_row: " + sn_row + "\n");

            n_column = 0;
            int alphabet_size = 26;
            int multiplier = 1;
            for(int i = sn_column.length() - 1; i >= 0; --i) {
                n_column = n_column + (((int) sn_column.charAt(i) - (int) 'A' + 1) * (multiplier));
                System.out.println("n_column: " + n_column + "\n");
                System.out.println("char_value: " + ((int) sn_column.charAt(i) - (int) 'A') + "\n");
                System.out.println("multiplier: " + multiplier + "\n");
                multiplier = multiplier * alphabet_size;
            }

            n_row = Integer.parseInt(sn_row);
            num_coordinate.num_column = n_column;
            num_coordinate.num_row = n_row;

            return num_coordinate;
        }
        else {
            System.out.println("Incorrect format\n");
            return null;
        }
    }
}
