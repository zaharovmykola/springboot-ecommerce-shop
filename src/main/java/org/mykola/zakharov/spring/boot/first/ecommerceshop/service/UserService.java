package org.mykola.zakharov.spring.boot.first.ecommerceshop.service;

import org.mykola.zakharov.spring.boot.first.ecommerceshop.dao.UserHibernateDAO;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.entity.RoleResponseModel;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.entity.Roles;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.entity.UserResponseModel;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.entity.Users;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.model.RoleModel;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserHibernateDAO dao;

    public UserResponseModel add(UserModel userModel) {
        Users users =
                Users.builder()
                        .vendor(userModel.getVendor())
                        .password(userModel.getPassword())
                        .build();
        dao.save(users);
        UserResponseModel response =
                UserResponseModel.builder()
                        .status("success")
                        .message("Next user added successfully :" + users.getVendor())
                        .build();
        return response;
    }

    public UserResponseModel getUser(String vendor) {
        List<Users> users = dao.findByVendor(vendor);
        List<UserModel> userModels = users.stream().map((us) ->
                UserModel.builder()
                        .id(us.getId())
                        .vendor(us.getVendor())
                        .password(us.getPassword())
                        .build()
        ).collect(Collectors.toList());
        return UserResponseModel.builder()
                .status("success")
                .users(userModels)
                .build();
    }

    public List<UserModel> getAllUsers() {
        List<Users> users = dao.findAll();
        List<UserModel> userModels = users.stream().map((us) ->
                UserModel.builder()
                        .id(us.getId())
                        .vendor(us.getVendor())
                        .password(us.getPassword())
                        .build()
        ).collect(Collectors.toList());
        return userModels;
    }
}
