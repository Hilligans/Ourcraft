package dev.hilligans.ourcraft.Recipe.RecipeHelper;

import dev.hilligans.ourcraft.Recipe.IRecipe;

public class RecipeView<T extends IRecipe<?>> {

    public int width;
    public int height;

    public RecipeView(int width, int height) {
        this.width = width;
        this.height = height;
    }


}
