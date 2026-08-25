package com.taroflavoured;

import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;

import java.util.List;
import java.util.function.Supplier;

/**
 * Constructor parameters for the runtime-added recipe-book enum values.
 * Kept separate from the main mod class so enum extension does not load mod state early.
 */
public final class FavourRecipeBookEnumParams {
    public static final EnumProxy<RecipeBookCategories> FAVOUR_CATEGORY = new EnumProxy<>(
            RecipeBookCategories.class,
            (Supplier<List<ItemStack>>) () -> List.of(new ItemStack(Items.ENCHANTING_TABLE))
    );

    public static final EnumProxy<RecipeBookType> FAVOUR_RECIPE_BOOK = new EnumProxy<>(
            RecipeBookType.class
    );

    private FavourRecipeBookEnumParams() {
    }
}
