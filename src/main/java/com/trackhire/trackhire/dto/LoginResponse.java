package com.trackhire.trackhire.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private String token;
    private Long userId;
    private String name;
    private String email;
}