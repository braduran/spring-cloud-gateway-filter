package co.com.filter.response;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;

import lombok.NoArgsConstructor;

@Component
public class BodyToJWS extends AbstractGatewayFilterFactory<BodyToJWS.Config>{
	
	public BodyToJWS(ServerCodecConfigurer codecConfigurer) {
		super(Config.class);
	}
	 
	@NoArgsConstructor
	public static class Config {
        //Put the configuration properties for your filter here
    }

	@Override
	public GatewayFilter apply(Config config) {
		return new ModifyResponseGatewayFilter();
	}
}
