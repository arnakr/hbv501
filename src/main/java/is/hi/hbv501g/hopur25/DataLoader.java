package is.hi.hbv501g.hopur25.loaders; // Place this under the loaders package

import is.hi.hbv501g.hopur25.persistence.entities.Recipe; // Adjust the import path as necessary
import is.hi.hbv501g.hopur25.persistence.repositories.RecipeRepository; // Adjust the import path as necessary
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final RecipeRepository recipeRepository;

    public DataLoader(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (recipeRepository.count() == 0) {
            Recipe recipe1 = new Recipe("Pancakes", List.of("Hveiti", "Egg", "Mjólk", "Sykur", "Lyftiduft"), 20, "Hrærið hráefnum saman og bakið");
            Recipe recipe2 = new Recipe("Spaghetti Bolognese", List.of("Spaghetti", "Tómatar", "Hakk"), 30, "Steikið hakk, sjóðið spaghetti og blandið saman á pönnu");
            recipeRepository.save(recipe1);
            recipeRepository.save(recipe2);
        }
    }
}
