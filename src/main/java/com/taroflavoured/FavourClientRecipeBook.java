package com.taroflavoured;

import net.minecraft.client.ClientRecipeBook;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Client-only recipe book data used by the Favour screen.
 * It contains only the Favour recipes supplied by the component.
 */
public final class FavourClientRecipeBook extends ClientRecipeBook {
    public FavourClientRecipeBook(ClientRecipeBook source, RegistryAccess registryAccess, List<RecipeHolder<FavourRecipe>> recipes) {
        setBookSettings(source.getBookSettings());

        for (RecipeHolder<FavourRecipe> recipe : recipes) {
            add(recipe.id());
        }

        List<RecipeHolder<?>> genericRecipes = new ArrayList<>(recipes);
        setupCollections(genericRecipes, registryAccess);
    }
}
