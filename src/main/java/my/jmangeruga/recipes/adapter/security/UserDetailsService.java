package my.jmangeruga.recipes.adapter.security;


import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

class UserDetailsService implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

    private final AuthService authService;

    UserDetailsService(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) throws AuthenticationException {
        final String authenticatedUserId = authService.userIdFromToken(token.getCredentials().toString());
        return new User(authenticatedUserId);
    }

}
