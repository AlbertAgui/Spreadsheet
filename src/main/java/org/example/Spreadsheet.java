package org.example;

import java.util.HashMap;
import java.util.Map;

public class Spreadsheet {
    Map<String, Cell> cell_matrix = new HashMap<>();

    public void get_cell_value(String coordinate) {
        float value;
        value = cell_matrix.get(coordinate).getValue();
        System.out.println("get value from coordinates:" + coordinate + " : " +  value + "\n");
    }

    public void set_cell_value(String coordinate, float value) {
        Cell temp;
        temp = new Cell();
        temp.setValue(value);
        cell_matrix.put(coordinate, temp);
        System.out.println("set value to coordinates:" + coordinate + " : " + value + "\n");
    }
}
