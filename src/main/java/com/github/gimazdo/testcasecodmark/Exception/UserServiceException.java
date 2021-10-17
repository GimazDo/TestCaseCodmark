package com.github.gimazdo.testcasecodmark.Exception;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * UserServiceException - исключение, которое может быть проброшено при действиях с пользователем
 */
@Data
public class UserServiceException extends Exception{
    /**
     * Поле хранящее список ошибок
     */
    private List<String> errors;

    /**
     * Конструктор, чтобы правильно проинициализировался лист с ошибками.
     */
    public UserServiceException() {
        this.errors = new ArrayList<>();
    }
}
