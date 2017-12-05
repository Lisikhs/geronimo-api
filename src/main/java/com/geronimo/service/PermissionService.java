package com.geronimo.service;

import com.geronimo.dao.PermissionRepository;
import com.geronimo.model.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class PermissionService implements IPermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    public Permission savePermission(Permission permission) {
        return permissionRepository.save(permission);
    }

    @Transactional(readOnly = true)
    @Override
    public Permission findPermissionByName(String name) {
        return permissionRepository.findByName(name);
    }
}
