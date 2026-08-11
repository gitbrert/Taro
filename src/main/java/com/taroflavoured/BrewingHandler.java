package com.taroflavoured;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.brewing.PotionBrewEvent;

public class BrewingHandler {
    @SubscribeEvent
    public void onBrewingPre(PotionBrewEvent.Pre event) {
        ItemStack ingredient = event.getItem(3);

        if (ingredient.is(Items.BLAZE_POWDER)) {
            event.setCanceled(true);
            return;
        }

        if (ingredient.is(TaroFlavoured.RAW_ESTUS.get())) {
            event.setItem(3, new ItemStack(Items.BLAZE_POWDER, ingredient.getCount()));
        }
    }

    @SubscribeEvent
    public void onBrewingPost(PotionBrewEvent.Post event) {
        ItemStack ingredient = event.getItem(3);

        if (ingredient.is(Items.BLAZE_POWDER)) {
            event.setItem(3, new ItemStack(TaroFlavoured.RAW_ESTUS.get(), ingredient.getCount()));
        }
    }
}
