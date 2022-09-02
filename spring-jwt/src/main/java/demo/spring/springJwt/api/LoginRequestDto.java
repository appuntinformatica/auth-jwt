package demo.spring.springJwt.api;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class LoginRequestDto {
	
	@NotBlank
	private String username;

	@NotBlank
	private String password;

}
