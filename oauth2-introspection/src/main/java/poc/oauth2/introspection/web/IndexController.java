package poc.oauth2.introspection.web;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping
public class IndexController {

    @GetMapping(value = "/hello", produces = MediaType.TEXT_PLAIN_VALUE)
    public String hello(Principal principal) {
        return principal.getName() + " says hello";
    }
}
