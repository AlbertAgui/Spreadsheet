package org.example;

public class Result<T> {
    private T value;
    private Boolean success;

    public Result(T value, Boolean success) {
        this.value = value;
        this.success = success;
    }

    public T getValue() {
        return value;
    }

    public Boolean getSuccess() {
        return success;
    }
}

