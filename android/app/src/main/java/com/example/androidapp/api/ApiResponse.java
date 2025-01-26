package com.example.androidapp.api;

import java.util.List;

public class ApiResponse {
    private String userId;
    private List<String> errors;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
    public ApiResponse(String userId) {
        this.userId = userId;
    }

}
