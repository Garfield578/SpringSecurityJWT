package com.twd.SpringSecurityJWT.dto;

import com.twd.SpringSecurityJWT.entity.OurUsers;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.net.http.HttpResponse;

@Data
public class SignUpResponce {
    private int StatusCode;
    private String message;
    private OurUsers ourUsers;

    public void setStatusCode(int statusCode) {
        this.StatusCode = statusCode;
    }

    public int getStatusCode() {
        return StatusCode;
    }
}
