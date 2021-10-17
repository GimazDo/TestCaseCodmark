package com.github.gimazdo.testcasecodmark.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;


@Data
public class UserRole {
    private  String roleName;
    private  String login;

    public UserRole(String roleName, String login) {
        this.roleName = roleName;
        this.login = login;
    }
}
