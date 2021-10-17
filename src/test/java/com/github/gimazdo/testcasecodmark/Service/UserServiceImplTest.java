package com.github.gimazdo.testcasecodmark.Service;

import com.github.gimazdo.testcasecodmark.Exception.UserServiceException;
import com.github.gimazdo.testcasecodmark.Model.Role;
import com.github.gimazdo.testcasecodmark.Model.User;
import com.github.gimazdo.testcasecodmark.Repository.RoleRepository;
import com.github.gimazdo.testcasecodmark.Repository.UserRepository;
import com.github.gimazdo.testcasecodmark.TestCaseCodmarkApplication;
import com.github.gimazdo.testcasecodmark.dto.UserRole;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.Extensions;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = TestCaseCodmarkApplication.class)
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    /**
     * Тест на добавление пользователя без ролей.
     */
    @Test
    void addUserWithoutRoles() {
        User user = new User("Login","Name","Passw0rd",null);
        try {
            userService.addUser(user);
        }
        catch (UserServiceException e)
        {
            e.printStackTrace();
        }
        Mockito.verify(userRepository, Mockito.times(1)).save(ArgumentMatchers.any(User.class));
        Mockito.verify(userRepository, Mockito.times(1)).findUserByLogin(ArgumentMatchers.anyString());
        Mockito.verify(roleRepository,Mockito.times((0))).findByName(ArgumentMatchers.anyString());
    }

    /**
     * Тест на добавление пользователя с ролями
     */
    @Test
    void addUserWithRoles() {
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(new Role(null, "Role"));

        Mockito.when(roleRepository.findByName(ArgumentMatchers.anyString())).thenReturn(new Role(Long.valueOf(1),"Role"));

        User user = new User("Login","Name","Passw0rd",roleSet);
        try {
            userService.addUser(user);
        }
        catch (UserServiceException e)
        {
            e.printStackTrace();
        }
        Mockito.verify(userRepository, Mockito.times(1)).save(ArgumentMatchers.any(User.class));
        Mockito.verify(userRepository, Mockito.times(1)).findUserByLogin(ArgumentMatchers.anyString());
        Mockito.verify(roleRepository,Mockito.times((roleSet.size()))).findByName(ArgumentMatchers.anyString());
    }

    /**
     * Тест на ошибку добавление пользователя с несуществующей ролью
     */
    @Test
    void addUserWithRolesFailed()
    {
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(new Role(null, "Role"));

        Mockito.when(roleRepository.findByName(ArgumentMatchers.anyString())).thenReturn(null);
        UserServiceException exception = Assertions.assertThrows(UserServiceException.class, () -> {
            User user = new User("Login","Name","Passw0rd",roleSet);
            userService.addUser(user);
        });
        List<String> errors = new ArrayList<>();
        errors.add("Роли Role не существует");
        assertEquals(errors, exception.getErrors());
        Mockito.verify(userRepository, Mockito.times(0)).save(ArgumentMatchers.any(User.class));
        Mockito.verify(userRepository, Mockito.times(1)).findUserByLogin(ArgumentMatchers.anyString());
        Mockito.verify(roleRepository,Mockito.times((roleSet.size()))).findByName(ArgumentMatchers.anyString());
    }

    @Test
    void addUserFailedUserExists()
    {
        Mockito.when(userRepository.findUserByLogin(ArgumentMatchers.anyString())).thenReturn(new User());

        UserServiceException exception = Assertions.assertThrows(UserServiceException.class, () -> {
            User user = new User("Login","Name","password",null);
            userService.addUser(user);
        });
        List<String> errors = new ArrayList<>();
        errors.add("Login занят");
        assertEquals(errors,exception.getErrors());
        Mockito.verify(userRepository, Mockito.times(0)).save(ArgumentMatchers.any(User.class));
        Mockito.verify(userRepository, Mockito.times(1)).findUserByLogin(ArgumentMatchers.anyString());
        Mockito.verify(roleRepository,Mockito.times((0))).findByName(ArgumentMatchers.anyString());
    }

    /**
     * Тест на ошибку добавления пользователя с некорректным паролем
     */
    @Test()
    void addUserFailedByPasswordWrong() {

        UserServiceException exception = Assertions.assertThrows(UserServiceException.class, () -> {
            User user = new User("Login","Name","password",null);
            userService.addUser(user);
        });
        List<String> errors = new ArrayList<>();
        errors.add("Password должен содержать хотя бы 1 заглавную букву и 1 цифру");
        assertEquals(errors,exception.getErrors());
        Mockito.verify(userRepository, Mockito.times(0)).save(ArgumentMatchers.any(User.class));
        Mockito.verify(userRepository, Mockito.times(1)).findUserByLogin(ArgumentMatchers.anyString());
        Mockito.verify(roleRepository,Mockito.times((0))).findByName(ArgumentMatchers.anyString());
    }

    /**
     * Тест на ошибку добавления пользователя с пустым логином
     */
    @Test()
    void addUserFailedByLogin() {

        UserServiceException exception = Assertions.assertThrows(UserServiceException.class, () -> {
            User user = new User(null,"Name","Passw0rd",null);
            userService.addUser(user);
        });
        List<String> errors = new ArrayList<>();
        errors.add("Login не может быть пустым");

        assertEquals(errors,exception.getErrors());
        Mockito.verify(userRepository, Mockito.times(0)).save(ArgumentMatchers.any(User.class));
        Mockito.verify(userRepository, Mockito.times(0)).findUserByLogin(ArgumentMatchers.anyString());
        Mockito.verify(roleRepository,Mockito.times((0))).findByName(ArgumentMatchers.anyString());
    }
    /**
     * Тест на ошибку добавления пользователя с пустым именем
     */
    @Test()
    void addUserFailedByName() {

        UserServiceException exception = Assertions.assertThrows(UserServiceException.class, () -> {
            User user = new User("Login",null,"Passw0rd",null);
            userService.addUser(user);
        });
        List<String> errors = new ArrayList<>();
        errors.add("Name не может быть пустым");
        assertEquals(errors,exception.getErrors());
        Mockito.verify(userRepository, Mockito.times(0)).save(ArgumentMatchers.any(User.class));
        Mockito.verify(userRepository, Mockito.times(1)).findUserByLogin(ArgumentMatchers.anyString());
        Mockito.verify(roleRepository,Mockito.times((0))).findByName(ArgumentMatchers.anyString());
    }

    /**
     * Тест на ошибку добавления пользователя с пустым паролем
     */
    @Test()
    void addUserFailedByPasswordNull() {

        UserServiceException exception = Assertions.assertThrows(UserServiceException.class, () -> {
            User user = new User("Login","Name",null,null);
            userService.addUser(user);
        });
        List<String> errors = new ArrayList<>();
        errors.add("Password не может быть пустым");
        assertEquals(errors,exception.getErrors());
        Mockito.verify(userRepository, Mockito.times(0)).save(ArgumentMatchers.any(User.class));
        Mockito.verify(userRepository, Mockito.times(1)).findUserByLogin(ArgumentMatchers.anyString());
        Mockito.verify(roleRepository,Mockito.times((0))).findByName(ArgumentMatchers.anyString());
    }
    /**
     * Тест на ошибку добавления пользователя со всеми пустыми полями
     */
    @Test()
    void addUserFailedByLoginAndNameAndPassword() {

        UserServiceException exception = Assertions.assertThrows(UserServiceException.class, () -> {
            User user = new User(null,null,null,null);
            userService.addUser(user);
        });
        List<String> errors = new ArrayList<>();
        errors.add("Login не может быть пустым");
        errors.add("Name не может быть пустым");
        errors.add("Password не может быть пустым");
        assertEquals(errors,exception.getErrors());
        Mockito.verify(userRepository, Mockito.times(0)).save(ArgumentMatchers.any(User.class));
        Mockito.verify(userRepository, Mockito.times(0)).findUserByLogin(ArgumentMatchers.anyString());
        Mockito.verify(roleRepository,Mockito.times((0))).findByName(ArgumentMatchers.anyString());
    }

    /**
     * Тест на удаление пользователя
     */
    @Test
    void deleteUser()
    {
        User user = new User("Login", null,null,null);

        Mockito.when(userRepository.findUserByLogin(ArgumentMatchers.anyString())).thenReturn(user);

        try
        {
            userService.deleteUser(user.getLogin());
        } catch (UserServiceException e) {
            e.printStackTrace();
        }
        Mockito.verify(userRepository, Mockito.times(1)).delete(ArgumentMatchers.any(User.class));
        Mockito.verify(userRepository, Mockito.times(1)).findUserByLogin(ArgumentMatchers.anyString());

    }
    /**
     * Тест на ошибку при удалении пользователя, потому что его не существует
     */
    @Test
    void deleteUserFailedUserNotFound()
    {
        User user = new User("Login", null,null,null);

        Mockito.when(userRepository.findUserByLogin(ArgumentMatchers.anyString())).thenReturn(null);
        UserServiceException exception = Assertions.assertThrows(UserServiceException.class, () -> {

            userService.deleteUser(user.getLogin());
        });
        List<String> errors = new ArrayList<>();
        errors.add("Пользователя не существует");
        assertEquals(errors,exception.getErrors());
        Mockito.verify(userRepository, Mockito.times(0)).delete(ArgumentMatchers.any(User.class));
        Mockito.verify(userRepository, Mockito.times(1)).findUserByLogin(ArgumentMatchers.anyString());

    }

    /**
     * Тест на ошибку при удалении пользователя из-за пустого логина
     */
    @Test
    void deleteUserFailedByLoginNull()
    {
        User user = new User(null, null,null,null);

        UserServiceException exception = Assertions.assertThrows(UserServiceException.class, () -> {

            userService.deleteUser(user.getLogin());
        });
        List<String> errors = new ArrayList<>();
        errors.add("Login не может быть пустым");
        assertEquals(errors,exception.getErrors());
        Mockito.verify(userRepository, Mockito.times(0)).delete(ArgumentMatchers.any(User.class));
        Mockito.verify(userRepository, Mockito.times(0)).findUserByLogin(ArgumentMatchers.anyString());
    }

    /**
     * Тест на изменение пользователя, без изменения его ролей.
     */
    @Test
    void updateUserWithoutRoles()
    {
        User user = new User ("Login", "NewName", "NewPassw0rd", null);

        Mockito.when(userRepository.findUserByLogin(ArgumentMatchers.anyString())).thenReturn(new User("Login", "OldName", "0ldPassword", null));
        try
        {
            userService.updateUser(user);
        } catch (UserServiceException e) {
            e.printStackTrace();
        }

        Mockito.verify(userRepository, Mockito.times(1)).findUserByLogin(ArgumentMatchers.anyString());
        Mockito.verify(userRepository, Mockito.times(1)).save(ArgumentMatchers.any(User.class));
    }

    /**
     * Тест на изменение пользователя с изменением его ролей.
     */
    @Test
    void updateUserWithRoles()
    {
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(null,"Role1"));
        User user = new User ("Login", "NewName", "NewPassw0rd", roles);


        Mockito.when(userRepository.findUserByLogin(ArgumentMatchers.anyString())).thenReturn(new User("Login", "OldName", "0ldPassword", new HashSet<>()));
        Mockito.when(roleRepository.findByName(ArgumentMatchers.anyString())).thenReturn(new Role());
        try
        {
            userService.updateUser(user);
        } catch (UserServiceException e) {
            e.printStackTrace();
        }

        Mockito.verify(roleRepository, Mockito.times(roles.size())).findByName(ArgumentMatchers.anyString());
        Mockito.verify(userRepository, Mockito.times(roles.size()+1)).findUserByLogin(ArgumentMatchers.anyString());
        Mockito.verify(userRepository, Mockito.times(roles.size()+1)).save(ArgumentMatchers.any(User.class));

    }
    /**
     * Тест на ошибку при изменении пользователя с изменением его ролей из-за несуществующей роли
     */
    @Test
    void updateUserWithRolesFailedRoleNotExists()
    {
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(null,"Role1"));
        roles.add(new Role(null,"Role2"));
        User user = new User ("Login", "NewName", "NewPassw0rd", roles);


        Mockito.when(userRepository.findUserByLogin(ArgumentMatchers.anyString())).thenReturn(new User("Login", "OldName", "0ldPassword", null));
        Mockito.when(roleRepository.findByName(ArgumentMatchers.anyString())).thenReturn(null);
        UserServiceException exception = Assertions.assertThrows(UserServiceException.class, () -> {

            userService.updateUser(user);
        });
        List<String> errors = new ArrayList<>();
        errors.add("Роли Role2 не существует");
        errors.add("Роли Role1 не существует");
        assertEquals(errors,exception.getErrors());

        Mockito.verify(roleRepository, Mockito.times(roles.size())).findByName(ArgumentMatchers.anyString());
        Mockito.verify(userRepository, Mockito.times(roles.size()+1)).findUserByLogin(ArgumentMatchers.anyString());
        Mockito.verify(userRepository, Mockito.times(0)).save(ArgumentMatchers.any(User.class));
    }
    /**
     * Тест на ошибку при изменении пользователя, из-за отсутствия пользователя с таким логином.
     */
    @Test
    void updateUserFailedUserNotExists()
    {
        User user = new User ("Login", "NewName", "NewPassw0rd", null);


        Mockito.when(userRepository.findUserByLogin(ArgumentMatchers.anyString())).thenReturn(null);
        UserServiceException exception = Assertions.assertThrows(UserServiceException.class, () -> {
            userService.updateUser(user);
        });
        List<String> errors = new ArrayList<>();
        errors.add("Пользователя не существует");
        assertEquals(errors,exception.getErrors());
        Mockito.verify(roleRepository, Mockito.times(0)).findByName(ArgumentMatchers.anyString());
        Mockito.verify(userRepository, Mockito.times(1)).findUserByLogin(ArgumentMatchers.anyString());
        Mockito.verify(userRepository, Mockito.times(0)).save(ArgumentMatchers.any(User.class));
    }

    /**
     * Тест на ошибку при изменении пользователя, из-за некорректного нового пароля.
     */
    @Test
    void updateUserFailedPasswordWrong()
    {
        User user = new User ("Login", "NewName", "WrongPassword", null);
        Mockito.when(userRepository.findUserByLogin(ArgumentMatchers.anyString())).thenReturn(new User());
        UserServiceException exception = Assertions.assertThrows(UserServiceException.class, () -> {
            userService.updateUser(user);
        });
        List<String> errors = new ArrayList<>();
        errors.add("Password должен содержать хотя бы 1 заглавную букву и 1 цифру");
        assertEquals(errors,exception.getErrors());
        Mockito.verify(roleRepository, Mockito.times(0)).findByName(ArgumentMatchers.anyString());
        Mockito.verify(userRepository, Mockito.times(1)).findUserByLogin(ArgumentMatchers.anyString());
        Mockito.verify(userRepository, Mockito.times(0)).save(ArgumentMatchers.any(User.class));
    }

    /**
     * Тест на успешное добавление новой роли.
     */
    @Test
    void addRole()
    {
        Role role = new Role(null, "Role");

        try
        {
            userService.addRole(role);
        } catch (UserServiceException e) {
            e.printStackTrace();
        }
        Mockito.verify(roleRepository, Mockito.times(1)).findByName(ArgumentMatchers.anyString());
        Mockito.verify(roleRepository, Mockito.times(0)).findById(ArgumentMatchers.anyLong());
        Mockito.verify(roleRepository, Mockito.times(1)).save(ArgumentMatchers.any(Role.class));

    }

    /**
     * Тест на ошибку при добавлении роли, потому что роль уже существует
     */
    @Test
    void addRoleFailedRoleExists()
    {
        Role role = new Role(null, "Role");

        Mockito.when(roleRepository.findByName(ArgumentMatchers.anyString())).thenReturn(new Role());

        UserServiceException exception = Assertions.assertThrows(UserServiceException.class, () -> {
            userService.addRole(role);
        });
        List<String> errors = new ArrayList<>();
        errors.add("Роль уже существует");

        assertEquals(errors, exception.getErrors());
        Mockito.verify(roleRepository, Mockito.times(1)).findByName(ArgumentMatchers.anyString());
        Mockito.verify(roleRepository, Mockito.times(0)).findById(ArgumentMatchers.anyLong());
        Mockito.verify(roleRepository, Mockito.times(0)).save(ArgumentMatchers.any(Role.class));
    }

    /**
     * Тест нга ошибку при добавлении роли, потому что id занят
     */
    @Test
    void addRoleFailedRoleExistsById()
    {
        Role role = new Role(Long.valueOf(1), "Role");
        Mockito.when(roleRepository.findByName(ArgumentMatchers.anyString())).thenReturn(null);
        Mockito.when(roleRepository.getById(ArgumentMatchers.anyLong())).thenReturn(new Role());
        UserServiceException exception = Assertions.assertThrows(UserServiceException.class, () -> {
            userService.addRole(role);
        });
        List<String> errors = new ArrayList<>();
        errors.add("Роль уже существует");

        assertEquals(errors, exception.getErrors());
        Mockito.verify(roleRepository, Mockito.times(1)).findByName(ArgumentMatchers.anyString());
        Mockito.verify(roleRepository, Mockito.times(1)).getById(ArgumentMatchers.anyLong());
        Mockito.verify(roleRepository, Mockito.times(0)).save(ArgumentMatchers.any(Role.class));

    }
    /**
     * Тест на ошибку при добавлении роли, потому что название пустое
     */
    @Test
    void addRoleFailedRoleNameNull()
    {
        Role role = new Role(Long.valueOf(1), null);
        UserServiceException exception = Assertions.assertThrows(UserServiceException.class, () -> {
            userService.addRole(role);
        });
        List<String> errors = new ArrayList<>();
        errors.add("Name не должен быть пустым");

        assertEquals(errors, exception.getErrors());
        Mockito.verify(roleRepository, Mockito.times(0)).findByName(ArgumentMatchers.anyString());
        Mockito.verify(roleRepository, Mockito.times(0)).findById(ArgumentMatchers.anyLong());
        Mockito.verify(roleRepository, Mockito.times(0)).save(ArgumentMatchers.any(Role.class));

    }
    /**
     * Тест на успешное присвоение роли пользователю
     */
    @Test
    void addRoleToUser()
    {
        String login = "Login";
        String roleName = "Role";

        Mockito.when(userRepository.findUserByLogin(login)).thenReturn(new User("login","name","password",new HashSet<>()));
        Mockito.when(roleRepository.findByName(roleName)).thenReturn(new Role());



        try
        {
            userService.addRoleToUser(login,roleName);
        } catch (UserServiceException e) {
            e.printStackTrace();
        }
        Mockito.verify(userRepository, Mockito.times(1)).findUserByLogin(ArgumentMatchers.anyString());
        Mockito.verify(roleRepository, Mockito.times(1)).findByName(ArgumentMatchers.anyString());
        Mockito.verify(userRepository, Mockito.times(1)).save(ArgumentMatchers.any(User.class));
    }

    /**
     * Тест на ошибку присвоения роли пользователю из-за отсутствия логина
     */
    @Test
    void addRoleToUserFailedLoginNull()
    {
        String login = null;
        String roleName = "Role";
        UserServiceException exception = Assertions.assertThrows(UserServiceException.class, () -> {
            userService.addRoleToUser(login,roleName);
        });
        List<String> errors = new ArrayList<>();
        errors.add("Логин не может быть пустым");

        assertEquals(errors, exception.getErrors());
        Mockito.verify(userRepository, Mockito.times(0)).findUserByLogin(ArgumentMatchers.anyString());
        Mockito.verify(roleRepository, Mockito.times(0)).findByName(ArgumentMatchers.anyString());
        Mockito.verify(userRepository, Mockito.times(0)).save(ArgumentMatchers.any(User.class));

    }
    /**
     * Тест на ошибку присвоения роли пользователю из-за отсутствия роли
     */
    @Test
    void addRoleToUserFailedRoleNameNull()
    {
        String login = "Login";
        String roleName = null;
        UserServiceException exception = Assertions.assertThrows(UserServiceException.class, () -> {
            userService.addRoleToUser(login,roleName);
        });
        List<String> errors = new ArrayList<>();
        errors.add("RoleName не может быть пустым");

        assertEquals(errors, exception.getErrors());
        Mockito.verify(userRepository, Mockito.times(0)).findUserByLogin(ArgumentMatchers.anyString());
        Mockito.verify(roleRepository, Mockito.times(0)).findByName(ArgumentMatchers.anyString());
        Mockito.verify(userRepository, Mockito.times(0)).save(ArgumentMatchers.any(User.class));

    }

    /**
     * Тест на ошибку присвоения роли пользователю из-за не существования пользователя
     */
    @Test
    void addRoleToUserFailedUserNotExists()
    {
        String login = "Login";
        String roleName = "Role";
        Mockito.when(userRepository.findUserByLogin(login)).thenReturn(null);
        Mockito.when(roleRepository.findByName(roleName)).thenReturn(new Role());
        UserServiceException exception = Assertions.assertThrows(UserServiceException.class, () -> {
            userService.addRoleToUser(login,roleName);
        });
        List<String> errors = new ArrayList<>();
        errors.add("Пользователя не существует");

        assertEquals(errors, exception.getErrors());
        Mockito.verify(userRepository, Mockito.times(1)).findUserByLogin(ArgumentMatchers.anyString());
        Mockito.verify(roleRepository, Mockito.times(1)).findByName(ArgumentMatchers.anyString());
        Mockito.verify(userRepository, Mockito.times(0)).save(ArgumentMatchers.any(User.class));

    }
    /**
     * Тест на ошибку присвоения роли пользователю из-за не существования роли
     */
    @Test
    void addRoleToUserFailedRoleNotExists()
    {
        String login = "Login";
        String roleName = "Role";
        Mockito.when(userRepository.findUserByLogin(login)).thenReturn(new User());
        Mockito.when(roleRepository.findByName(roleName)).thenReturn(null);
        UserServiceException exception = Assertions.assertThrows(UserServiceException.class, () -> {
            userService.addRoleToUser(login,roleName);
        });
        List<String> errors = new ArrayList<>();
        errors.add("Роли Role не существует");

        assertEquals(errors, exception.getErrors());
        Mockito.verify(userRepository, Mockito.times(1)).findUserByLogin(ArgumentMatchers.anyString());
        Mockito.verify(roleRepository, Mockito.times(1)).findByName(ArgumentMatchers.anyString());
        Mockito.verify(userRepository, Mockito.times(0)).save(ArgumentMatchers.any(User.class));

    }
    /**
     * Тест на ошибку при присвоении роли, потому что она уже есть у пользователя
     */
    @Test
    void addRoleToUserFailedUserHasRole()
    {

        String login = "Login";
        String roleName = "Role";
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(Long.valueOf(0), roleName));

        Mockito.when(userRepository.findUserByLogin(login)).thenReturn(new User(login,null,null,roles));
        Mockito.when(roleRepository.findByName(roleName)).thenReturn(new Role(Long.valueOf(0), roleName));
        UserServiceException exception = Assertions.assertThrows(UserServiceException.class, () -> {
            userService.addRoleToUser(login,roleName);
        });
        List<String> errors = new ArrayList<>();
        errors.add("У пользователя уже есть данная роль");

        assertEquals(errors, exception.getErrors());
        Mockito.verify(userRepository, Mockito.times(1)).findUserByLogin(ArgumentMatchers.anyString());
        Mockito.verify(roleRepository, Mockito.times(1)).findByName(ArgumentMatchers.anyString());
        Mockito.verify(userRepository, Mockito.times(0)).save(ArgumentMatchers.any(User.class));

    }
}