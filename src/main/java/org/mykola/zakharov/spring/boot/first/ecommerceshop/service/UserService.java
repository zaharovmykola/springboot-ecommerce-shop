package org.mykola.zakharov.spring.boot.first.ecommerceshop.service;

import org.mykola.zakharov.spring.boot.first.ecommerceshop.dao.RoleHibernateDAO;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.dao.UserHibernateDAO;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.model.UserResponseModel;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.entity.User;
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
    private UserHibernateDAO userDao;

    @Autowired
    private RoleHibernateDAO roleDao;

    public UserResponseModel add(UserModel userModel) {
        User user =
                User.builder()
                        .login(userModel.getLogin())
                        .password(userModel.getPassword())
                        .role(roleDao.findRoleByName("user"))
                        .build();
        userDao.save(user);
        UserResponseModel response =
                UserResponseModel.builder()
                        .status("success")
                        .message("Next user added successfully :" + user.getLogin())
                        .build();
        return response;
    }

    public UserResponseModel getUser(String login) {
        User user = userDao.findUserByLogin(login);
        if (user != null){
            UserModel userModel = UserModel.builder()
                    .id(user.getId())
                    .login(user.getLogin())
                    // .password(user.getPassword())
                    .build();
            return UserResponseModel.builder()
                    .status("success")
                    .user(userModel)
                    .build();
        } else {
            return UserResponseModel.builder()
                    .status("success")
                    .message("User " + login + " was not found")
                    .build();
        }
    }

    public UserResponseModel getAllUsers() {
        List<User> users = userDao.findAll();
        List<UserModel> userModels = users.stream().map((us) ->
                UserModel.builder()
                        .id(us.getId())
                        .login(us.getLogin())
                        .password(us.getPassword())
                        .build()
        ).collect(Collectors.toList());
        return UserResponseModel.builder()
                .status("success")
                .users(userModels)
                .build();
    }
}
