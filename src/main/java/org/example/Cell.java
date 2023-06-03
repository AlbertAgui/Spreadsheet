package org.example;

import java.util.Set;

public class Cell {

    private Content content;

    private Dependants dependants;

    public Cell() {
        this.content = new Content();
        this.dependants = new Dependants();
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

    public void addDependant(NumCoordinate dependant) {
        dependants.addDependant(dependant);
    }

    public void eraseDependant(NumCoordinate dependant) {
        dependants.eraseDependant(dependant);
    }

    public Set<NumCoordinate> getDependants() {
        return dependants.getDependants();
    }
}
