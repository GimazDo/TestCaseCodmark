package com.github.gimazdo.testcasecodmark.dto;

import com.github.gimazdo.testcasecodmark.Model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;


@Data
public class UserWithoutRoles {
    private String name;
    private String login;

    public  UserWithoutRoles(User user)
    {
        this.login = user.getLogin();
        this.name = user.getName();
    }
}
