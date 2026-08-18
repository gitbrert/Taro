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

    public static final DeferredItem<Item> DIVINE_CRYSTAL = ITEMS.register("divine_crystal", () ->
            new Item(new Item.Properties()
                    .rarity(Rarity.RARE)
                    .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)));

    public static final DeferredItem<Item> CRYSTAL_HEART = ITEMS.register("crystal_heart", () ->
            new Item(new Item.Properties()));

    public static final DeferredItem<Item> BENZENE = ITEMS.register("benzene", () ->
            new Item(new Item.Properties()));

    public static final DeferredItem<Item> EVIL_EYE = ITEMS.register("evil_eye", () ->
            new Item(new Item.Properties()));

    // Phase 2b Blessings
    public static final DeferredItem<Item> BLESSING_DIABOBA = ITEMS.register("blessing_diaboba", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BLESSING_MARZANNA = ITEMS.register("blessing_marzanna", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BLESSING_SIEGFRIED = ITEMS.register("blessing_siegfried", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BLESSING_MELUSINE = ITEMS.register("blessing_melusine", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BLESSING_ANKOU = ITEMS.register("blessing_ankou", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BLESSING_ENKIDU = ITEMS.register("blessing_enkidu", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BLESSING_ILLAPA = ITEMS.register("blessing_illapa", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BLESSING_SON_TINH = ITEMS.register("blessing_son_tinh", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BLESSING_WAYRA = ITEMS.register("blessing_wayra", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BLESSING_HOU_YI = ITEMS.register("blessing_hou_yi", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BLESSING_AH_PUCH = ITEMS.register("blessing_ah_puch", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BLESSING_JUMONG = ITEMS.register("blessing_jumong", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BLESSING_AISHA = ITEMS.register("blessing_aisha", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BLESSING_MARGOT = ITEMS.register("blessing_margot", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BLESSING_AMARU = ITEMS.register("blessing_amaru", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BLESSING_THUY_TINH = ITEMS.register("blessing_thuy_tinh", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BLESSING_SAMJOK_O = ITEMS.register("blessing_samjok_o", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BLESSING_BUDAI = ITEMS.register("blessing_budai", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BLESSING_COSANZEANA = ITEMS.register("blessing_cosanzeana", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BLESSING_LUG = ITEMS.register("blessing_lug", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BLESSING_ROSA_DE_LIMA = ITEMS.register("blessing_rosa_de_lima", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BLESSING_SIDI_AMAR_BOUSSENA = ITEMS.register("blessing_sidi_amar_boussena", () -> new Item(new Item.Properties()));

    public TaroFlavoured(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
        NeoForge.EVENT_BUS.register(new HealthHandler());
    }
}
