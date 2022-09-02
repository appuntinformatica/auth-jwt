package demo.spring.springJwt.api;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import demo.spring.springJwt.util.JwtResponse;
import demo.spring.springJwt.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;

// http://localhost:8080/spring-jwt/api/login
@Slf4j
@RestController
@RequestMapping("/login")
public class LoginResource {
    
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtUtils jwtUtils;
	
    @Autowired
    PasswordEncoder encryptedPasswordEncoder;
	
	@PostMapping("")
	public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequestDto loginRequest) {
		log.info(loginRequest.toString());
		
		
		Authentication authentication = new UsernamePasswordAuthenticationToken(
				loginRequest.getUsername(), 
				loginRequest.getPassword());
		authentication = authenticationManager.authenticate(authentication);
		log.info("auth: {}", authentication.toString());
		
    	SecurityContextHolder.getContext().setAuthentication(authentication);
    	
    	User user = (User) authentication.getPrincipal();
    	
    	List<String> roles = user.getAuthorities().stream()
    	        .map(item -> item.getAuthority())
    	        .collect(Collectors.toList());
    	
    	JwtResponse jwtResponse = jwtUtils.generateJwtResponse(user.getUsername(), roles);

		return ResponseEntity.ok(jwtResponse);
	}
	
	

}