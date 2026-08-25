package com.taroflavoured;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.MapDecorations;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;

/** Matches the vanilla Jungle Explorer Map representation used by Minecraft 1.21.1. */
public final class JungleExplorerMapPredicate {
    private JungleExplorerMapPredicate() {}

    public static boolean matches(ItemStack stack) {
        MapDecorations decorations = stack.get(DataComponents.MAP_DECORATIONS);
        if (decorations == null) return false;

        return decorations.decorations().values().stream().anyMatch(entry -> {
            Holder<MapDecorationType> type = entry.type();
            return type.unwrapKey()
                    .map(key -> key.location().equals(
                            net.minecraft.resources.ResourceLocation.withDefaultNamespace("jungle_temple")))
                    .orElse(false);
        });
    }
}
