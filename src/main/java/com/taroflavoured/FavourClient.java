package com.taroflavoured;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterRecipeBookCategoriesEvent;

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
        // Temporarily disabled while inspecting the transformed RecipeBookCategories metadata.
        RecipeBookCategoryDiagnostic.print();
    }
}
