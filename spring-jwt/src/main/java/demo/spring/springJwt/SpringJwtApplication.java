package demo.spring.springJwt;

import demo.spring.springJwt.model.Roles;
import demo.spring.springJwt.model.entity.RoleEntity;
import demo.spring.springJwt.model.entity.UserEntity;
import demo.spring.springJwt.service.RoleService;
import demo.spring.springJwt.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

/* https://www.vincenzoracca.com/blog/framework/spring/jwt/ */

@SpringBootApplication
public class SpringJwtApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringJwtApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	CommandLineRunner run(UserService userService, RoleService roleService) {
		return args -> {
			roleService.save(new RoleEntity(1L, Roles.ROLE_USER.name()));
			roleService.save(new RoleEntity(2L, Roles.ROLE_ADMIN.name()));

			userService.save(new UserEntity(1L, "rossi", "1234", new ArrayList<>()));
			userService.save(new UserEntity(2L, "bianchi", "1234", new ArrayList<>()));

			userService.addRoleToUser("rossi", Roles.ROLE_USER.name());
			userService.addRoleToUser("bianchi", Roles.ROLE_ADMIN.name());
			userService.addRoleToUser("bianchi", Roles.ROLE_USER.name());
		};
	}

}
