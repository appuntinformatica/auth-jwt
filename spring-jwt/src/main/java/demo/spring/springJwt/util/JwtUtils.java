package demo.spring.springJwt.util;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class JwtUtils {
	@Value("${app.jwt.jwtSecret}")
	private String jwtSecret;

	@Value("${app.jwt.jwtExpirationMs}")
	private int jwtExpirationMs;
	
    public JwtResponse generateJwtResponse(String username, List<String> roles) {
    	log.info("username = {}", username);
    	
    	Date expirationDate = new Date((new Date()).getTime() + jwtExpirationMs);
    	
    	JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject(username)
                .claim("roles", roles)
                .expirationTime(expirationDate)
                .issueTime(new Date())
                .build();
    	
    	Payload payload = new Payload(claims.toJSONObject());
    	
    	 JWSObject jwsObject = new JWSObject(new JWSHeader(JWSAlgorithm.HS256),
                 payload);

    	 try {
    		 jwsObject.sign(new MACSigner(jwtSecret));
    	 } catch (JOSEException ex) {
    		 log.error(ex.getMessage(), ex);
    	 }
         
         JwtResponse jwt = JwtResponse.builder()
         		.token(jwsObject.serialize())
         		.username(username)
         		.expirationDate(expirationDate)
         		.roles(roles)
     		.build();
         
         return jwt;
    }

	public UsernamePasswordAuthenticationToken parseToken(String token) {
		byte[] secretKey = jwtSecret.getBytes();
		
		try {
			SignedJWT signedJWT = SignedJWT.parse(token);
			signedJWT.verify(new MACVerifier(secretKey));
			 ConfigurableJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();
	
	        JWSKeySelector<SecurityContext> keySelector = new JWSVerificationKeySelector<>(JWSAlgorithm.HS256,
	                new ImmutableSecret<>(secretKey));
	        jwtProcessor.setJWSKeySelector(keySelector);
	        jwtProcessor.process(signedJWT, null);
	        JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
	        
	        String username = claims.getSubject();
	        var  roles = (List<String>) claims.getClaim("roles");	
	        var authorities = roles == null ? null : roles.stream()
	                .map(SimpleGrantedAuthority::new)
	                .collect(Collectors.toList());
	        
	        return new UsernamePasswordAuthenticationToken(username, null, authorities);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			return null;
		}
	}

	public boolean validateJwtToken(String token, HttpServletResponse response) {
		try {
			byte[] secretKey = jwtSecret.getBytes();
			SignedJWT signedJWT = SignedJWT.parse(token);
			signedJWT.verify(new MACVerifier(secretKey));
			
			return true;
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
		/*
		try {
			log.info("token = {}", token);
			
			
			parseToken(token);
			
			JwtParserBuilder b = Jwts.parserBuilder().setSigningKey(jwtSecret.getBytes());
			JwtParser parser = b.build();
			parser.parseClaimsJws(authToken);
			return true;
	    } catch (SecurityException e) {
	    	log.error("SecurityException JWT signature: {}", e.getMessage());
	    } catch (MalformedJwtException e) {
	    	log.error("Invalid JWT token: {}", e.getMessage());
	    } catch (ExpiredJwtException e) {
	    	log.error("JWT token is expired: {}", e.getMessage());
	    	response.setHeader("jwtMessage", JwtMessages.TOKEN_EXPIRED.name());
	    } catch (UnsupportedJwtException e) {
	    	log.error("JWT token is unsupported: {}", e.getMessage());
	    } catch (IllegalArgumentException e) {
	    	log.error("JWT claims string is empty: {}", e.getMessage());
	    }*/
	    return false;
	}
}
