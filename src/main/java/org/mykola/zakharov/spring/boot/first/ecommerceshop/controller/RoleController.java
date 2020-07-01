package org.mykola.zakharov.spring.boot.first.ecommerceshop.controller;

import org.mykola.zakharov.spring.boot.first.ecommerceshop.entity.RoleResponseModel;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.model.RoleModel;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/eCommerceShop")
public class RoleController {

    @Autowired
    private RoleService service;

    @PostMapping("/addRole")
    public RoleResponseModel addInstant(@RequestBody RoleModel role) {
        return service.add(role);
    }

    @GetMapping("/getRolesByVendor/{vendor}")
    public RoleResponseModel getRole(@PathVariable String vendor) {
        return service.getRole(vendor);
    }

    @GetMapping("/getAllRoles")
    public List<RoleModel> getAllRoles() {
        return service.getAllRoles();
    }

}
