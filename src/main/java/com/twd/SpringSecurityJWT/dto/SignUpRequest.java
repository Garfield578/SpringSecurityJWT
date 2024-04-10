package com.twd.SpringSecurityJWT.dto;

import lombok.Data;

@Data
public class SignUpRequest {
    private String name;
    private String email;
    private String password;
    private String role;
}
