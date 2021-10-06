package my.jmangeruga.recipes.domain;

import java.util.UUID;

public interface RecipeIdentityProvider {

    default UUID nextIdentity() {
        return UUID.randomUUID();
    }

}
