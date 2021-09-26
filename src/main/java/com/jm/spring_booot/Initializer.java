package com.jm.spring_booot;

import com.jm.spring_booot.entity.Role;
import com.jm.spring_booot.entity.User;
import com.jm.spring_booot.service.RoleService;
import com.jm.spring_booot.service.UserService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;


@Component
public class Initializer {

    private final UserService userService;
    private final RoleService roleService;

    public Initializer(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostConstruct
    private void postConstruct() {
        Role roleAdmin = new Role("ROLE_ADMIN");
        Role roleUser = new Role("ROLE_USER");
        roleService.addRole(roleAdmin);
        roleService.addRole(roleUser);

        Set<Role> roleSetAdmin = new HashSet<>();
        roleSetAdmin.add(roleService.getRoleByName("ROLE_ADMIN"));
        User admin = new User("adminName", "adminSurname", 1, "admin@gmail.com", "admin", "admin", roleSetAdmin);
        userService.addUser(admin);

        Set<Role> roleSetUser = new HashSet<>();
        roleSetUser.add(roleService.getRoleByName("ROLE_USER"));
        User user = new User("userName", "surnameUser", 2, "user@gmail.com", "user", "user", roleSetUser);
        userService.addUser(user);
    }
}
