package com.geronimo.service;

import com.geronimo.model.Permission;

public interface IPermissionService {

    Permission savePermission(Permission permission);

    Permission findPermissionByName(String name);
}
