package com.twd.SpringSecurityJWT.dto;

import lombok.Data;

@Data
public class SignInResponce {
    private String statusCode;
    private String message;
    private String token;
    private String refreshToken;
    private String expirationTime;


}
