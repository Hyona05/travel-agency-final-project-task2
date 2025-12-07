package com.epam.finaltask.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private String statusCode;
    private String statusMessage;
    private T results;

    public ApiResponse() {
    }

    public ApiResponse(String statusCode, String statusMessage, T results) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.results = results;
    }

    public static <T> ApiResponse<T> ok(String message, T results) {
        return new ApiResponse<>("OK", message, results);
    }

    public static <T> ApiResponse<T> ok(T results) {
        return new ApiResponse<>("OK", null, results);
    }

    public String getStatusCode() {
        return statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public T getResults() {
        return results;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public void setResults(T results) {
        this.results = results;
    }
}
