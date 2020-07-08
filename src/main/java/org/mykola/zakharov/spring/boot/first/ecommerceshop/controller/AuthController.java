package org.mykola.zakharov.spring.boot.first.ecommerceshop.controller;

import org.mykola.zakharov.spring.boot.first.ecommerceshop.entity.Role;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.model.ResponseModel;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.model.UserRequestModel;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    // Получение ссылки на пользовательскую службу безопасности
    @Autowired
    private AuthService authService;

    @GetMapping("/roles")
    public ResponseEntity<ResponseModel> getAll() {
        return new ResponseEntity<>(authService.getAllRoles(), HttpStatus.OK);
    }

    @PostMapping("/admin/role")
    public ResponseEntity<ResponseModel> createRole(@RequestBody Role role) {
        return new ResponseEntity<>(authService.createRole(role), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/role/{id}")
    public ResponseEntity<ResponseModel> deleteRole(@PathVariable Long id) {
        return new ResponseEntity<>(authService.deleteRole(id), HttpStatus.NO_CONTENT);
    }

    @GetMapping("/role/{id}/users")
    public ResponseEntity<ResponseModel> getUsersByRole(@PathVariable Long id) {
        ResponseModel responseModel =
                authService.getRoleUsers(id);
        return new ResponseEntity<>(
                responseModel,
                (responseModel.getData() != null)
                    ? HttpStatus.OK
                    : HttpStatus.NOT_FOUND
        );
    }

    @PostMapping("/user")
    public ResponseEntity<ResponseModel> createUser(@RequestBody UserRequestModel userRequestModel) {
        return new ResponseEntity<>(authService.createUser(userRequestModel), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/user/{id}")
    public ResponseEntity<ResponseModel> deleteUser(@PathVariable Long id) {
        return new ResponseEntity<>(authService.deleteUser(id), HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/user/check")
    @ResponseBody
    public ResponseEntity<ResponseModel> checkUser(Authentication authentication) {
        ResponseModel responseModel = authService.check(authentication);
        return new ResponseEntity<>(
            responseModel,
            (responseModel.getData() != null)
                ? HttpStatus.OK
                : HttpStatus.UNAUTHORIZED
        );
    }

    @GetMapping("/user/signedout")
    public ResponseEntity<ResponseModel> signedOut() {
        return new ResponseEntity<>(authService.onSignOut(), HttpStatus.NO_CONTENT);
    }

    @GetMapping("/user/onerror")
    public ResponseEntity<ResponseModel> onError() {
        return new ResponseEntity<>(authService.onError(), HttpStatus.UNAUTHORIZED);
    }
}
