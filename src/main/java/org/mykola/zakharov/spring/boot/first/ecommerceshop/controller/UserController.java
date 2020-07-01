package org.mykola.zakharov.spring.boot.first.ecommerceshop.controller;

import org.mykola.zakharov.spring.boot.first.ecommerceshop.entity.RoleResponseModel;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.entity.UserResponseModel;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.model.RoleModel;
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
    private UserService service;

    @PostMapping("/addUser")
    public UserResponseModel addInstant(@RequestBody UserModel user) {
        return service.add(user);
    }

    @GetMapping("/getUsersByVendor/{vendor}")
    public UserResponseModel getUser(@PathVariable String vendor) {
        return service.getUser(vendor);
    }

    @GetMapping("/getAllUsers")
    public List<UserModel> getAllUsers() {
        return service.getAllUsers();
    }
}
