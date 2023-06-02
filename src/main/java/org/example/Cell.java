package org.example;

public class Cell {

    private Content content;

    public Cell() {
        this.content = new Content();
    }

    public void setWrittenData(String writtenData) {
        content.setWrittenData(writtenData);
    }

    public String getWrittenData() {
        return content.getWrittenData();
    }

    public void setValue(float value) {
        content.setValue(value);
    }

    public float getValue() {
        return content.getValue();
    }
}
