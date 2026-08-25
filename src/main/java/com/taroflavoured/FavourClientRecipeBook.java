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
    private final RecipeCollection favourCollection;

    public FavourClientRecipeBook(ClientRecipeBook source, RegistryAccess registryAccess, List<RecipeHolder<FavourRecipe>> recipes) {
        setBookSettings(source.getBookSettings());

        for (RecipeHolder<FavourRecipe> recipe : recipes) {
            add(recipe.id());
        }

        List<RecipeHolder<?>> genericRecipes = List.copyOf(recipes);
        this.favourCollection = new RecipeCollection(registryAccess, genericRecipes);
        this.favourCollection.initialize(this);
    }

    @Override
    public List<RecipeCollection> getCollection(RecipeBookCategories category) {
        return category == RecipeBookCategories.CRAFTING_MISC ? List.of(favourCollection) : List.of();
    }

    @Override
    public List<RecipeCollection> getCollections() {
        return List.of(favourCollection);
    }
}
