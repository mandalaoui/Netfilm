package com.example.androidapp.api;

import java.util.List;

public class LoginResponse {
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

    public LoginResponse(List<String> errors) {
        this.errors = errors;
    }

    public LoginResponse() {
    }
    public LoginResponse(String userId) {
        this.userId = userId;
    }

}
