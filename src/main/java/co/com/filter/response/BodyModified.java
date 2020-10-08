package co.com.filter.response;

import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;

public class BodyModified extends ServerHttpResponseDecorator{

	public BodyModified(ServerHttpResponse delegate) {
		super(delegate);
	}

}
