package co.com.filter.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Model {
	private Object args;
	private Headers headers;
	private String origin;
	private String url;
}
