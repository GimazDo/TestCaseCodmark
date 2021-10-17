package com.github.gimazdo.testcasecodmark.Repository;

import com.github.gimazdo.testcasecodmark.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findUserByLogin(String login);
}
