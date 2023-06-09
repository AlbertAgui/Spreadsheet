package org.example;

import java.util.*;

public class Dependants {
    private Set<NumCoordinate> dependants;

    public Dependants() {
        this.dependants = new HashSet<>();
    }

    public void setDependants(Set<NumCoordinate> dependants) {
        this.dependants = dependants;
    }

    public Set<NumCoordinate> getDependants() {
        return this.dependants;
    }

    public void addDependant(NumCoordinate dependant) {
        this.dependants.add(dependant);
    }

    public void eraseDependant(NumCoordinate dependant) {
        this.dependants.remove(dependant);
    }

}
