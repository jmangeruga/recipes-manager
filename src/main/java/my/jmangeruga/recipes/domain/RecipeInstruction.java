package my.jmangeruga.recipes.domain;

import javax.persistence.Embeddable;
import java.util.function.Consumer;

import static my.jmangeruga.recipes.domain.SimpleDomainValidation.notNullOrEmpty;
import static my.jmangeruga.recipes.domain.SimpleDomainValidation.validated;
import static my.jmangeruga.recipes.domain.SimpleDomainValidation.withMaxSize;

@Embeddable
public class RecipeInstruction {

    private static final Consumer<String> MAX_SIZE = withMaxSize(3000, "Recipe instruction description max length [3000] exceeded");

    private String description;

    private RecipeInstruction() {
    }

    public RecipeInstruction(String description) {
        this.description = validated(notNullOrEmpty("A non blank recipe instruction description is mandatory").andThen(MAX_SIZE), description);
    }

    public String getDescription() {
        return description;
    }

}
