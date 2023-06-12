package org.example;


import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Commands {
    public static String removeFirstWord(String inputString) {
        // Split the string into words
        String[] words = inputString.split("\\s+");

        // Check if there are at least two words
        if (words.length > 1) {
            // Join all words except the first one
            String result = String.join(" ", Arrays.copyOfRange(words, 1, words.length));
            return result;
        } else {
            return null;
        }
    }

    public static String[] E(String userCommand) {
        String flag = "edit cell";
        userCommand = removeFirstWord(userCommand);
        String pattern = "([A-Z]+[0-9]+)\\s(.+)";
        Matcher matcher = Pattern.compile(pattern).matcher(userCommand);
        if (matcher.matches()) {
            String cellCoordinate = matcher.group(1);
            String content = matcher.group(2);
            Controller.editCell(cellCoordinate, content);
            return new String[]{flag, cellCoordinate, content};
        } else {
            System.out.println("error");
            return null;
        }
    }

    public static String[] RF(String userCommand) {
        String flag = "read_file_command";
        userCommand = removeFirstWord(userCommand);
        String pattern = "^([a-zA-Z]:)?(/[a-zA-Z0-9_.-]+)+/?$";
        Matcher matcher = Pattern.compile(pattern).matcher(userCommand);
        if (matcher.matches()) {
            return new String[]{flag, matcher.group(0)};
        } else {
            System.out.println("error");
            return null;
        }
    }

    public static String[] C() {
        String flag = "create spreadsheet";
        Controller.createEmptySpreadsheet();
        return new String[]{flag};
    }

    public static String[] L(String userCommand) {
        userCommand = removeFirstWord(userCommand);
        String flag = "Load";
//        String pattern = "^([a-zA-Z]:)?(/[a-zA-Z0-9_.-]+)+/?$";
        String pattern = "[\\w\\d]+\\/[^\\/]+\\.[\\w\\d]+";
        Matcher matcher = Pattern.compile(pattern).matcher(userCommand);
        if (matcher.matches()) {
            String path = matcher.group(0);
            Controller.loadSpreadsheet(path);
            return new String[]{flag, matcher.group(0)};
        } else {
            System.out.println("error");
            return null;
        }
    }

    public static String[] S(String userCommand) {
        userCommand = removeFirstWord(userCommand);
        String flag = "Save";
//        String pattern = "^([a-zA-Z]:)?(/[a-zA-Z0-9_.-]+)+/?$";
        String pattern = "[\\w\\d]+\\/[^\\/]+\\.[\\w\\d]+";
        Matcher matcher = Pattern.compile(pattern).matcher(userCommand);
        if (matcher.matches()) {
            String path = matcher.group(0);
            Controller.storeSpreadsheet(path);
            return new String[]{flag, matcher.group(0)};
        } else {
            System.out.println("error");
            return null;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String uc = "";

//        RF doesnt work yet
        while (!uc.equals("quit")) {
            System.out.println("RF address = read commands from a file \n C for create an spread sheet \n E CellCordinate Input for editing a cell \n L path for load \n S path for Store");
            System.out.print("Enter command:");
            uc = scanner.nextLine();

            if (uc.startsWith("RF")) {
                System.out.println(Arrays.toString(RF(uc)));
            } else if (uc.startsWith("C")) {
                System.out.println(Arrays.toString(C()));
            } else if (uc.startsWith("E")) {
                System.out.println(Arrays.toString(E(uc)));
            } else if (uc.startsWith("L")) {
                System.out.println(Arrays.toString(L(uc)));
            } else if (uc.startsWith("S")) {
                System.out.println(Arrays.toString(S(uc)));
            } else if (uc.equals("quit")) {
                break;
            }
        }
    }
}

