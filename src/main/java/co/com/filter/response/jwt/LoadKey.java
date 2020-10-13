package co.com.filter.response.jwt;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadKey {

	@Value("${jwt.rsaKey}")
	public String rsaKey;
	
    @Bean
    PrivateKey publicKey() throws NoSuchAlgorithmException,InvalidKeySpecException, IOException{
        byte[] privateBytes = Base64.getDecoder().decode(rsaKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateBytes);
    	KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }
}
