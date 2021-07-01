package co.com.filter.service;

import co.com.filter.service.model.ModelRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ExposedService {

    @PostMapping("/post")
    public String headersRqToHeadersRs(@RequestHeader Map<String, String> headers,
                                       @RequestBody ModelRequest request){
        System.out.println("***** HEADERS *****");
        System.out.println(headers.toString());
        System.out.println("***** BODY *****");
        System.out.println(request.getData().toString());
        return "OK";
    }

}
