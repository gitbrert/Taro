package com.taroflavoured;

import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.inventory.RecipeBookType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterRecipeBookCategoriesEvent;

import java.util.List;

@EventBusSubscriber(modid = TaroFlavoured.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class FavourClient {
    private FavourClient() {
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(TaroFlavoured.FAVOUR_MENU.get(), FavourScreen::new);
    }

    @SubscribeEvent
    public static void registerRecipeBookCategories(RegisterRecipeBookCategoriesEvent event) {
        RecipeBookCategories favourCategory = RecipeBookCategories.valueOf("TAROFLAVOURED_FAVOURS");
        event.registerBookCategories(RecipeBookType.CRAFTING, List.of(favourCategory));
        event.registerRecipeCategoryFinder(TaroFlavoured.FAVOUR_RECIPE_TYPE.get(), recipe -> favourCategory);
    }

    @SubscribeEvent
    public static void diagnostic(net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent.LoggingIn event) {
        RecipeBookCategoryDiagnostic.print();
    }
}
