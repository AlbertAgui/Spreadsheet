package org.example.ContentPackage;

import org.example.ContentPackage.Content;

public class ContentFormula extends Content<Float> {
    private String writtenData;

    public void setWrittenData(String writtenData) {
        this.writtenData = writtenData;
    }

    public String getWrittenData() {
        return this.writtenData;
    }
}
