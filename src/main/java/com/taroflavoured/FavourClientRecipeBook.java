package com.taroflavoured;

import net.minecraft.client.ClientRecipeBook;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.List;

/**
 * Client-only recipe book data used by the Favour screen.
 * It contains only the Favour recipes supplied by the component.
 */
public final class FavourClientRecipeBook extends ClientRecipeBook {
    private final List<RecipeCollection> favourCollections;

    public FavourClientRecipeBook(ClientRecipeBook source, RegistryAccess registryAccess, List<RecipeHolder<FavourRecipe>> recipes) {
        setBookSettings(source.getBookSettings());

        ClientRecipeBook favourBook = new ClientRecipeBook();
        favourBook.setBookSettings(source.getBookSettings());
        for (RecipeHolder<FavourRecipe> recipe : recipes) {
            favourBook.add(recipe);
        }

        List<RecipeHolder<?>> genericRecipes = List.copyOf(recipes);
        favourBook.setupCollections(genericRecipes, registryAccess);
        this.favourCollections = List.copyOf(favourBook.getCollection(RecipeBookCategories.CRAFTING_MISC));
    }

    @Override
    public List<RecipeCollection> getCollection(RecipeBookCategories category) {
        return category == RecipeBookCategories.CRAFTING_MISC ? favourCollections : List.of();
    }

    @Override
    public List<RecipeCollection> getCollections() {
        return favourCollections;
    }
}
