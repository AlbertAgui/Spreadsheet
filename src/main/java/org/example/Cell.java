package org.example;

import java.util.Set;

public class Cell {

    private Content content;

    private Dependants dependants;

    /*public Cell(String contentType) {
        ContentFactory contentfactory = ContentFactory.getInstance(contentType);
        this.content = contentfactory.createContent();
        this.dependants = new Dependants();
    }*/

    public Cell(){
        dependants = new Dependants();
    }


    public void setContent(Content content) {
        this.content = content;
    }

    public Content getContent() {
        return content;
    }

    public void addDependant(NumCoordinate dependant) {
        this.dependants.addDependant(dependant);
    }

    public void eraseDependant(NumCoordinate dependant) {
        this.dependants.eraseDependant(dependant);
    }

    public Set<NumCoordinate> getDependants() {
        return this.dependants.getDependants();
    }
}
