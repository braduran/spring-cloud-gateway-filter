package co.com.filter.response;

import java.security.Key;

import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.lang.Nullable;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.server.ServerWebExchange;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class CustomDecorator extends ServerHttpResponseDecorator{

    private ServerWebExchange exchange;
    private ServerCodecConfigurer codecConfigurer;
	
	public CustomDecorator(ServerHttpResponse delegate, ServerWebExchange exchange, @Nullable ServerCodecConfigurer codecConfigurer) {
		super(delegate);
        this.exchange = exchange;
        this.codecConfigurer = codecConfigurer;
	}
	
	@Override
    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, "application/json");
        
        ClientResponse clientResponse = prepareClientResponse(body, httpHeaders);
        Mono<ModelModified> modifiedBody = clientResponse.bodyToMono(Model.class)
                .flatMap(model -> Mono.just(getJws(model)));
        
        BodyInserter<Mono<ModelModified>, ReactiveHttpOutputMessage> bodyInserter = BodyInserters.fromPublisher(modifiedBody, ModelModified.class);
        ServerHttpResponse response = exchange.getResponse();
        
        CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange,
                response.getHeaders());
        return bodyInserter.insert(outputMessage, new BodyInserterContext()).then(Mono.defer(() -> process(outputMessage)));
    }
	
	 public ClientResponse prepareClientResponse(Publisher<? extends DataBuffer> body, HttpHeaders httpHeaders) {
		ServerHttpResponse response = exchange.getResponse();
		
		ClientResponse.Builder builder = getCodecConfigurer() != null ?
				ClientResponse.create(response.getStatusCode(),getCodecConfigurer().getReaders())
				: ClientResponse.create(response.getStatusCode());
		return builder.headers(headers -> headers.putAll(httpHeaders)).body(Flux.from(body)).build();
	}
	 
	public Mono<Void> process(CachedBodyOutputMessage outputMessage){
        Flux<DataBuffer> messageBody = outputMessage.getBody();
        HttpHeaders headers = getDelegate().getHeaders();
        if (!headers.containsKey(HttpHeaders.TRANSFER_ENCODING)) {
            messageBody = messageBody
                    .doOnNext(data -> headers.setContentLength(data.readableByteCount()));
        }
        return getDelegate().writeWith(messageBody);
    }
	
	private ModelModified getJws(Model model) {
		Key clave = Keys.secretKeyFor(SignatureAlgorithm.HS256);
		String jws = Jwts.builder().claim("data", model).signWith(clave).compact();
		return ModelModified.builder().data(jws).build();
	}
	 
	public ServerCodecConfigurer getCodecConfigurer(){
        return this.codecConfigurer;
    }

    public void setCodecConfigurer(ServerCodecConfigurer codecConfigurer){
        this.codecConfigurer = codecConfigurer;
    }
}
