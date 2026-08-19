package com.taroflavoured;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(TaroFlavoured.MOD_ID)
public class TaroFlavoured {
    public static final String MOD_ID = "taroflavoured";
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MOD_ID);
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(BuiltInRegistries.MENU, MOD_ID);

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

    // Envious Book is a separate item. Its Curse of Binding component is added
    // when an Envious Book stack is created with the active enchantment registry.
    public static final DeferredItem<Item> ENVIOUS_BOOK = ITEMS.register("envious_book", () ->
            new Item(new Item.Properties()
                    .stacksTo(1)
                    .rarity(Rarity.UNCOMMON)
                    .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)));

    public static final DeferredHolder<MenuType<?>, MenuType<FavourMenu>> FAVOUR_MENU = MENUS.register(
            "favour_enchanting", () -> IMenuTypeExtension.create(FavourMenu::new));

    // Phase 2b Favours
    public static final DeferredItem<Item> FAVOUR_DIABOBA = ITEMS.register("favour_diaboba", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FAVOUR_MARZANNA = ITEMS.register("favour_marzanna", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FAVOUR_SIEGFRIED = ITEMS.register("favour_siegfried", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FAVOUR_MELUSINE = ITEMS.register("favour_melusine", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FAVOUR_ANKOU = ITEMS.register("favour_ankou", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FAVOUR_ENKIDU = ITEMS.register("favour_enkidu", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FAVOUR_ILLAPA = ITEMS.register("favour_illapa", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FAVOUR_SON_TINH = ITEMS.register("favour_son_tinh", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FAVOUR_WAYRA = ITEMS.register("favour_wayra", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FAVOUR_HOU_YI = ITEMS.register("favour_hou_yi", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FAVOUR_AH_PUCH = ITEMS.register("favour_ah_puch", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FAVOUR_JUMONG = ITEMS.register("favour_jumong", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FAVOUR_AISHA = ITEMS.register("favour_aisha", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FAVOUR_MARGOT = ITEMS.register("favour_margot", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FAVOUR_AMARU = ITEMS.register("favour_amaru", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FAVOUR_THUY_TINH = ITEMS.register("favour_thuy_tinh", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FAVOUR_SAMJOK_O = ITEMS.register("favour_samjok_o", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FAVOUR_BUDAI = ITEMS.register("favour_budai", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FAVOUR_COSANZEANA = ITEMS.register("favour_cosanzeana", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FAVOUR_LUG = ITEMS.register("favour_lug", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FAVOUR_ROSA_DE_LIMA = ITEMS.register("favour_rosa_de_lima", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FAVOUR_SIDI_AMAR_BOUSSENA = ITEMS.register("favour_sidi_amar_boussena", () -> new Item(new Item.Properties()));

    public TaroFlavoured(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
        MENUS.register(modEventBus);
        NeoForge.EVENT_BUS.register(new HealthHandler());
    }

    public static boolean isEnviousBook(ItemStack stack) {
        return stack.is(ENVIOUS_BOOK.get());
    }
}
