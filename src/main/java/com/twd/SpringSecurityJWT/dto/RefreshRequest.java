package com.twd.SpringSecurityJWT.dto;

import lombok.Data;

@Data
public class RefreshRequest {
    private String token;
}
