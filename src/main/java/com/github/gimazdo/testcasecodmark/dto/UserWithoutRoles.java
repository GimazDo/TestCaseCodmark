package com.github.gimazdo.testcasecodmark.dto;

import com.github.gimazdo.testcasecodmark.Model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * Класс DTO для отображения {@link User} без роли
 */
@Data
public class UserWithoutRoles {
    /**
     * Имя пользователя
     */
    private String name;
    /**
     * Логин пользователя
     */
    private String login;

    /**
     * Конструктор для конвертации {@link User} в {@link UserWithoutRoles}
     * @param user - пользователь для конвертации
     */
    public  UserWithoutRoles(User user)
    {
        this.login = user.getLogin();
        this.name = user.getName();
    }
}
