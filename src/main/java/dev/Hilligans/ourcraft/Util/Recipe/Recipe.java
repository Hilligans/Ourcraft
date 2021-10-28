package dev.Hilligans.ourcraft.Util.Recipe;

public class Recipe {







    public static class RecipeElement {
        public IRecipeComponent component;
        public int slot;
        public int count;

        public RecipeElement(IRecipeComponent component, int slot, int count) {
            this.component = component;
            this.slot = slot;
            this.count = count;
        }
    }



}
