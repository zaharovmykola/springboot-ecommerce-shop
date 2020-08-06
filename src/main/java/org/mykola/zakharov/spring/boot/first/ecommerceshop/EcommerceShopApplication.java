package org.mykola.zakharov.spring.boot.first.ecommerceshop;

import org.mykola.zakharov.spring.boot.first.ecommerceshop.dao.CategoryHibernateDAO;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.dao.ProductHibernateDAO;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.dao.RoleHibernateDAO;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.dao.UserHibernateDAO;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.entity.Category;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.entity.Product;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.entity.Role;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;

@SpringBootApplication
@PropertySource("classpath:application.properties")
public class EcommerceShopApplication {

	@Value("${tests.unit.strings.image-base64-msft}")
	private String msftImageString;

	@Value("${tests.unit.strings.image-base64-orcl}")
	private String orclImageString;

	@Value("${tests.unit.strings.image-base64-eth}")
	private String ethImageString;

	@Autowired
	public PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(EcommerceShopApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(
			RoleHibernateDAO roleDAO,
			UserHibernateDAO userDAO,
			CategoryHibernateDAO categoryDAO,
			ProductHibernateDAO productDAO
	) {
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
			Category stockCategory = Category.builder().name("stock").build();
			Category cryptoCategory = Category.builder().name("crypto").build();
			Category eMoneyCategory = Category.builder().name("e-money").build();
			categoryDAO.save(stockCategory);
			categoryDAO.save(cryptoCategory);
			categoryDAO.save(eMoneyCategory);
			Product stockMSFTProduct =
					Product.builder()
							.name("MSFT")
							.description("Microsoft Stock")
							.price(new BigDecimal(203.92))
							.quantity(1000)
							.category(stockCategory)
							.image(msftImageString)
							.build();
			Product stockORCLProduct =
					Product.builder()
							.name("ORCL")
							.description("Oracle Stock")
							.price(new BigDecimal(55.82))
							.quantity(2000)
							.category(stockCategory)
							.image(orclImageString)
							.build();
			Product stockORCLProduct2 =
					Product.builder()
							.name("ORCL")
							.description("Oracle Stock")
							.price(new BigDecimal(56.12))
							.quantity(1000)
							.category(stockCategory)
							.image(orclImageString)
							.build();
			Product cryptoEthereumProduct =
					Product.builder()
							.name("ETH")
							.description("Ethereum Cryptocurrency")
							.price(new BigDecimal(232.48))
							.quantity(500)
							.category(cryptoCategory)
							.image(ethImageString)
							.build();
			productDAO.save(stockMSFTProduct);
			productDAO.save(stockORCLProduct);
			productDAO.save(stockORCLProduct2);
			productDAO.save(cryptoEthereumProduct);
		};
	}
}