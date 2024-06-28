package com.example.demo.webtoken;

import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	public static final String SECRET = "934E2EE3FA8BE75C1FBC691A828C56FB0AAC41658D2ED7888CAA867D51E6681DC03889A82733EF72906416B6AF9586FC256E207E9CD2D4BC24B209EF6F49068D";
	
	public static final Long VALIDITY = TimeUnit.MINUTES.toMillis(240); // Token valid for 4 hours
	
	// This method is used to generate Json web token 
	public String generateToken(UserDetails userDetails) {
		Map<String,String> claims = new HashMap<>();
		claims.put("iss","http://secure.genuinecoder.com");
		return Jwts.builder()
				
		.claims(claims)
		.subject(userDetails.getUsername())
		.issuedAt(Date.from(Instant.now()))
		.expiration(Date.from(Instant.now().plusMillis(VALIDITY)))
		.signWith(generateKey())
		.compact();
	}
	
	private SecretKey generateKey() {
		byte[] decodedKey = Base64.getDecoder().decode(SECRET);
		return Keys.hmacShaKeyFor(decodedKey);
		
	}

	public String extractUsername(String jwt) {
		Claims claims = getClaims(jwt);
		return claims.getSubject();
		
	}

	private Claims getClaims(String jwt) {
		return Jwts.parser()
		.verifyWith(generateKey())
		.build()
		.parseSignedClaims(jwt)
		.getPayload();
		
	}

	public boolean isTokenValid(String jwt) {
		Claims claims = getClaims(jwt);
		return claims.getExpiration().after(Date.from(Instant.now()));		
	}
}
