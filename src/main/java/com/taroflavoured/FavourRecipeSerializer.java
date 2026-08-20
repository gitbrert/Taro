package com.taroflavoured;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.RecipeSerializer;

public final class FavourRecipeSerializer implements RecipeSerializer<FavourRecipe> {
    private static final StreamCodec<RegistryFriendlyByteBuf, FavourRecipe> STREAM_CODEC =
            ByteBufCodecs.fromCodecWithRegistries(FavourRecipe.CODEC.codec());

    @Override
    public MapCodec<FavourRecipe> codec() {
        return FavourRecipe.CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, FavourRecipe> streamCodec() {
        return STREAM_CODEC;
    }
}
