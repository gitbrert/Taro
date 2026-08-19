package com.taroflavoured;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;

/** Applies the enchantments specified by the Phase 2b Favour guide. */
public final class FavourEnchantments {
    private FavourEnchantments() {}

    public static ItemStack apply(ItemStack favour, RegistryAccess registryAccess) {
        ItemEnchantments.Mutable enchantments = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
        HolderLookup.RegistryLookup<Enchantment> registry = registryAccess.lookupOrThrow(Registries.ENCHANTMENT);

        if (favour.is(TaroFlavoured.FAVOUR_DIABOBA.get())) {
            add(enchantments, registry, "minecraft:bane_of_arthropods", 5);
        } else if (favour.is(TaroFlavoured.FAVOUR_MARZANNA.get())) {
            add(enchantments, registry, "minecraft:frost_walker", 2);
            add(enchantments, registry, "main:freezing_protection", 1);
        } else if (favour.is(TaroFlavoured.FAVOUR_SIEGFRIED.get())) {
            add(enchantments, registry, "main:lunge", 3);
            add(enchantments, registry, "minecraft:breach", 4);
            add(enchantments, registry, "minecraft:sweeping_edge", 3);
        } else if (favour.is(TaroFlavoured.FAVOUR_MELUSINE.get())) {
            add(enchantments, registry, "minecraft:lure", 3);
            add(enchantments, registry, "minecraft:luck_of_the_sea", 3);
            add(enchantments, registry, "minecraft:loyalty", 3);
        } else if (favour.is(TaroFlavoured.FAVOUR_ANKOU.get())) {
            add(enchantments, registry, "minecraft:swift_sneak", 3);
            add(enchantments, registry, "minecraft:soul_speed", 3);
        } else if (favour.is(TaroFlavoured.FAVOUR_ENKIDU.get())) {
            add(enchantments, registry, "main:traversal", 1);
        } else if (favour.is(TaroFlavoured.FAVOUR_ILLAPA.get())) {
            add(enchantments, registry, "minecraft:channeling", 1);
            add(enchantments, registry, "minecraft:smite", 5);
        } else if (favour.is(TaroFlavoured.FAVOUR_SON_TINH.get())) {
            add(enchantments, registry, "minecraft:efficiency", 5);
            add(enchantments, registry, "minecraft:unbreaking", 3);
        } else if (favour.is(TaroFlavoured.FAVOUR_WAYRA.get())) {
            add(enchantments, registry, "minecraft:feather_falling", 4);
            add(enchantments, registry, "minecraft:wind_burst", 3);
        } else if (favour.is(TaroFlavoured.FAVOUR_HOU_YI.get())) {
            add(enchantments, registry, "minecraft:infinity", 1);
            add(enchantments, registry, "minecraft:quick_charge", 3);
            add(enchantments, registry, "minecraft:blast_protection", 4);
        } else if (favour.is(TaroFlavoured.FAVOUR_AH_PUCH.get())) {
            add(enchantments, registry, "minecraft:piercing", 4);
            add(enchantments, registry, "minecraft:impaling", 5);
            add(enchantments, registry, "minecraft:projectile_protection", 4);
        } else if (favour.is(TaroFlavoured.FAVOUR_JUMONG.get())) {
            add(enchantments, registry, "minecraft:power", 4);
            add(enchantments, registry, "minecraft:multishot", 1);
        } else if (favour.is(TaroFlavoured.FAVOUR_AISHA.get())) {
            add(enchantments, registry, "minecraft:sharpness", 5);
            add(enchantments, registry, "minecraft:thorns", 3);
        } else if (favour.is(TaroFlavoured.FAVOUR_MARGOT.get())) {
            add(enchantments, registry, "main:zephyr", 1);
        } else if (favour.is(TaroFlavoured.FAVOUR_AMARU.get())) {
            add(enchantments, registry, "minecraft:density", 5);
            add(enchantments, registry, "minecraft:knockback", 2);
            add(enchantments, registry, "minecraft:punch", 2);
        } else if (favour.is(TaroFlavoured.FAVOUR_THUY_TINH.get())) {
            add(enchantments, registry, "minecraft:depth_strider", 3);
            add(enchantments, registry, "minecraft:riptide", 3);
            add(enchantments, registry, "minecraft:aqua_affinity", 1);
            add(enchantments, registry, "minecraft:respiration", 3);
        } else if (favour.is(TaroFlavoured.FAVOUR_SAMJOK_O.get())) {
            add(enchantments, registry, "minecraft:flame", 1);
            add(enchantments, registry, "minecraft:fire_aspect", 2);
            add(enchantments, registry, "minecraft:fire_protection", 4);
        } else if (favour.is(TaroFlavoured.FAVOUR_BUDAI.get())) {
            add(enchantments, registry, "minecraft:fortune", 3);
            add(enchantments, registry, "minecraft:looting", 3);
        } else if (favour.is(TaroFlavoured.FAVOUR_COSANZEANA.get())) {
            add(enchantments, registry, "minecraft:mending", 1);
        } else if (favour.is(TaroFlavoured.FAVOUR_LUG.get())) {
            add(enchantments, registry, "main:reach", 1);
        } else if (favour.is(TaroFlavoured.FAVOUR_ROSA_DE_LIMA.get())) {
            add(enchantments, registry, "minecraft:silk_touch", 1);
            add(enchantments, registry, "minecraft:protection", 4);
        } else if (favour.is(TaroFlavoured.FAVOUR_SIDI_AMAR_BOUSSENA.get())) {
            add(enchantments, registry, "main:warding_armour", 1);
        }

        favour.set(DataComponents.ENCHANTMENTS, enchantments.toImmutable());
        return favour;
    }

    private static void add(ItemEnchantments.Mutable enchantments, HolderLookup.RegistryLookup<Enchantment> registry, String id, int level) {
        ResourceKey<Enchantment> key = ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse(id));
        registry.get(key).ifPresent(holder -> enchantments.set(holder, level));
    }
}
