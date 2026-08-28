package com.taroflavoured;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.crafting.ICustomIngredient;
import net.neoforged.neoforge.common.crafting.IngredientType;

import java.util.stream.Stream;

/** Recipe-book ingredient that accepts any Envious Book stack, including the crafted cursed version. */
public record EnviousBookIngredient() implements ICustomIngredient {
    public static final MapCodec<EnviousBookIngredient> CODEC = MapCodec.unit(new EnviousBookIngredient());

    @Override
    public boolean test(ItemStack stack) {
        return TaroFlavoured.isEnviousBook(stack);
    }

    @Override
    public Stream<ItemStack> getItems() {
        return Stream.of(new ItemStack(TaroFlavoured.ENVIOUS_BOOK.get()));
    }

    @Override
    public boolean isSimple() {
        return false;
    }

    @Override
    public IngredientType<?> getType() {
        return TaroFlavoured.ENVIOUS_BOOK_INGREDIENT.get();
    }
}
