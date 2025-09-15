package dev.hilligans.ourcraft.recipe.helper;

import dev.hilligans.ourcraft.recipe.IRecipe;
import dev.hilligans.engine.util.registry.IRegistryElement;

public class RecipeView<T extends IRecipe<?>> implements IRegistryElement {

    public int width;
    public int height;

    public RecipeView(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public String getResourceName() {
        return "recipeView";
    }

    @Override
    public String getResourceOwner() {
        return "ourcraft";
    }

    @Override
    public String getResourceType() {
        return "recipe_view";
    }
}
