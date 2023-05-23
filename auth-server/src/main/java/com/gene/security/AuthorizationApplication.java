package com.gene.security;

import com.gene.security.auth.AuthenticationService;
import com.gene.security.auth.RegisterRequest;
import com.gene.security.user.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

import static com.gene.security.user.Role.*;

@SpringBootApplication
public class AuthorizationApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthorizationApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(
			AuthenticationService service
	) {
		return args -> {
		 List<Role> adminRoles = new ArrayList<>();
		adminRoles.add(ADMIN);
		adminRoles.add(MANAGER);
		List<Role> userRoles = new ArrayList<>();
			userRoles.add(EMPLOY);

			var admin = RegisterRequest.builder()
					.firstname("Admin")
					.lastname("Admin")
					.email("surangakumarage@gmail.com")
					.password("password")
					.roles(adminRoles)
					.build();
			System.out.println("Admin token: " + service.register(admin).getAccessToken());

			var manager = RegisterRequest.builder()
					.firstname("User")
					.lastname("User")
					.email("user@mail.com")
					.password("password")
					.roles(userRoles)
					.build();
			System.out.println("User token: " + service.register(manager).getAccessToken());

	};
}
}


