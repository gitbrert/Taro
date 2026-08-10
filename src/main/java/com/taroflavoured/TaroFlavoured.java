package com.taroflavoured;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(TaroFlavoured.MOD_ID)
public class TaroFlavoured {
    public static final String MOD_ID = "taroflavoured";
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MOD_ID);

    public static final DeferredItem<Item> DIVINE_FRAGMENT = ITEMS.register("divine_fragment", () ->
            new Item(new Item.Properties()
                    .rarity(Rarity.RARE)
                    .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)));

    public static final DeferredItem<Item> DIVINE_FAVOUR = ITEMS.register("divine_favour", () ->
            new Item(new Item.Properties()
                    .rarity(Rarity.RARE)
                    .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)));

    public static final DeferredItem<Item> CRYSTAL_HEART = ITEMS.register("crystal_heart", () ->
            new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAW_ESTUS = ITEMS.register("raw_estus", () ->
            new Item(new Item.Properties()));

    public static final DeferredItem<Item> BENZENE = ITEMS.register("benzene", () ->
            new Item(new Item.Properties()));

    public static final DeferredItem<Item> STABILISED_ESTUS = ITEMS.register("stabilised_estus", () ->
            new Item(new Item.Properties()));

    public TaroFlavoured(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
        NeoForge.EVENT_BUS.register(new HealthHandler());
    }
}
