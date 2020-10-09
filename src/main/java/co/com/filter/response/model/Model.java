package co.com.filter.response.model;

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
	private Object headers;
	private String origin;
	private String url;
}
