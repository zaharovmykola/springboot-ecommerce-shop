package org.mykola.zakharov.spring.boot.first.ecommerceshop.service;

import org.mykola.zakharov.spring.boot.first.ecommerceshop.dao.RoleHibernateDAO;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.entity.RoleResponseModel;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.entity.Roles;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.model.RoleModel;
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
        Roles roles =
                Roles.builder()
                .vendor(roleModel.getVendor())
                .build();
        dao.save(roles);
        RoleResponseModel response =
                RoleResponseModel.builder()
                        .status("success")
                        .message("Next role added successfully :" + roles.getVendor())
                        .build();
        return response;
    }

    public RoleResponseModel getRole(String vendor) {
        List<Roles> roles = dao.findByVendor(vendor);
        List <RoleModel> rolesModels = roles.stream().map((rol) ->
                RoleModel.builder()
                .id(rol.getId())
                .vendor(rol.getVendor())
                .build()
        ).collect(Collectors.toList());
        return RoleResponseModel.builder()
                .status("success")
                .roles(rolesModels)
                .build();
    }

    public List<RoleModel> getAllRoles () {
        List<Roles> roles = dao.findAll();
        List <RoleModel> rolesModels = roles.stream().map((rol) ->
                RoleModel.builder()
                        .id(rol.getId())
                        .vendor(rol.getVendor())
                        .build()
        ).collect(Collectors.toList());
         return rolesModels;
    }
}
