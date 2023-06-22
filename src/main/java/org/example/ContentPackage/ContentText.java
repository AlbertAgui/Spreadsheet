package org.example.ContentPackage;

import edu.upc.etsetb.arqsoft.spreadsheet.entities.NoNumberException;

public class ContentText extends  Content<String> {
    @Override
    public String getContentText() {
        return super.getValue();
    }

    @Override
    public float getContentNumber() throws NoNumberException {
        try {
            return Float.parseFloat(super.getValue());
        } catch (Error e) {
            throw new NoNumberException();
        }
    }
}