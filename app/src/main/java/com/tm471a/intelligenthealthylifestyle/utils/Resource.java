package com.tm471a.intelligenthealthylifestyle.utils;

public class Resource<T> {

    private T data;
    private String message;
    private Status status;

    public enum Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    public Resource(Status status, T data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> Resource<T> success(T data) {
        return new Resource<>(Status.SUCCESS, data, null);
    }

    public static <T> Resource<T> error(String message, T data) {
        return new Resource<>(Status.ERROR, data, message);
    }

    public static <T> Resource<T> loading() {
        return new Resource<>(Status.LOADING, null, null);
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public Status getStatus() {
        return status;
    }
}
