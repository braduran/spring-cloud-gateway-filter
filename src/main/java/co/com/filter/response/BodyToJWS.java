package co.com.filter.response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;

import co.com.filter.response.jwt.JWT;
import lombok.NoArgsConstructor;

@Component
public class BodyToJWS extends AbstractGatewayFilterFactory<BodyToJWS.Config>{
	
	@Autowired
	private JWT jwtUtil;
	
	public BodyToJWS(ServerCodecConfigurer codecConfigurer) {
		super(Config.class);
	}
	 
	@NoArgsConstructor
	public static class Config {
        //Put the configuration properties for your filter here
    }

	@Override
	public GatewayFilter apply(Config config) {
		return new BodyToJWSFilter(jwtUtil);
	}
}
