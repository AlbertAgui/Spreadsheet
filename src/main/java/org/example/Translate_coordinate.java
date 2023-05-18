package org.example;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Translate_coordinate {
    public static Boolean translate(String s_coordinate) {
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
}
