package co.com.filter.request;

import lombok.NoArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class ModifyHeaders extends AbstractGatewayFilterFactory<ModifyHeaders.Config> {

    public ModifyHeaders(ServerCodecConfigurer codecConfigurer) {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            HttpHeaders headerRq = exchange.getRequest().getHeaders();
            request = request.mutate()
                    .header("document-id", headerRq.getFirst("id"))
                    .header("document-type", headerRq.getFirst("type"))
                    .build();
            return chain.filter(exchange.mutate().request(request).build());
        };
    }

    @NoArgsConstructor
    public static class Config {
        //Put the configuration properties for your filter here
    }
}
