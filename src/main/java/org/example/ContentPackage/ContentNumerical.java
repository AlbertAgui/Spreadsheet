package org.example.ContentPackage;

public class ContentNumerical extends Content<Float> {
    @Override
    public String getContentText() {
        float value = super.getValue();
        if (value % 1 == 0) {  // Check if the value is an integer
            return Integer.toString((int) value);  // Write integer value without decimal point
        } else {
            return Float.toString(value);  // Write decimal value with decimal point
        }
    }

    @Override
    public float getContentNumber() {
        return super.getValue();
    }
}
