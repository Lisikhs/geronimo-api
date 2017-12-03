package com.geronimo.service;

import com.geronimo.model.Role;

public interface IRoleService {

    Role saveRole(Role role);

    Role findRoleByName(String name);
}
