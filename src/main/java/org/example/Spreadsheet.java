package org.example;

public class Spreadsheet {
    SparseMatrix<Cell> cell_matrix = new SparseMatrix<>();

    public void get_cell_value(Num_coordinate n_coordinate) {
        float value;
        value = cell_matrix.GetElem(n_coordinate.num_column, n_coordinate.num_row).getValue();
        //System.out.println("get value from coordinates:" + coordinate + " : " +  value + "\n");
    }

    public void set_cell_value(Num_coordinate n_coordinate, float value) {
        Cell in_cell;
        in_cell = new Cell();
        in_cell.setValue(value);
        cell_matrix.SetElem(n_coordinate.num_column, n_coordinate.num_row, in_cell);
        //System.out.println("set value to coordinates:" + coordinate + " : " + value + "\n");
    }
}
