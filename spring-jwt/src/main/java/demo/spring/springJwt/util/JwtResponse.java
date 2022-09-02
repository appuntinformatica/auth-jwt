package demo.spring.springJwt.util;

import java.util.Date;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class JwtResponse {
	
	private String token;
	private String username;
	private Date expirationDate;
	private List<String> roles;
  
}
