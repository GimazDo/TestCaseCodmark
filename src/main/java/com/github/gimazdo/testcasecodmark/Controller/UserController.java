package com.github.gimazdo.testcasecodmark.Controller;

import com.github.gimazdo.testcasecodmark.Model.Role;
import com.github.gimazdo.testcasecodmark.Model.User;
import com.github.gimazdo.testcasecodmark.Exception.UserServiceException;
import com.github.gimazdo.testcasecodmark.Service.UserService;
import com.github.gimazdo.testcasecodmark.dto.UserRole;
import com.github.gimazdo.testcasecodmark.dto.UserWithoutRoles;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * REST контроллер всех действий с пользователем и ролями.
 */
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Функция получения списка пользователей
     * @return список пользователей без их ролей в формате JSON
     */
    @GetMapping("/getUsers")
    @ResponseBody
    public List<UserWithoutRoles> getUsers() {

        return userService.getAllUsersWithoutRoles();

    }

    /**
     * Получение конкретного пользователя
     * @param user пользователь, которого нужно найти. Используется класс {@link User},
     *             потому что нужна обертка, чтобы взять login из JSON.
     * @return {@link User} со всеми его ролями.
     */
    @GetMapping("/getUser")
    @ResponseBody
    public User getUser(@RequestBody User user) {
        return userService.findByLogin(user.getLogin());
    }

    /**
     * Функция добавления пользователя
     * @param user пользователь, которого нужно добавить.
     * @return HashMap, который хранит результат работы функции.
     *      Если все проверки пройдены успешно {success: true}
     *      Если случилась ошибка валидации {success: false, errors: {массив ошибок}}
     */
    @PostMapping("/addUser")
    @ResponseBody
    public HashMap<String, Object> addUser(@RequestBody User user) {
        HashMap<String, Object> resultMessage = new HashMap<>();
        try {
            userService.addUser(user);
            resultMessage.put("success", true);
        } catch (UserServiceException e) {
            resultMessage.put("success", false);
            resultMessage.put("errors", e.getErrors());
        }
        return resultMessage;
    }

    /**
     * Функция обновления пользователя
     * @param user пользователь, с новыми данными, но старым login(так как логин не изменяется).
     * @return HashMap, который хранит результат работы функции.
     *        Если все проверки пройдены успешно {success: true}
     *        Если случилась ошибка валидации {success: false, errors: {массив ошибок}}
     */
    @PutMapping("/updateUser")
    @ResponseBody
    public HashMap<String, Object> updateUser(@RequestBody User user)
    {
        HashMap<String, Object> resultMessage = new HashMap<>();
        try {
            userService.updateUser(user);
            resultMessage.put("success", true);
        } catch (UserServiceException e) {
            resultMessage.put("success", false);
            resultMessage.put("errors", e.getErrors());
        }
        return resultMessage;
    }

    /**
     * Функция удаления пользователя.
     * @param user пользователь которого нужно удалить. Используется только поле login, так что остальным могут быть пустыми.
     * @return HashMap, который хранит результат работы функции.
     *      Если все проверки пройдены успешно {success: true}
     *      Если случилась ошибка валидации {success: false, errors: {массив ошибок}}
     */
    @DeleteMapping("/deleteUser")
    @ResponseBody
    public HashMap<String, Object> deleteUser(@RequestBody User user)
    {
        HashMap<String, Object> resultMessage = new HashMap<>();
        try {
            userService.deleteUser(user.getLogin());
            resultMessage.put("success", true);
        } catch (UserServiceException e) {
            resultMessage.put("success", false);
            resultMessage.put("errors", e.getErrors());
        }
        return resultMessage;
    }

    /**
     * Функция добавления новой роли.
     * @param role  роль, которую нужно добавить.
     * @return HashMap, который хранит результат работы функции.
     *      Если все проверки пройдены успешно и роль добавлена {success: true}
     *      Если случилась ошибка валидации {success: false, errors: {массив ошибок}}
     */
    @PostMapping("/addRole")
    @ResponseBody
    public HashMap<String, Object> addRole(@RequestBody Role role)
    {
        HashMap<String, Object> resultMessage = new HashMap<>();
        try {
            userService.addRole(role);
            resultMessage.put("success", true);
        } catch (UserServiceException e) {
            resultMessage.put("success", false);
            resultMessage.put("errors", e.getErrors());
        }
        return resultMessage;
    }

    /**
     * Добавление роли пользователю
     * @param userRole хранить логин пользователя и роль которую нужно добавить
     * @return HashMap, который хранит результат работы функции.
     * Если все проверки пройдены успешно и роль добавлена {success: true}
     * Если случилась ошибка валидации {success: false, errors: {массив ошибок}
     */
    @PutMapping("/addRoleToUser")
    @ResponseBody
    public HashMap<String, Object> addRoleToUser(@RequestBody UserRole userRole)
    {
        HashMap<String, Object> resultMessage = new HashMap<>();
        try {
            userService.addRoleToUser(userRole.getLogin(),userRole.getRoleName());
            resultMessage.put("success", true);
        } catch (UserServiceException e) {
            resultMessage.put("success", false);
            resultMessage.put("errors", e.getErrors());
        }
        return resultMessage;
    }



}
