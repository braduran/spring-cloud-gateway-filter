package co.com.filter.response;

import java.util.HashMap;
import java.util.Map;

import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.server.ServerWebExchange;

import co.com.filter.response.jwt.JWT;
import co.com.filter.response.model.Model;
import co.com.filter.response.model.ModelModified;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class BodyToJWSDecorator extends ServerHttpResponseDecorator{

	private final JWT jwtUtil;
	
	private ServerWebExchange exchange;
	
	public BodyToJWSDecorator(ServerHttpResponse delegate, ServerWebExchange exchange, JWT jwtUtil) {
		super(delegate);
		this.exchange = exchange;
		this.jwtUtil = jwtUtil;
	}
	
	@Override
	public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
		ServerHttpResponse response = exchange.getResponse();
		
		ClientResponse cr = ClientResponse.create(response.getStatusCode())
				.headers(h -> h.putAll(response.getHeaders()))
				.body(Flux.from(body))
				.build();
		
		Mono<ModelModified> modifiedBody = cr.bodyToMono(Model.class).flatMap(ir -> {
			return Mono.just(getJws(ir));
		});
		
		BodyInserter<Mono<ModelModified>, ReactiveHttpOutputMessage> bodyInserter = 
				BodyInserters.fromPublisher(modifiedBody, ModelModified.class);
		
		CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange,
                response.getHeaders());
		
        return bodyInserter.insert(outputMessage, new BodyInserterContext())
        		.then(Mono.defer(() -> getDelegate().writeWith(outputMessage.getBody())));		
	}
	
	private ModelModified getJws(Model ir) {
		String jws = jwtUtil.encodeRS256(attributesToMap(ir));
		return ModelModified.builder().data(jws).build();
	}
	
	private Map<String, Object> attributesToMap(Model model){
		Map<String, Object> attributes = new HashMap<>();
		attributes.put("args", model.getArgs());
		attributes.put("headers", model.getHeaders());
		attributes.put("origin", model.getOrigin());
		attributes.put("url", model.getUrl());
		return attributes;
	}
}
