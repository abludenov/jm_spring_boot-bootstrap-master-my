package com.jm.spring_booot.DAO;

import com.jm.spring_booot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDAO extends JpaRepository<User, Long> {
    User getUserByUsername(String username);
}
