package org.mykola.zakharov.spring.boot.first.ecommerceshop.controller;

import org.mykola.zakharov.spring.boot.first.ecommerceshop.entity.Role;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.model.Cart;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.model.ResponseModel;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.model.UserRequestModel;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    // Получение ссылки на пользовательскую службу безопасности
    @Autowired
    private AuthService authService;

    @Secured("ROLE_ADMIN")
    @GetMapping("admin/roles")
    public ResponseEntity<ResponseModel> getAllRoles() {
        return new ResponseEntity<>(authService.getAllRoles(), HttpStatus.OK);
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("admin/role")
    public ResponseEntity<ResponseModel> createRole(@RequestBody Role role) {
        return new ResponseEntity<>(authService.createRole(role), HttpStatus.CREATED);
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping(value = "admin/role/{id}")
    public ResponseEntity<ResponseModel> deleteRole(@PathVariable Long id) {
        return new ResponseEntity<>(authService.deleteRole(id), HttpStatus.NO_CONTENT);
    }

    @GetMapping("admin/role/{id}/users")
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
        ResponseModel responseModel =
                authService.createUser(userRequestModel);
        System.out.println("responseModel = " + responseModel);
        return new ResponseEntity<>(
                responseModel,
                (responseModel.getMessage().toLowerCase().contains("created"))
                        ? HttpStatus.CREATED
                        : responseModel.getMessage().contains("name")
                        ? HttpStatus.CONFLICT
                        : HttpStatus.BAD_GATEWAY
        );
    }

    @DeleteMapping(value = "/user/{id}")
    public ResponseEntity<ResponseModel> deleteUser(@PathVariable Long id, Authentication authentication) {
        ResponseModel responseModel = authService.deleteUser(id, authentication);
        return new ResponseEntity<>(
                responseModel,
                (responseModel.getStatus().equals(ResponseModel.FAIL_STATUS))
                ? HttpStatus.FORBIDDEN
                : HttpStatus.NO_CONTENT
        );
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
        // после выхода пользователя из аккаунта
        // пытаемся получить объект его корзины покупок из объекта сеанса,
        // и если получили - удаляем корзину из сеанса
        return new ResponseEntity<>(authService.onSignOut(), HttpStatus.NO_CONTENT);
    }

    @GetMapping("/user/onerror")
    public ResponseEntity<ResponseModel> signedOut(HttpSession httpSession) {
        Cart cart = (Cart) httpSession.getAttribute("CART");
        if (cart != null) {
            httpSession.removeAttribute("CART");
        }
        return new ResponseEntity<>(authService.onError(), HttpStatus.UNAUTHORIZED);
    }

    @Secured("ROLE_ADMIN")
    @PatchMapping (value = "/user/{id}/makeadmin")
    public ResponseEntity<ResponseModel> makeUserAdmin(@PathVariable Long id) throws Exception {
        return new ResponseEntity<>(authService.makeUserAdmin(id), HttpStatus.OK);
    }
}
