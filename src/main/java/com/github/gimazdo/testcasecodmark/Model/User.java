package com.github.gimazdo.testcasecodmark.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * User - класс, описывающий пользователя
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@EqualsAndHashCode
public class User {


    /**
     * Поле хранящее логин пользователя, является первичным ключом
     */
    @Id
    @Column(length = 35)
    private String login;
    /**
     * Поле хранящее имя пользователя
     */
    @Column(nullable = false)
    private String name;
    /**
     * Поле хранящее пароль пользователя
     * В данном случае пароль храниться в явном виде и не шифруется
     */
    @Column(nullable = false)
    private String password;

    /**
     * Поле хранящее роли пользователя
     * Используется {@link Set} так как у пользователя не может быть 2 одинаковые роли
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;


}
