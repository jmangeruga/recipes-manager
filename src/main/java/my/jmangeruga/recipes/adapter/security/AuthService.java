package my.jmangeruga.recipes.adapter.security;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthService {

    private final StaticSecurityConfig staticSecurityConfig;

    AuthService(StaticSecurityConfig staticSecurityConfig) {
        this.staticSecurityConfig = staticSecurityConfig;
    }

    String userIdFromToken(String authToken) {
        /*
        In a real implementation for the chosen token authentication approach, this code would communicate with
        a remote authentication service that owns the credentials and is able to validate a given token.
        Communication with that service is preferably done through HTTPS, so that risk of exposing sensible information is
        mitigated.
         */
        return Optional.ofNullable(staticSecurityConfig.getTokens().get(authToken))
            .orElseThrow(() -> new BadCredentialsException("Provided token is invalid"));
    }

}
