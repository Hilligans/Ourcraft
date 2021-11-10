package dev.Hilligans.ourcraft.Recipe.RecipeHelper;

import dev.Hilligans.ourcraft.Recipe.IRecipe;

public class RecipeView<T extends IRecipe<?>> {

    public int width;
    public int height;

    public RecipeView(int width, int height) {
        this.width = width;
        this.height = height;
    }


}
