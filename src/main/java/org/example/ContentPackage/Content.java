package org.example.ContentPackage;

import edu.upc.etsetb.arqsoft.spreadsheet.entities.NoNumberException;

public class Content<T> {
    private T value;

    public void setValue(T value) {
        this.value = value;
    }

    public T getValue() {
        return this.value;
    }

    public String getContentText() {
        return "";
    }

    public float getContentNumber() throws NoNumberException {
        return 0;
    }
}
