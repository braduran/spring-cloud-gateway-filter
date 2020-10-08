package co.com.filter.response;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.NettyWriteResponseFilter;
import org.springframework.core.Ordered;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

public class ModifyResponseGatewayFilter implements GatewayFilter, Ordered{

	@Nullable
	private final ServerCodecConfigurer codecConfigurer;
	
	public ModifyResponseGatewayFilter(@Nullable ServerCodecConfigurer codecConfigurer) {
		this.codecConfigurer = codecConfigurer;
	}
	
	@Override
	public int getOrder() {
		return NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER -1;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		return chain.filter(exchange.mutate().response(decorate(exchange)).build());
	}
	
	private ServerHttpResponse decorate(ServerWebExchange exchange) {
		return new CustomDecorator(exchange.getResponse(), exchange, codecConfigurer);
	}

}
