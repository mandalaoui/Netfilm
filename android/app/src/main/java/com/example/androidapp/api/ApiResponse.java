package com.example.androidapp.api;

import java.util.List;

public class ApiResponse {
//    private String userId;
    private String token;
    private List<String> errors;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public ApiResponse(List<String> errors) {
        this.errors = errors;
    }

    public ApiResponse() {
    }
    public ApiResponse(String token) {
        this.token = token;
    }

}
