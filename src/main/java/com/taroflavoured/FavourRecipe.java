package com.taroflavoured;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

/** A native 1.21.1 recipe used by the Taro enchanting table. */
public final class FavourRecipe implements Recipe<CraftingInput> {
    public static final MapCodec<FavourRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.INT.fieldOf("tier").forGetter(FavourRecipe::tier),
            Codec.STRING.optionalFieldOf("group", "").forGetter(FavourRecipe::getGroup),
            Ingredient.CODEC.listOf().fieldOf("ingredients").forGetter(recipe -> recipe.recipeIngredients),
            ItemStack.CODEC.fieldOf("result").forGetter(FavourRecipe::result)
    ).apply(instance, FavourRecipe::new));

    public final int tier;
    private final String group;
    private final List<Ingredient> recipeIngredients;
    private final ItemStack result;
    private final NonNullList<Ingredient> placementIngredients;

    public FavourRecipe(int tier, String group, List<Ingredient> ingredients, ItemStack result) {
        if (ingredients.isEmpty() || ingredients.size() > 6) {
            throw new IllegalArgumentException("A favour recipe must have 1-6 ingredients");
        }
        this.tier = tier;
        this.group = group;
        this.recipeIngredients = List.copyOf(ingredients);
        this.result = result.copy();

        this.placementIngredients = NonNullList.withSize(6, Ingredient.EMPTY);
        for (int i = 0; i < ingredients.size(); i++) {
            this.placementIngredients.set(i, ingredients.get(i));
        }
    }

    public int tier() {
        return tier;
    }

    public ItemStack result() {
        return result;
    }

    public List<Ingredient> recipeIngredients() {
        return recipeIngredients;
    }

    /**
     * Slot 0 is always the book input. The remaining ingredients are intentionally
     * matched without regard to order, preserving Taro's order-independent recipes.
     */
    @Override
    public boolean matches(CraftingInput input, Level level) {
        if (input.width() != 6 || input.height() != 1) return false;
        if (input.size() != 6) return false;
        if (!recipeIngredients.get(0).test(input.getItem(0))) return false;

        boolean[] used = new boolean[recipeIngredients.size() - 1];
        int supplied = 0;
        for (int slot = 1; slot < 6; slot++) {
            ItemStack stack = input.getItem(slot);
            if (stack.isEmpty()) continue;
            supplied++;

            boolean matched = false;
            for (int i = 1; i < recipeIngredients.size(); i++) {
                int ingredientIndex = i - 1;
                if (!used[ingredientIndex] && recipeIngredients.get(i).test(stack)) {
                    used[ingredientIndex] = true;
                    matched = true;
                    break;
                }
            }
            if (!matched) return false;
        }

        return supplied == recipeIngredients.size() - 1 && allUsed(used);
    }

    private static boolean allUsed(boolean[] used) {
        for (boolean value : used) if (!value) return false;
        return true;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registryAccess) {
        ItemStack output = result.copy();
        if (output.is(TaroFlavoured.ENVIOUS_BOOK.get())) {
            return TaroFlavoured.createEnviousBook(registryAccess);
        }
        return FavourEnchantments.apply(output, registryAccess);
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width >= 6 && height >= 1;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registryAccess) {
        ItemStack output = result.copy();
        if (output.is(TaroFlavoured.ENVIOUS_BOOK.get())) {
            return TaroFlavoured.createEnviousBook(registryAccess);
        }
        return FavourEnchantments.apply(output, registryAccess);
    }

    @Override
    public RecipeSerializer<FavourRecipe> getSerializer() {
        return TaroFlavoured.FAVOUR_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<FavourRecipe> getType() {
        return TaroFlavoured.FAVOUR_RECIPE_TYPE.get();
    }

    @Override
    public String getGroup() {
        return group;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return placementIngredients;
    }

    @Override
    public boolean isIncomplete() {
        return recipeIngredients.stream().anyMatch(Ingredient::hasNoItems);
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(Items.ENCHANTING_TABLE);
    }

    /** Used by the menu's tier-aware recipe-book filtering. */
    public boolean isAvailableAtTier(int availableTier) {
        return availableTier >= tier;
    }
}
