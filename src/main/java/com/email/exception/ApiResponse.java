package com.email.exception;

public class ApiResponse<T> {

    private String status;   // SUCCESS / FAILURE
    private String message;
    private T data;

    public ApiResponse(String status,  T data) {
        this.status = status;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>("SUCCESS", data);
    }

    public static <T> ApiResponse<T> failure(String message) {
        return new ApiResponse<>("FAILURE", null);
    }

    // getters & setters


    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
