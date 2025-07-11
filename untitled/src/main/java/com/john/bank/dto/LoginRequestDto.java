package com.john.bank.dto;

import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class LoginRequestDto {
    private String name;
    private String password;
}
