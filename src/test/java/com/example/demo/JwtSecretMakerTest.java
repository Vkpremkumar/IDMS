package com.example.demo;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.Test;

import io.jsonwebtoken.Jwts;
import jakarta.xml.bind.DatatypeConverter;

public class JwtSecretMakerTest {
	
	
	
	// This method is used to generate a secret key

	@Test
	public void generateSecretKey() {
		SecretKey key = Jwts.SIG.HS512.key().build();
		String encodedKey = DatatypeConverter.printHexBinary(key.getEncoded());
		System.out.printf("\nKey =[%s]\n", encodedKey);
	}

}
