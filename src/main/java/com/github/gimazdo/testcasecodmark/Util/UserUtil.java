package com.github.gimazdo.testcasecodmark.Util;

import com.github.gimazdo.testcasecodmark.Exception.UserServiceException;
import com.github.gimazdo.testcasecodmark.Model.User;

import java.util.regex.Pattern;

public class UserUtil {
    public static final Pattern passwordPattern = Pattern.compile("^(?=.*[A-Z])(?=.*\\d).+$");
    public static boolean checkPassword(String password)
    {
        return passwordPattern.matcher(password).matches();
    }

    public static UserServiceException validateUser(User user)
    {
        UserServiceException exception = new UserServiceException();
        if(user.getLogin()==null)
        {
            exception.getErrors().add("Login не может быть пустым");
        }
        if(user.getName()==null)
        {
            exception.getErrors().add("Name не может быть пустым");
        }
        if(user.getPassword()==null)
        {
            exception.getErrors().add("Password не может быть пустым");
        }
        else
        {
            if(!UserUtil.checkPassword(user.getPassword()))
            {
                exception.getErrors().add("Password должен содержать хотя бы 1 заглавную букву и 1 цифру");
            }
        }
        return exception;
    }

}
