package my.jmangeruga.recipes.adapter.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(prefix="static-security")
public class StaticSecurityConfig {

    private final Map<String, String> tokens = new HashMap<>();

    public Map<String, String> getTokens() {
        return tokens;
    }
}
