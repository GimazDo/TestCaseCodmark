package com.github.gimazdo.testcasecodmark.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * Клас содержащий название роли и логин пользователя. Используется для добавления пользователю новой роли.
 */
@Data
public class UserRole {
    /**
     * Название роли
     */
    private  String roleName;
    /**
     * Логин пользователя
     */
    private  String login;
    /**
     * Конструктор
     */
    public UserRole(String roleName, String login) {
        this.roleName = roleName;
        this.login = login;
    }
}
