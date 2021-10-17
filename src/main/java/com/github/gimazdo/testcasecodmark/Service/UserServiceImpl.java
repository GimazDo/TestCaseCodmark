package com.github.gimazdo.testcasecodmark.Service;

import com.github.gimazdo.testcasecodmark.Model.Role;
import com.github.gimazdo.testcasecodmark.Model.User;
import com.github.gimazdo.testcasecodmark.Repository.RoleRepository;
import com.github.gimazdo.testcasecodmark.Repository.UserRepository;
import com.github.gimazdo.testcasecodmark.Exception.UserServiceException;
import com.github.gimazdo.testcasecodmark.Util.UserUtil;
import com.github.gimazdo.testcasecodmark.dto.UserWithoutRoles;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    /**
     * Ищет всех пользователей через репозиторий и конвертирует их в {@link UserWithoutRoles}
     * @return Список пользователей без их ролей
     */
    @Override
    public List<UserWithoutRoles> getAllUsersWithoutRoles() {
        List<UserWithoutRoles> users = new ArrayList<>();
        userRepository.findAll().forEach(user -> users.add(new UserWithoutRoles(user)));
        return  users;
    }

    /**
     * Поиск пользователя по логину
     * @param login - логин пользователя
     * @return {@link User} найденный по логину. Если пользователя не существует, то вернется null.
     */
    @Override
    public User findByLogin(String login) {

        return userRepository.findUserByLogin(login);
    }

    /**
     * Добавление пользователя в БД.
     * @param user - пользователь, которого нужно добавить
     * @throws UserServiceException хранит в себе все ошибки, которые могут произойти во время работы логина.
     * Основная задача - собрать ошибки формато-логического контроля и отправить их пользователю.
     */
    @Override
    @Transactional
    public void addUser(User user) throws UserServiceException {
        UserServiceException exception = new UserServiceException();
        if(user.getLogin()!=null && userRepository.findUserByLogin(user.getLogin())!=null)
        {
            exception.getErrors().add("Login занят");
            throw  exception;
        }
        exception = UserUtil.validateUser(user);
        Set<Role> roles = new HashSet<>();
        if(user.getRoles()!=null)
        {

            for(Role r: user.getRoles())
            {
                Role role = roleRepository.findByName(r.getName());
                if(role!=null)
                {
                    roles.add(role);
                }
                else
                {
                    exception.getErrors().add("Роли " + r.getName() + " не существует");
                }
            }
        }

        if(exception.getErrors().size()>0) {
            throw exception;
        }

        user.setRoles(roles);
        userRepository.save(user);
    }

    /**
     * Функция обновления данных пользователя.
     * @param user - пользователь с новыми данными.
     * @throws UserServiceException - хранит в себе все ошибки, которые могут произойти во время работы логина.
     *      Основная задача - собрать ошибки формато-логического контроля и отправить их пользователю.
     */
    @Override
    public void updateUser(User user) throws UserServiceException
    {
        UserServiceException exception = new UserServiceException();
        if(user.getLogin()==null)
        {
            exception.getErrors().add("Логин не может быть пустым");
        }
        User oldUser = userRepository.findUserByLogin(user.getLogin());
        if(oldUser==null)
        {
            exception.getErrors().add("Пользователя не существует");
            throw  exception;
        }
        if(user.getPassword()!=null) {
            if( UserUtil.checkPassword(user.getPassword()))
            {
                oldUser.setPassword(user.getPassword());
            }
            else
            {
                exception.getErrors().add("Password должен содержать хотя бы 1 заглавную букву и 1 цифру");
            }
        }
        if(user.getName()!=null) {oldUser.setName(user.getName());}
        if(user.getRoles()!=null) {user.getRoles().forEach(p-> {
            try{
                addRoleToUser(oldUser.getLogin(), p.getName());
            }
            catch (UserServiceException e) {
                exception.getErrors().addAll(e.getErrors());
            }
        });}
        if(exception.getErrors().size()>0) {
            throw exception;
        }
        userRepository.save(user);
    }
    /**
     * Функция удаления пользователя.
     * @param login - логин пользователя
     * @throws UserServiceException - хранит в себе все ошибки, которые могут произойти во время работы логина.
     *      Основная задача - собрать ошибки формато-логического контроля и отправить их пользователю.
     */
    @Override
    public void deleteUser(String login) throws UserServiceException {
        if(login==null)
        {
            UserServiceException exception = new UserServiceException();
            exception.getErrors().add("Login не может быть пустым");
            throw  exception;
        }
        User userForDelete = userRepository.findUserByLogin(login);
        if(userForDelete==null)
        {
            UserServiceException exception = new UserServiceException();
            exception.getErrors().add("Пользователя не существует");
            throw  exception;
        }

        userRepository.delete(userForDelete);
    }

    /**
     * Функция добавления роли пользователю
     * @param login логин пользователя, которому добавляется роль
     * @param roleName название роли, которую нужно добавить
     */
    @Transactional
    @Override
    public void addRoleToUser(String login, String roleName) throws UserServiceException {
        UserServiceException exception = new UserServiceException();
        if(login==null)
        {
            exception.getErrors().add("Логин не может быть пустым");
        }
        if(roleName==null)
        {
            exception.getErrors().add("RoleName не может быть пустым");
        }
        if(exception.getErrors().size()>0) {
            throw exception;
        }
        User user = userRepository.findUserByLogin(login);
        Role role = roleRepository.findByName(roleName);

        if(user==null)
        {
            exception.getErrors().add("Пользователя не существует");
        }
        if(role==null)
        {
            exception.getErrors().add("Роли "+roleName + " не существует");
        }
        if(exception.getErrors().size()>0) {
            throw exception;
        }
        if(user.getRoles().contains(role))
        {
            exception.getErrors().add("У пользователя уже есть данная роль");
            throw exception;
        }

        user.getRoles().add(role);
        userRepository.save(user);
    }

    /**
     * Функция добавления новой роли
     * @param role роль, которую нужно добавить
     * @throws UserServiceException выбрасывается, если роль уже существует
     */
    @Override
    public void addRole(Role role) throws UserServiceException {
        UserServiceException exception = new UserServiceException();
        if(role.getName()==null)
        {
            exception.getErrors().add("Name не должен быть пустым");
            throw exception;
        }
        if( roleRepository.findByName(role.getName())!=null || (role.getId()!=null && roleRepository.getById(role.getId())!=null))
        {
            exception.getErrors().add("Роль уже существует");
            throw exception;
        }

        roleRepository.save(role);
    }



}
