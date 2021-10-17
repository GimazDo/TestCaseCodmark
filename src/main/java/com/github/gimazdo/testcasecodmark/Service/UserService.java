package com.github.gimazdo.testcasecodmark.Service;

import com.github.gimazdo.testcasecodmark.Model.Role;
import com.github.gimazdo.testcasecodmark.Model.User;
import com.github.gimazdo.testcasecodmark.Exception.UserServiceException;
import com.github.gimazdo.testcasecodmark.dto.UserWithoutRoles;

import java.util.List;
import java.util.Set;

public interface UserService
{
    List<UserWithoutRoles> getAllUsersWithoutRoles();

    User findByLogin(String login);

    void addUser(User user) throws UserServiceException;

    void deleteUser(String login) throws UserServiceException;

    void addRoleToUser(String login, String roleName) throws UserServiceException;

    void addRole(Role role) throws UserServiceException;

    void updateUser(User user) throws UserServiceException;
}
