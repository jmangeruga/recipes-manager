package my.jmangeruga.recipes.adapter.security;

import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import javax.servlet.http.HttpServletRequest;

import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class AuthTokenExtractionFilter extends AbstractPreAuthenticatedProcessingFilter {
    private static final String BEARER = "Bearer";
    private static final String BLANK_TOKEN = "";
    private static final String UNKNOWN_PRINCIPAL = "unknown-principal";

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        return UNKNOWN_PRINCIPAL;
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        return ofNullable(request.getHeader(AUTHORIZATION))
            .map(value -> value.replaceFirst(BEARER, ""))
            .map(String::trim)
            .orElse(BLANK_TOKEN);
    }

}
