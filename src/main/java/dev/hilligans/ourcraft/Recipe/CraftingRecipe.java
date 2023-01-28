package dev.hilligans.ourcraft.Recipe;

import dev.hilligans.ourcraft.Container.Container;
import dev.hilligans.ourcraft.Container.CraftingContainer;
import dev.hilligans.ourcraft.Item.ItemStack;

public class CraftingRecipe<T extends Container & CraftingContainer> implements IRecipe<T> {

    public boolean shapeless;
    public boolean matchShapeExactly;
    public boolean allowHorizontalFlip;
    public boolean allowVerticalFlip;

    public ItemStack outputStack;


    public CraftingRecipe() {

    }

    public CraftingRecipe(boolean shapeless, boolean matchShapeExactly, boolean allowHorizontalFlip, boolean allowVerticalFlip) {
        this.shapeless = shapeless;
        this.matchShapeExactly = matchShapeExactly;
        this.allowHorizontalFlip = allowHorizontalFlip;
        this.allowVerticalFlip = allowVerticalFlip;
    }


    public ItemStack getCraftingResult(T inventory) {
        return outputStack.copy();
    }


    @Override
    public RecipeElement[] getOutput() {
        return new RecipeElement[0];
    }

    @Override
    public RecipeElement[] getInput() {
        return new RecipeElement[0];
    }
}
