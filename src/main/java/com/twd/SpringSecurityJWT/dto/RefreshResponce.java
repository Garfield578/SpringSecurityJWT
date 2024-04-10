package com.twd.SpringSecurityJWT.dto;

import lombok.Data;

@Data
public class RefreshResponce {
    private String statusCode;
    private String message;
    private String token;
    private String refreshToken;
    private String expirationTime;
}
