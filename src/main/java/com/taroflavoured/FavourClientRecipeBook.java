package com.taroflavoured;

import net.minecraft.client.ClientRecipeBook;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.List;

/**
 * Client-only recipe book data used by the Favour screen.
 * It deliberately contains only Taro's Favour recipes, while retaining the
 * player's normal recipe-book settings.
 */
public final class FavourClientRecipeBook extends ClientRecipeBook {
    public FavourClientRecipeBook(ClientRecipeBook source, RegistryAccess registryAccess, List<RecipeHolder<FavourRecipe>> recipes) {
        this.copyOverData(source);

        for (RecipeHolder<FavourRecipe> recipe : recipes) {
            ResourceLocation id = recipe.id();
            // Sidi Amar Boussena has its own Quran-based unlock and must not be
            // exposed by the general Favour recipe-book population.
            if (id.getPath().equals("favour_sidi_amar_boussena")) {
                continue;
            }
            this.add(recipe);
        }

        this.setupCollections(recipes.stream()
                .filter(recipe -> !recipe.id().getPath().equals("favour_sidi_amar_boussena"))
                .map(recipe -> (RecipeHolder<?>) recipe)
                .toList(), registryAccess);
    }
}
