package org.example;

public class ContentFormula extends Content<Float> {
    private String writtenData;

    public void setWrittenData(String writtenData) {
        this.writtenData = writtenData;
    }

    public String getWrittenData() {
        return this.writtenData;
    }

    @Override
    public String getContentText() {
        return getWrittenData();
    }

    @Override
    public float getContentNumber() {
        return super.getValue();
    }
}
