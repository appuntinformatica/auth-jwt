package demo.spring.springJwt.util;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {

		JwtMessages message = JwtMessages.UNKNOWN_ERROR;
		
		if ( response.getHeader("jwtMessage") != null ) {
			message = JwtMessages.valueOf( response.getHeader("jwtMessage") );
			
		} else if ( authException instanceof BadCredentialsException ) {
			log.warn( "BadCredentialsException: {}", authException.getMessage() );
			message = JwtMessages.BAD_CREDENTIALS;
			
		} else if ( authException instanceof DisabledException ) {
			log.warn( "DisabledException: {}", authException.getMessage() );
			message = JwtMessages.USER_DISABLED;
			
		} else if ( authException instanceof InsufficientAuthenticationException ) {
			log.warn( "InsufficientAuthenticationException: {}", authException.getMessage() );
			message = JwtMessages.INVALID_ID_TOKEN;
			
		} else {
			authException.printStackTrace();	
		}

		//response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized");
		
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.getOutputStream().println( message.name() );
		response.getOutputStream().close();
		
	}

}
