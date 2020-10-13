package co.com.filter.response;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.NettyWriteResponseFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;

import co.com.filter.response.jwt.JWT;
import reactor.core.publisher.Mono;

public class BodyToJWSFilter implements GatewayFilter, Ordered{
	
	private final JWT jwtUtil;
	
	public BodyToJWSFilter(JWT jwtUtil) {
		this.jwtUtil = jwtUtil;
	}
	
	@Override
	public int getOrder() {
		return NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER - 1;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		return chain.filter(exchange.mutate().response(decorate(exchange)).build());
	}
	
	private ServerHttpResponse decorate(ServerWebExchange exchange) {
		return new BodyToJWSDecorator(exchange.getResponse(), exchange, jwtUtil);
	}

}
