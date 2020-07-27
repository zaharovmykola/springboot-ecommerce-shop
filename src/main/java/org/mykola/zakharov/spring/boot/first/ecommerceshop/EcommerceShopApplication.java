package org.mykola.zakharov.spring.boot.first.ecommerceshop;

import org.mykola.zakharov.spring.boot.first.ecommerceshop.dao.RoleHibernateDAO;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.dao.UserHibernateDAO;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.entity.Role;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class EcommerceShopApplication {

	@Autowired
	public PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(EcommerceShopApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(RoleHibernateDAO roleDAO, UserHibernateDAO userDAO) {
		return args -> {
			roleDAO.save(Role.builder().name("ROLE_ADMIN").build());
			roleDAO.save(Role.builder().name("ROLE_USER").build());
			Role adminRole = roleDAO.findRoleByName("ROLE_ADMIN");
			Role userRole = roleDAO.findRoleByName("ROLE_USER");
			userDAO.save(
					User.builder()
							.name("admin")
							.password(passwordEncoder.encode("AdminPassword1"))
							.role(adminRole)
							.build()
			);
			userDAO.save(
					User.builder()
							.name("one")
							.password(passwordEncoder.encode("UserPassword1"))
							.role(userRole)
							.build()
			);
			userDAO.save(
					User.builder()
							.name("two")
							.password(passwordEncoder.encode("UserPassword2"))
							.role(userRole)
							.build()
			);
			userDAO.save(
					User.builder()
							.name("three")
							.password(passwordEncoder.encode("UserPassword3"))
							.role(userRole)
							.build()
			);
		};
	}
}
