package dev.Hilligans.ourcraft.Util.Recipe;

import dev.Hilligans.ourcraft.Container.Container;
import dev.Hilligans.ourcraft.Data.Other.IInventory;

import java.util.ArrayList;

public interface IRecipe<T extends Container> {






    class RecipeElement {
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
