package dev.hilligans.ourcraft.recipe;

import dev.hilligans.ourcraft.container.Container;
import dev.hilligans.ourcraft.util.registry.IRegistryElement;

public interface IRecipe<T extends Container> extends IRegistryElement {

    RecipeElement[] getOutput();
    RecipeElement[] getInput();

    @Override
    default String getResourceType() {
        return "recipe";
    }

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
