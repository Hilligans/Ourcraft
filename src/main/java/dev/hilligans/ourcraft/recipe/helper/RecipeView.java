package dev.hilligans.ourcraft.recipe.helper;

import dev.hilligans.ourcraft.recipe.IRecipe;

public class RecipeView<T extends IRecipe<?>> {

    public int width;
    public int height;

    public RecipeView(int width, int height) {
        this.width = width;
        this.height = height;
    }


}
