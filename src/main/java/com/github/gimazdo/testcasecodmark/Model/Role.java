package com.github.gimazdo.testcasecodmark.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;


/**
 * Role - класс, описывающий роль
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "roles")
@EqualsAndHashCode
public class Role {

    /**
     * Поле хранящее уникальный номер роли, является первичным ключом
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Поле хранящее название роли
     */
    @Column(unique = true)
    private String name;
}
