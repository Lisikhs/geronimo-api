package com.geronimo.config;

import com.geronimo.model.Permission;
import com.geronimo.model.Role;
import com.geronimo.model.User;
import com.geronimo.service.IPermissionService;
import com.geronimo.service.IRoleService;
import com.geronimo.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Component
public class Bootstrap {

    private static Logger LOG = LoggerFactory.getLogger(Bootstrap.class);

    @Autowired
    private IUserService userService;

    @Autowired
    private IPermissionService permissionService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        createRolesAndPermissions();
        createRootUser();
        createJohnDoeUser();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private void createRolesAndPermissions() {

        Permission homePermission = permissionService.findPermissionByName("HOME");
        if (homePermission == null) {
            homePermission = new Permission("HOME");
            permissionService.savePermission(homePermission);
        }

        Permission adminHomePermission = permissionService.findPermissionByName("ADMIN_HOME");
        if (adminHomePermission == null) {
            adminHomePermission = new Permission("ADMIN_HOME");
            permissionService.savePermission(adminHomePermission);
        }

        Role adminRole = roleService.findRoleByName("ADMIN");
        if (adminRole == null) {
            adminRole = new Role("ADMIN");
            adminRole.getPermissions().add(adminHomePermission);

            roleService.saveRole(adminRole);
        }

        Role userRole = roleService.findRoleByName("USER");
        if (userRole == null) {
            userRole = new Role("USER");
            userRole.getPermissions().add(homePermission);

            roleService.saveRole(userRole);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private void createRootUser() {
        User user = userService.getUserByUsername("root");
        if (user == null) {
            LOG.info("Creating root user since it's not in the database yet");
            user = new User("root", passwordEncoder.encode("root"));
            user.getRoles().add(roleService.findRoleByName("ADMIN"));

            userService.saveOrUpdateUser(user);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private void createJohnDoeUser() {
        User user = userService.getUserByUsername("john");
        if (user == null) {
            user = new User("john", passwordEncoder.encode("doe"));
            user.getRoles().add(roleService.findRoleByName("USER"));

            userService.saveOrUpdateUser(user);
        }
    }
}
