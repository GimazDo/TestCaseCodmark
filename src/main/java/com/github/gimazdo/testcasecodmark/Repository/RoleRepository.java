package com.github.gimazdo.testcasecodmark.Repository;

import com.github.gimazdo.testcasecodmark.Model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findByName(String roleName);
}
