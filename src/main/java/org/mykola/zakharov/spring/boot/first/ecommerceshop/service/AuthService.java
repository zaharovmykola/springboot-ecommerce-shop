package org.mykola.zakharov.spring.boot.first.ecommerceshop.service;

import org.mykola.zakharov.spring.boot.first.ecommerceshop.dao.RoleHibernateDAO;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.dao.UserHibernateDAO;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.entity.Role;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.entity.User;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.model.ResponseModel;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.model.RoleModel;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.model.UserModel;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.model.UserRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthService {

    @Autowired
    public PasswordEncoder passwordEncoder;

    @Autowired
    private RoleHibernateDAO roleDao;

    @Autowired
    private UserHibernateDAO userDao;

    public ResponseModel createRole(Role role) {
        roleDao.save(role);
        return ResponseModel.builder()
                .status(ResponseModel.SUCCESS_STATUS)
                .message(String.format("%s Role Created", role.getName()))
                .build();
    }

    public ResponseModel createUser(UserRequestModel userRequestModel) {
        User user =
            User.builder()
                .name(userRequestModel.getName().trim())
                .password(passwordEncoder.encode(userRequestModel.getPassword().trim()))
                .role(roleDao.findRoleByName("user"))
                .build();
        userDao.save(user);
        return ResponseModel.builder()
                .status(ResponseModel.SUCCESS_STATUS)
                .message(String.format("%s User Created", user.getName()))
                .build();
    }

    public ResponseModel getAllRoles() {
        List<Role> roles =
                roleDao.findAll(Sort.by("name").ascending());
        List<RoleModel> roleModels =
            roles.stream()
                .map((r) -> RoleModel.builder()
                    .id(r.getId())
                    .name(r.getName())
                    .build())
                .collect(Collectors.toList());
        return ResponseModel.builder()
                .status(ResponseModel.SUCCESS_STATUS)
                .message(String.format("Role List Retrieved Successfully"))
                .data(roleModels)
                .build();
    }

    @Transactional
    public ResponseModel getRoleUsers(Long roleId) {
        Optional<Role> roleOptional = roleDao.findById(roleId);
        if (roleOptional.isPresent()) {
            Role role = roleOptional.get();
            List<UserModel> userModels =
                role.getSetOfUsers().stream().map(user ->
                    UserModel.builder()
                        .name(user.getName())
                        .roleId(user.getRole().getId())
                        .build()
                )
                .collect(Collectors.toList());
            return ResponseModel.builder()
                .status(ResponseModel.SUCCESS_STATUS)
                .message(String.format("List of %s Role Users Retrieved Successfully", role.getName()))
                .data(userModels)
                .build();
        } else {
            return ResponseModel.builder()
                .status(ResponseModel.FAIL_STATUS)
                .message("No Users: Role #" + roleId + " Not Found")
                .build();
        }
    }

    public ResponseModel deleteRole(Long id) {
        roleDao.deleteById(id);
        return ResponseModel.builder()
                .status(ResponseModel.SUCCESS_STATUS)
                .message("Role Deleted")
                .build();
    }

    public ResponseModel deleteUser(Long id, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            User user = userDao.findUserByName(authentication.getName());
            if (user.getId().equals(id)) {
                userDao.deleteById(id);
                return ResponseModel.builder()
                    .status(ResponseModel.SUCCESS_STATUS)
                    .build();
            } else {
                return ResponseModel.builder()
                    .status(ResponseModel.FAIL_STATUS)
                    .build();
            }
        } else {
            return ResponseModel.builder()
                    .status(ResponseModel.FAIL_STATUS)
                    .build();
        }
    }


    public ResponseModel check(Authentication authentication) {
        ResponseModel response = new ResponseModel();
        if (authentication != null && authentication.isAuthenticated()) {
            UserModel userModel = UserModel.builder()
                    .name(authentication.getName())
                    .build();
            response.setStatus(ResponseModel.SUCCESS_STATUS);
            response.setMessage(String.format("User %s signed in", userModel.getName()));
            response.setData(userModel);
        } else {
            response.setStatus(ResponseModel.FAIL_STATUS);
            response.setMessage("User is a guest");
        }
        return response;
    }

    public ResponseModel onSignOut() {
        return ResponseModel.builder()
            .status(ResponseModel.SUCCESS_STATUS)
            .message("Signed out")
            .build();
    }

    public ResponseModel onError() {
        return ResponseModel.builder()
                .status(ResponseModel.FAIL_STATUS)
                .message("Auth error")
                .build();
    }

    public ResponseModel makeUserAdmin(Long id) throws Exception {
        // Получаем из БД объект роли администратора
        Role role = roleDao.findRoleByName("admin");
        Optional<User> userOptional = userDao.findById(id);
        if (userOptional.isPresent()){
            User user = userOptional.get();
            user.setRole(role);
            userDao.save(user);
            return ResponseModel.builder()
                .status(ResponseModel.SUCCESS_STATUS)
                .message(String.format("Admin %s created successfully", user.getName()))
                .build();
        } else {
            return ResponseModel.builder()
                .status(ResponseModel.FAIL_STATUS)
                .message(String.format("User #%s Not Found", id))
                .build();
        }
    }
}
