package com.ziong.ziong.model.dtos;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserDto {
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private String password;
}
