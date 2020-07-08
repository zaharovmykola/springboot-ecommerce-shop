package org.mykola.zakharov.spring.boot.first.ecommerceshop.controller;

import org.mykola.zakharov.spring.boot.first.ecommerceshop.model.RoleResponseModel;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.model.UserResponseModel;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.model.UserModel;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.service.RoleService;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/eCommerceShop")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @PostMapping("/addUser")
    public UserResponseModel addInstant(@RequestBody UserModel user) {
        return userService.add(user);
    }

    // /eCommerceShop/getUserByLogin
    @GetMapping("/getUserByLogin/{login}")
    public UserResponseModel getUser(@PathVariable String login) {
        return userService.getUser(login);
    }

    @GetMapping("/getAllUsers")
    public UserResponseModel getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/getUsersByRole/{roleName}")
    public UserResponseModel getUsersByRole(@PathVariable String roleName) {
        return roleService.getRoleUsers(roleName);
    }

    // /eCommerceShop/getUserByLogin/Kievstar
}
