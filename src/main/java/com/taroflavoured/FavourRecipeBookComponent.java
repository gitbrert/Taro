package com.taroflavoured;

import net.minecraft.client.ClientRecipeBook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Vanilla recipe-book UI backed by an isolated ClientRecipeBook containing only
 * Taro Favour recipes. The vanilla component keeps its normal visuals and
 * controls; only its recipe-book data source is replaced.
 */
public final class FavourRecipeBookComponent extends RecipeBookComponent {
    private static final Field BOOK_FIELD;

    static {
        try {
            BOOK_FIELD = RecipeBookComponent.class.getDeclaredField("book");
            BOOK_FIELD.setAccessible(true);
        } catch (ReflectiveOperationException exception) {
            throw new ExceptionInInitializerError(exception);
        }
    }

    @Override
    public void init(int width, int height, Minecraft minecraft, boolean widthTooNarrow, net.minecraft.world.inventory.RecipeBookMenu<?, ?> menu) {
        super.init(width, height, minecraft, widthTooNarrow, menu);
        installFavourBook(minecraft);
        super.recipesUpdated();
    }

    @Override
    public void recipesUpdated() {
        if (this.minecraft != null) {
            installFavourBook(this.minecraft);
        }
        super.recipesUpdated();
    }

    private void installFavourBook(Minecraft minecraft) {
        if (minecraft.player == null || minecraft.level == null) {
            return;
        }

        List<RecipeHolder<FavourRecipe>> recipes = minecraft.level.getRecipeManager()
                .getAllRecipesFor(TaroFlavoured.FAVOUR_RECIPE_TYPE.get());

        ClientRecipeBook favourBook = new FavourClientRecipeBook(
                minecraft.player.getRecipeBook(),
                minecraft.level.registryAccess(),
                recipes
        );

        try {
            BOOK_FIELD.set(this, favourBook);
        } catch (IllegalAccessException exception) {
            throw new IllegalStateException("Unable to install the Favour recipe book", exception);
        }
    }
}
