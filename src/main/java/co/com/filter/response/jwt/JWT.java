package co.com.filter.response.jwt;

import java.security.PrivateKey;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JWT {
	private final PrivateKey key;
	
	@Value("${jwt.expiration:3600000}")
	private long expiration;

    public String encodeRS256(Map<String, Object> claims){
		long currentMillis = System.currentTimeMillis() + expiration;
		Date dateExpiration = new Date(currentMillis);
		return Jwts.builder()
				.setClaims(claims)
				.setExpiration(dateExpiration)
				.signWith(key)
				.compact();
    }
}
