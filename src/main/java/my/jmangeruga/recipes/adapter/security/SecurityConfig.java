package my.jmangeruga.recipes.adapter.security;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletResponse;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
@EnableConfigurationProperties(StaticSecurityConfig.class)
class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static final RequestMatcher PUBLIC_URLS = new OrRequestMatcher(
        new AntPathRequestMatcher("/error")
    );

    private final PreAuthenticatedAuthenticationProvider authProvider;
    private final AuthService authService;

    SecurityConfig(AuthService authService) {
        super();
        this.authProvider = new PreAuthenticatedAuthenticationProvider();
        this.authService = authService;
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) {
        authProvider.setPreAuthenticatedUserDetailsService(new UserDetailsService(authService));
        authProvider.setUserDetailsChecker(new AccountStatusUserDetailsChecker());
        auth.authenticationProvider(authProvider);
    }

    @Override
    public void configure(final WebSecurity web) {
        web.ignoring().requestMatchers(PUBLIC_URLS);
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
            .sessionManagement()
            .sessionCreationPolicy(STATELESS)
            .and()
            .authenticationProvider(authProvider)
            .addFilterBefore(authTokenExtractionFilter(), AnonymousAuthenticationFilter.class)
            .exceptionHandling().authenticationEntryPoint(unauthorizedEntryPoint())
            .and()
            .authorizeRequests()
            .anyRequest()
            .authenticated().and()
            .csrf().disable()
            .formLogin().disable()
            .httpBasic().disable()
            .logout().disable();
    }

    AuthTokenExtractionFilter authTokenExtractionFilter() throws Exception {
        final AuthTokenExtractionFilter filter = new AuthTokenExtractionFilter();
        filter.setAuthenticationManager(authenticationManager());
        return filter;
    }

    public AuthenticationEntryPoint unauthorizedEntryPoint() {
        return (request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }

}
