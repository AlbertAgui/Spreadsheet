package org.example;

import edu.upc.etsetb.arqsoft.spreadsheet.entities.BadCoordinateException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Translate_coordinate {
    public static NumCoordinate translateCellIdToCoordinateTo(String s_coordinate) {
        String pattern = "^([A-Z]+)(\\d+)$";

        Pattern regexPattern = Pattern.compile(pattern);
        Matcher matcher = regexPattern.matcher(s_coordinate);

        if (matcher.matches()) {
            String sn_column = matcher.group(1); //characters
            String sn_row = matcher.group(2);    //numbers
            int n_column, n_row;

            //System.out.println("Characters: " + sn_column + "\n");
            //System.out.println("sn_row: " + sn_row + "\n");

            n_column = 0;
            int alphabet_size = 26;
            int multiplier = 1;

            for (int i = sn_column.length() - 1; i >= 0; --i) {
                n_column = n_column + (((int) sn_column.charAt(i) - (int) 'A' + 1) * (multiplier));
                multiplier = multiplier * alphabet_size;
            }

            n_row = Integer.parseInt(sn_row);

            NumCoordinate numCoordinate = new NumCoordinate(n_row, n_column);
            return numCoordinate;
        }
        else {
            throw new BadCoordinateException("Incorrect cell format" + "s_coordinate");
        }
    }
}
