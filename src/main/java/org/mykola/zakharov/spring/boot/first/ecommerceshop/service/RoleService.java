package org.mykola.zakharov.spring.boot.first.ecommerceshop.service;

import org.mykola.zakharov.spring.boot.first.ecommerceshop.dao.RoleHibernateDAO;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.model.RoleResponseModel;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.entity.Role;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.model.RoleModel;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.model.UserModel;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.model.UserResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoleService {

    @Autowired
    private RoleHibernateDAO dao;

    public RoleResponseModel add(RoleModel roleModel) {
        Role role =
                Role.builder()
                .name(roleModel.getName())
                .build();
        dao.save(role);
        RoleResponseModel response =
                RoleResponseModel.builder()
                        .status("success")
                        .message("Next role added successfully :" + role.getName())
                        .build();
        return response;
    }

    public UserResponseModel getRoleUsers(String roleName) {
        Role role = dao.findRoleByName(roleName);
        if (role != null) {
            List<UserModel> userModels =
                role.getSetOfUsers().stream().map(us ->
                    UserModel.builder()
                            .id(us.getId())
                            .login(us.getLogin())
                            .password(us.getPassword())
                            .build()
                )
                .collect(Collectors.toList());
        return UserResponseModel.builder()
                .status("success")
                .users(userModels)
                .build();
        } else {
            return UserResponseModel.builder()
                    .status("success")
                    .message("No users: Role " + roleName + "not found")
                    .build();
        }
    }

    public List<RoleModel> getAllRoles () {
        List<Role> roles = dao.findAll();
        List <RoleModel> rolesModels = roles.stream().map((rol) ->
                RoleModel.builder()
                        .id(rol.getId())
                        .name(rol.getName())
                        .build()
        ).collect(Collectors.toList());
         return rolesModels;
    }
}
