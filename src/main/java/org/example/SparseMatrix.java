package org.example;

import java.util.HashMap;
import java.util.Map;

public class SparseMatrix<T> {
    private Map<Integer, Map<Integer, T>> matrix;

    public SparseMatrix(){
        matrix = new HashMap<>();
    }

    public void SetElem(int nCol, int nRow, T value){
        if(!matrix.containsKey(nRow)) {
            matrix.put(nRow, new HashMap<>());
        }
        matrix.get(nRow).put(nCol, value);
    }

    public T GetElem(int nCol, int nRow){
        if(matrix.containsKey(nRow)) {
            Map<Integer, T> columns = matrix.get(nRow);
            if(columns.containsKey(nCol))
                return columns.get(nCol);
        }
        return null;
    }
}
