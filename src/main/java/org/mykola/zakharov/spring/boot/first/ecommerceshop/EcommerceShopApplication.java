package org.mykola.zakharov.spring.boot.first.ecommerceshop;

import org.mykola.zakharov.spring.boot.first.ecommerceshop.dao.RoleHibernateDAO;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.entity.Role;
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
	public CommandLineRunner initData(RoleHibernateDAO roleDAO) {
		return args -> {
			roleDAO.save(Role.builder().name("admin").build());
			roleDAO.save(Role.builder().name("user").build());
		};
	}
}
