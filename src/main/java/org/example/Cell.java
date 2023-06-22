package org.example;

import org.example.ContentPackage.Content;

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
        content = new Content();
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public Content getContent() {
        return content;
    }

    public void setDependants(Dependants dependants) {this.dependants = dependants;}

    public Dependants getDependants() {
        return this.dependants;
    }
}
