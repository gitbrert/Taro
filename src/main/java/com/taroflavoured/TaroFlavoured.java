package com.taroflavoured;

import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.common.crafting.IngredientType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

@net.neoforged.fml.common.Mod(TaroFlavoured.MOD_ID)
public class TaroFlavoured {
    public static final String MOD_ID = "taroflavoured";
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MOD_ID);
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(BuiltInRegistries.MENU, MOD_ID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, MOD_ID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, MOD_ID);
    public static final DeferredRegister<IngredientType<?>> INGREDIENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.INGREDIENT_TYPES, MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<FavourMenu>> FAVOUR_MENU =
            MENUS.register("favour_enchanting", () -> IMenuTypeExtension.create(FavourMenu::new));
    public static final DeferredHolder<RecipeType<?>, RecipeType<FavourRecipe>> FAVOUR_RECIPE_TYPE =
            RECIPE_TYPES.register("favour", () -> RecipeType.simple(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(MOD_ID, "favour")));
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<FavourRecipe>> FAVOUR_RECIPE_SERIALIZER =
            RECIPE_SERIALIZERS.register("favour", FavourRecipe.Serializer::new);
    public static final DeferredHolder<IngredientType<?>, IngredientType<EnviousBookIngredient>> ENVIOUS_BOOK_INGREDIENT =
            INGREDIENT_TYPES.register("envious_book", () -> new IngredientType<>(EnviousBookIngredient.CODEC));

    public static final RecipeBookType FAVOUR_RECIPE_BOOK = RecipeBookType.valueOf("TAROFLAVOURED_FAVOUR_BOOK");

    public static final DeferredItem<Item> DIVINE_FRAGMENT = ITEMS.register("divine_fragment", () -> new Item(new Item.Properties().rarity(Rarity.RARE).component(net.minecraft.core.component.DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)));
    public static final DeferredItem<Item> DIVINE_CRYSTAL = ITEMS.register("divine_crystal", () -> new Item(new Item.Properties().rarity(Rarity.RARE).component(net.minecraft.core.component.DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)));
    public static final DeferredItem<Item> CRYSTAL_HEART = ITEMS.register("crystal_heart", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BENZENE = ITEMS.register("benzene", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> EVIL_EYE = ITEMS.register("evil_eye", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> ENVIOUS_BOOK = ITEMS.register("envious_book", () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON).component(net.minecraft.core.component.DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)));

    public static final DeferredItem<Item> MEAD = ITEMS.register("mead", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CATFISH = ITEMS.register("catfish", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CHEERFUL_CLAY_STATUE = ITEMS.register("cheerful_clay_statue", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> MOURNFUL_CLAY_STATUE = ITEMS.register("mournful_clay_statue", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> COMPOUND_BOW = ITEMS.register("compound_bow", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BRONZE_LAUREL = ITEMS.register("bronze_laurel", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> GLOW_JAM = ITEMS.register("glow_jam", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> GOLDEN_APPLE_EMPANADA = ITEMS.register("golden_apple_empanada", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> HONEY_GINGER_TEA = ITEMS.register("honey_ginger_tea", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> GOLDEN_CARROT_CUPCAKE = ITEMS.register("golden_carrot_cupcake", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> OPAL_EARRINGS = ITEMS.register("opal_earrings", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> TUNISIAN_BARB = ITEMS.register("tunisian_barb", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> WOODEN_CROSS = ITEMS.register("wooden_cross", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> NAZAR = ITEMS.register("nazar", () -> new Item(new Item.Properties()));

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

    public static ItemStack createEnviousBook(RegistryAccess registryAccess) {
        ItemStack stack = new ItemStack(ENVIOUS_BOOK.get());
        var enchantmentRegistry = registryAccess.lookupOrThrow(Registries.ENCHANTMENT);
        var bindingCurse = enchantmentRegistry.getOrThrow(Enchantments.BINDING_CURSE);
        ItemEnchantments.Mutable enchantments = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
        enchantments.set(bindingCurse, 1);
        stack.set(net.minecraft.core.component.DataComponents.ENCHANTMENTS, enchantments.toImmutable());
        return stack;
    }

    public TaroFlavoured(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
        MENUS.register(modEventBus);
        RECIPE_TYPES.register(modEventBus);
        RECIPE_SERIALIZERS.register(modEventBus);
        INGREDIENT_TYPES.register(modEventBus);
        net.neoforged.neoforge.common.NeoForge.EVENT_BUS.register(new HealthHandler());
    }

    public static boolean isEnviousBook(ItemStack stack) { return stack.is(ENVIOUS_BOOK.get()); }
}
