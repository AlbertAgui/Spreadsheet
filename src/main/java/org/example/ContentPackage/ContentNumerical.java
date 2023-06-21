package org.example.ContentPackage;

public class ContentNumerical extends Content<Float> {
    @Override
    public String getContentText() {
        return Float.toString(super.getValue());
    }

    @Override
    public float getContentNumber() {
        return super.getValue();
    }
}
