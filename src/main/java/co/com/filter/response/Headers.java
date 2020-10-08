package co.com.filter.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Headers {
	@JsonProperty("Accept")
	private String accept;
	@JsonProperty("Accept-Encoding")
	private String acceptEncoding;
	@JsonProperty("Cache-Control")
	private String cacheControl;
	@JsonProperty("Content-Type")
	private String contentType;
	@JsonProperty("Host")
	private String host;
	@JsonProperty("Postman-Token")
	private String postmanToken;
	@JsonProperty("User-Agent")
	private String userAgent;
	@JsonProperty("X-Amzn-Trace-Id")
	private String xAmzTraceId;
}
