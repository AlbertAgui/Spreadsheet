package org.example.TUI;


import edu.upc.etsetb.arqsoft.spreadsheet.entities.CircularDependencyException;
import edu.upc.etsetb.arqsoft.spreadsheet.entities.ContentException;
import edu.upc.etsetb.arqsoft.spreadsheet.usecases.marker.ReadingSpreadSheetException;
import edu.upc.etsetb.arqsoft.spreadsheet.usecases.marker.SavingSpreadSheetException;
import org.example.*;
import org.example.ContentPackage.Content;
import org.example.ContentPackage.ContentFormula;
import org.example.ContentPackage.ContentNumerical;
import org.example.ContentPackage.ContentText;

import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
public class TextualInterface {

    public static void printCells(Cells cells) {
        NumCoordinate size = cells.getSize();

        if (size == null) {
            throw new RuntimeException("Print spreadsheet: spreadsheet size is null");
        }

        int cellWidth = 15; // Adjust the width as needed

        for (int i = 1; i <= size.getNumRow(); i++) {
            for (int j = 1; j <= size.getNumColum(); j++) {
                NumCoordinate coordinate = new NumCoordinate(i, j);
                Cell cell = cells.getCell(coordinate);

                if (cell != null) {
                    Content content = cell.getContent();
                    String cellValue;
                    if (content instanceof ContentFormula) {
                        cellValue = ((ContentFormula) content).getValue() + " = " + ((ContentFormula) content).getWrittenData();
                    } else if (content instanceof ContentText) {
                        cellValue = ((ContentText) content).getValue();
                    } else if (content instanceof ContentNumerical) {
                        cellValue = String.valueOf(((ContentNumerical) content).getValue());
                    } else {
                        cellValue = "";
                    }
                    int padding = 1;
                    String formattedCell = "";
                    padding = cellWidth - cellValue.length();
                    int leftPadding = padding / 2;
                    int rightPadding = padding - leftPadding;
                    formattedCell = String.format("[%-" + Math.max(1, leftPadding) + "s%s%-" + Math.max(1, rightPadding) + "s] ", "", cellValue, "");

                    System.out.print(formattedCell);
                } else {
                    System.out.print("[ ] ");
                }
            }
            System.out.println();
        }
    }
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

    public static String[] E(String userCommand) throws ContentException, CircularDependencyException {
        String flag = "edit cell";
        userCommand = removeFirstWord(userCommand);
        String pattern = "([A-Z]+[0-9]+)\\s(.+)";
        Matcher matcher = Pattern.compile(pattern).matcher(userCommand);
        if (matcher.matches()) {
            String cellCoordinate = matcher.group(1);
            String content = matcher.group(2);
            Controller.editCell(cellCoordinate, content);
            printCells(Controller.getSpreadsheet().cells);
            return new String[]{flag, cellCoordinate, content};
        } else {
            System.out.println("Error Editing cell please check the command and try again");
            return null;
        }
    }

    private static String[] RF(String userCommand) throws SavingSpreadSheetException, ReadingSpreadSheetException {
        String flag = "read_file_command";
        userCommand = removeFirstWord(userCommand);
        String pattern = "[\\w\\d]+/[^/]+\\.[\\w\\d]+";
        Matcher matcher = Pattern.compile(pattern).matcher(userCommand);
        if (matcher.matches()) {
            String filePath = matcher.group(0);
            try (BufferedReader fileReader = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = fileReader.readLine()) != null) {
                    String command = line.trim();
                    if (command.startsWith("RF")) {
                        RF(command);
                    } else if (command.startsWith("C")) {
                        C();
                    } else if (command.startsWith("E")) {
                        E(command);
                    } else if (command.startsWith("L")) {
                        L(command);
                    } else if (command.startsWith("S")) {
                        S(command);
                    } else if (command.equals("quit")) {
                        break;
                    }
                }
            } catch (IOException e) {
                System.out.println("File not found");
            } catch (ContentException e) {
                throw new RuntimeException(e);
            } catch (CircularDependencyException e) {
                throw new RuntimeException(e);
            }
            return new String[]{flag, filePath};
        } else {
            System.out.println("Error Incorrect path format");
            return null;
        }
    }

    public static String[] C() {
        String flag = "create spreadsheet";
        Controller.createEmptySpreadsheet();
        printCells(Controller.getSpreadsheet().cells);
        return new String[]{flag};
    }

    public static String[] L(String userCommand) throws ReadingSpreadSheetException {
        userCommand = removeFirstWord(userCommand);
        String flag = "Load";
        String pattern = "[\\w\\d]+\\/[^\\/]+\\.[\\w\\d]+";
        Matcher matcher = Pattern.compile(pattern).matcher(userCommand);
        if (matcher.matches()) {
            String path = matcher.group(0);
            Controller.loadSpreadsheet(path);
            printCells(Controller.getSpreadsheet().cells);
            return new String[]{flag, matcher.group(0)};
        }/* else {
            System.out.println("Loading Failed, File not found or incorrect format");
            return null;
        }*/
        return null;
    }

    public static String[] S(String userCommand) throws SavingSpreadSheetException {
        userCommand = removeFirstWord(userCommand);
        String flag = "Save";
        String pattern = "[\\w\\d]+\\/[^\\/]+\\.[\\w\\d]+";
        Matcher matcher = Pattern.compile(pattern).matcher(userCommand);
        if (matcher.matches()) {
            String path = matcher.group(0);
            Controller.storeSpreadsheet(path);
            printCells(Controller.getSpreadsheet().cells);
            return new String[]{flag, matcher.group(0)};
        } else {
            System.out.println("Saving Failed, Please check the path");
            return null;
        }
    }

    public static void input_commands(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String uc = "";
        System.out.println("RF address = read commands from a file \n" +
                " C for create an spread sheet \n" +
                " E CellCordinate Input for editing a cell \n" +
                " L path for load \n" +
                " S path for Store \n" +
                " QUIT to close the program");
        while (!uc.equals("quit")) {
            System.out.print("Enter command:");
            uc = scanner.nextLine();

            if (uc.startsWith("RF")) {
                try {
                    System.out.println(Arrays.toString(RF(uc)));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            } else if (uc.startsWith("C")) {
                try {
                    System.out.println(Arrays.toString(C()));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            } else if (uc.startsWith("E")) {
                try {
                    System.out.println(Arrays.toString(E(uc)));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            } else if (uc.startsWith("L")) {
                try {
                    System.out.println(Arrays.toString(L(uc)));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            } else if (uc.startsWith("S")) {
                try {
                    System.out.println(Arrays.toString(S(uc)));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            } else if (uc.equals("QUIT")) {
                break;
            } else {
                System.out.println("Wrong Command\n Please Enter correctly");
                System.out.println("RF address = read commands from a file \n" +
                        " C for create an spread sheet \n" +
                        " E CellCordinate Input for editing a cell \n" +
                        " L path for load \n" +
                        " S path for Store \n" +
                        " QUIT to close the program");
            }
        }
    }
}

