package org.example;

public class Content {
    private String writtenData;
    private float value;

    public Content() {
        this.writtenData = "";
        this.value = 0;
    }

    public void setWrittenData(String writtenData) {
        this.writtenData = writtenData;
    }

    public String getWrittenData() {
        return this.writtenData;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public float getValue() {
        return this.value;
    }
}
