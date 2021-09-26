package com.jm.spring_booot.DAO;

import com.jm.spring_booot.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

@org.springframework.stereotype.Repository
public interface RoleDAO extends JpaRepository<Role, Long> {
    Role getRoleByName(String name);
}
