package org.mykola.zakharov.spring.boot.first.ecommerceshop;

import org.mykola.zakharov.spring.boot.first.ecommerceshop.dao.RoleHibernateDAO;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.dao.UserHibernateDAO;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.entity.Role;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.entity.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EcommerceShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcommerceShopApplication.class, args);
	}
	@Bean
	public CommandLineRunner initData(RoleHibernateDAO roleDAO, UserHibernateDAO userDAO) {
		return args -> {
			roleDAO.save(Role.builder().name("admin").build());
			roleDAO.save(Role.builder().name("user").build());
			Role adminRole = roleDAO.findRoleByName("admin");
			Role userRole = roleDAO.findRoleByName("user");
			userDAO.save(User.builder().name("admin").password("1").role(adminRole).build());
			userDAO.save(User.builder().name("u1").password("p1").role(userRole).build());
			userDAO.save(User.builder().name("u2").password("p2").role(userRole).build());
			userDAO.save(User.builder().name("u3").password("p3").role(userRole).build());
		};
	}
}
