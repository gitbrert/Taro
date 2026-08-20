package com.taroflavoured;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * A recipe for the custom enchanting-table interface.
 *
 * Book + benzene + evil eye -> Envious Book (Tier 0)
 * Envious Book + favour ingredients -> Favour
 */
public record FavourRecipe(int tier, String[] ingredients, ItemStack favour) {
    private static final List<FavourRecipe> RECIPES = createRecipes();

    public boolean matches(int availableTier, ItemStack input, List<ItemStack> suppliedIngredients) {
        if (availableTier < tier) return false;
        if (!input.is(net.minecraft.world.item.Items.BOOK) && !TaroFlavoured.isEnviousBook(input)) return false;
        if (suppliedIngredients.size() < ingredients.length) return false;

        boolean[] used = new boolean[ingredients.length];
        for (ItemStack supplied : suppliedIngredients) {
            if (supplied.isEmpty()) continue;
            boolean matched = false;
            for (int i = 0; i < ingredients.length; i++) {
                if (!used[i] && matchesIngredient(ingredients[i], supplied)) {
                    used[i] = true;
                    matched = true;
                    break;
                }
            }
            if (!matched) return false;
        }

        for (boolean matched : used) if (!matched) return false;
        return true;
    }

    private static boolean matchesIngredient(String ingredientId, ItemStack stack) {
        if (ingredientId.equals("minecraft:any_carpet")) {
            ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
            return id.getNamespace().equals("minecraft") && id.getPath().endsWith("_carpet");
        }
        return BuiltInRegistries.ITEM.getOptional(ResourceLocation.parse(ingredientId))
                .map(stack::is)
                .orElse(false);
    }

    public static FavourRecipe find(int tier, ItemStack input, List<ItemStack> ingredients) {
        // Tier 0: Book + exactly one Benzene and one Evil Eye -> Envious Book.
        if (input.is(net.minecraft.world.item.Items.BOOK)) {
            int nonEmpty = 0;
            boolean benzene = false;
            boolean evilEye = false;
            for (ItemStack ingredient : ingredients) {
                if (ingredient.isEmpty()) continue;
                nonEmpty++;
                if (ingredient.is(TaroFlavoured.BENZENE.get())) benzene = true;
                else if (ingredient.is(TaroFlavoured.EVIL_EYE.get())) evilEye = true;
                else return null;
            }
            if (nonEmpty == 2 && benzene && evilEye) {
                return new FavourRecipe(0,
                        new String[]{"taroflavoured:benzene", "taroflavoured:evil_eye"}, ItemStack.EMPTY);
            }
        }

        for (FavourRecipe recipe : RECIPES) {
            if (recipe.matches(tier, input, ingredients)) return recipe;
        }
        return null;
    }

    private static List<FavourRecipe> createRecipes() {
        List<FavourRecipe> recipes = new ArrayList<>();

        // Tier 1
        recipes.add(recipe(1, "minecraft:spider_eye", "minecraft:cobweb", "minecraft:fermented_spider_eye", TaroFlavoured.FAVOUR_DIABOBA));
        recipes.add(recipe(1, "minecraft:snow_block", "minecraft:powder_snow_bucket", "minecraft:blue_ice", TaroFlavoured.FAVOUR_MARZANNA));
        recipes.add(recipe(1, "minecraft:iron_sword", "matchainfused:mead", "minecraft:goat_horn", TaroFlavoured.FAVOUR_SIEGFRIED));
        recipes.add(recipe(1, "matchainfused:catfish", "minecraft:bricks", "minecraft:phantom_membrane", TaroFlavoured.FAVOUR_MELUSINE));
        recipes.add(recipe(1, "minecraft:soul_sand", "minecraft:bone", "minecraft:echo_shard", TaroFlavoured.FAVOUR_ANKOU));
        recipes.add(recipe(1, "minecraft:leather_boots", "minecraft:filled_map", "minecraft:boat", TaroFlavoured.FAVOUR_ENKIDU));

        // Tier 2
        recipes.add(recipe(2, "minecraft:lightning_rod", "matchainfused:mournful_clay_statue", "minecraft:firework_star", "matchainfused:copper_spear", TaroFlavoured.FAVOUR_ILLAPA));
        recipes.add(recipe(2, "minecraft:golden_pickaxe", "create:limestone", "minecraft:bamboo", "minecraft:golden_hoe", TaroFlavoured.FAVOUR_SON_TINH));
        recipes.add(recipe(2, "minecraft:wind_charge", "matchainfused:cheerful_clay_statue", "minecraft:phantom_membrane", "create:windmill_sail", TaroFlavoured.FAVOUR_WAYRA));
        recipes.add(recipe(2, "minecraft:emerald_block", "minecraft:spectral_arrow", "matchainfused:compound_bow", "minecraft:magma_block", TaroFlavoured.FAVOUR_HOU_YI));
        recipes.add(recipe(2, "minecraft:axolotl_bucket", "matchainfused:stone_spear", "minecraft:wither_skeleton_skull", "minecraft:jungle_explorer_map", TaroFlavoured.FAVOUR_AH_PUCH));
        recipes.add(recipe(2, "minecraft:spore_blossom", "matchainfused:bronze_laurel", "matchainfused:compound_bow", "minecraft:sniffer_egg", TaroFlavoured.FAVOUR_JUMONG));
        recipes.add(recipe(2, "minecraft:mourner_pottery_sherd", "matcha:nazar", "mr_magic_mirrors:chaos_mirror", "minecraft:pufferfish", TaroFlavoured.FAVOUR_AISHA));
        recipes.add(recipe(2, "minecraft:red_mushroom", "minecraft:music_disc_relic", "minecraft:amethyst_shard", "matchainfused:glow_jam", TaroFlavoured.FAVOUR_MARGOT));

        // Tier 3
        recipes.add(recipe(3, "minecraft:packed_mud", "minecraft:prismarine_shard", "minecraft:dragon_breath", "minecraft:feather", "minecraft:glass", TaroFlavoured.FAVOUR_AMARU));
        recipes.add(recipe(3, "minecraft:fire_coral", "minecraft:turtle_scute", "minecraft:tide_armor_trim_smithing_template", "minecraft:nautilus_shell", "minecraft:horn_coral", TaroFlavoured.FAVOUR_THUY_TINH));
        recipes.add(recipe(3, "minecraft:gold_block", "minecraft:feather", "taroflavoured:divine_crystal", "minecraft:sunflower", "minecraft:magma_block", TaroFlavoured.FAVOUR_SAMJOK_O));
        recipes.add(recipe(3, "minecraft:emerald_block", "matchainfused:golden_apple_empanada", "matchainfused:honey_ginger_tea", "matchainfused:golden_carrot_cupcake", "minecraft:diamond_block", TaroFlavoured.FAVOUR_BUDAI));
        recipes.add(recipe(3, "matchainfused:opal_earrings", "matchainfused:tunisian_barb", "minecraft:enchanted_golden_apple", "minecraft:wildflowers", "matchainfused:wooden_cross", TaroFlavoured.FAVOUR_COSANZEANA));
        recipes.add(recipe(3, "create:engineers_goggles", "minecraft:saddle", "matchainfused:netherite_spear", "minecraft:lead", "minecraft:honeycomb", TaroFlavoured.FAVOUR_LUG));
        recipes.add(recipe(3, "minecraft:loom", "minecraft:potato", "minecraft:wither_rose", "minecraft:phantom_membrane", "minecraft:azalea_leaves", TaroFlavoured.FAVOUR_ROSA_DE_LIMA));
        recipes.add(recipe(3, "minecraft:any_carpet", TaroFlavoured.FAVOUR_SIDI_AMAR_BOUSSENA));

        return List.copyOf(recipes);
    }

    private static FavourRecipe recipe(int tier, String ingredient, net.neoforged.neoforge.registries.DeferredItem<net.minecraft.world.item.Item> result) { return new FavourRecipe(tier, new String[]{ingredient}, new ItemStack(result.get())); }
    private static FavourRecipe recipe(int tier, String a, String b, String c, net.neoforged.neoforge.registries.DeferredItem<net.minecraft.world.item.Item> result) { return new FavourRecipe(tier, new String[]{a, b, c}, new ItemStack(result.get())); }
    private static FavourRecipe recipe(int tier, String a, String b, String c, String d, net.neoforged.neoforge.registries.DeferredItem<net.minecraft.world.item.Item> result) { return new FavourRecipe(tier, new String[]{a, b, c, d}, new ItemStack(result.get())); }
    private static FavourRecipe recipe(int tier, String a, String b, String c, String d, String e, net.neoforged.neoforge.registries.DeferredItem<net.minecraft.world.item.Item> result) { return new FavourRecipe(tier, new String[]{a, b, c, d, e}, new ItemStack(result.get())); }
}
