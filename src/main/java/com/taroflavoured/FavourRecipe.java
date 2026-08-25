package com.taroflavoured;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.List;

/** A Favour recipe displayed by the vanilla recipe-book UI and used by the Favour menu. */
public final class FavourRecipe implements CraftingRecipe {
    private final int tier;
    private final String group;
    private final NonNullList<Ingredient> ingredients;
    private final ItemStack result;

    public FavourRecipe(int tier, String group, List<Ingredient> ingredients, ItemStack result) {
        this.tier = tier;
        this.group = group;
        this.ingredients = NonNullList.withSize(ingredients.size(), Ingredient.EMPTY);
        for (int i = 0; i < ingredients.size(); i++) {
            this.ingredients.set(i, expandEnviousBookIngredient(ingredients.get(i)));
        }
        this.result = result;
    }

    private static Ingredient expandEnviousBookIngredient(Ingredient ingredient) {
        if (!ingredient.test(new ItemStack(TaroFlavoured.ENVIOUS_BOOK.get()))) return ingredient;

        ItemStack craftedBook = new ItemStack(TaroFlavoured.ENVIOUS_BOOK.get());
        var bindingCurse = BuiltInRegistries.ENCHANTMENT.getHolderOrThrow(Enchantments.BINDING_CURSE);
        ItemEnchantments.Mutable enchantments = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
        enchantments.set(bindingCurse, 1);
        craftedBook.set(net.minecraft.core.component.DataComponents.ENCHANTMENTS, enchantments.toImmutable());

        // StackedContents uses ItemStack stacking IDs, which include components. Include
        // both the plain recipe-book form and the actual crafted form so the vanilla
        // recipe-book craftability calculation recognizes the crafted Envious Book.
        return Ingredient.ofStacks(new ItemStack(TaroFlavoured.ENVIOUS_BOOK.get()), craftedBook);
    }

    public int tier() { return tier; }

    private static boolean ingredientMatches(Ingredient ingredient, ItemStack supplied) {
        // Envious Books are deliberately item-compatible with the recipe ingredient even
        // though the crafted book carries the Binding Curse component.
        if (ingredient.test(new ItemStack(TaroFlavoured.ENVIOUS_BOOK.get()))) {
            return TaroFlavoured.isEnviousBook(supplied);
        }
        return ingredient.test(supplied);
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        boolean[] used = new boolean[ingredients.size()];
        int nonEmpty = 0;
        for (int slot = 0; slot < input.size(); slot++) {
            ItemStack supplied = input.getItem(slot);
            if (supplied.isEmpty()) continue;
            nonEmpty++;
            boolean matched = false;
            for (int i = 0; i < ingredients.size(); i++) {
                if (!used[i] && ingredientMatches(ingredients.get(i), supplied)) {
                    used[i] = true;
                    matched = true;
                    break;
                }
            }
            if (!matched) return false;
        }
        if (nonEmpty != ingredients.size()) return false;
        for (boolean matched : used) if (!matched) return false;
        return true;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) { return result.copy(); }

    @Override
    public boolean canCraftInDimensions(int width, int height) { return width >= 6 && height >= 1; }

    @Override
    public String getGroup() { return group; }

    @Override
    public NonNullList<Ingredient> getIngredients() { return ingredients; }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) { return result.copy(); }

    @Override
    public CraftingBookCategory category() { return CraftingBookCategory.MISC; }

    @Override
    public RecipeSerializer<?> getSerializer() { return TaroFlavoured.FAVOUR_RECIPE_SERIALIZER.get(); }

    @Override
    public RecipeType<?> getType() { return TaroFlavoured.FAVOUR_RECIPE_TYPE.get(); }

    @Override
    public ItemStack getToastSymbol() { return new ItemStack(TaroFlavoured.ENVIOUS_BOOK.get()); }

    public static class Serializer implements RecipeSerializer<FavourRecipe> {
        public static final MapCodec<FavourRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.INT.fieldOf("tier").forGetter(FavourRecipe::tier),
                Codec.STRING.optionalFieldOf("group", "").forGetter(FavourRecipe::getGroup),
                Ingredient.CODEC_NONEMPTY.listOf().fieldOf("ingredients").forGetter(recipe -> recipe.ingredients),
                ItemStack.SINGLE_ITEM_CODEC.fieldOf("result").forGetter(recipe -> recipe.result)
        ).apply(instance, FavourRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, FavourRecipe> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT, FavourRecipe::tier,
                ByteBufCodecs.STRING_UTF8, FavourRecipe::getGroup,
                Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), recipe -> recipe.ingredients,
                ItemStack.STREAM_CODEC, recipe -> recipe.result,
                FavourRecipe::new
        );

        @Override public MapCodec<FavourRecipe> codec() { return CODEC; }
        @Override public StreamCodec<RegistryFriendlyByteBuf, FavourRecipe> streamCodec() { return STREAM_CODEC; }
    }
}
