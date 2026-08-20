package com.taroflavoured;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

/** Handles the one Favour recipe that is gated behind obtaining the Quran. */
public final class FavourRecipeUnlocks {
    private static final ResourceLocation ADVANCEMENT = ResourceLocation.fromNamespaceAndPath(
            TaroFlavoured.MOD_ID, "unlock_sidi_amar_boussena");
    private static final String CRITERION = "has_quran";

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (!(player instanceof ServerPlayer serverPlayer) || serverPlayer.tickCount % 20 != 0) {
            return;
        }

        AdvancementHolder advancement = serverPlayer.server.getAdvancements().get(ADVANCEMENT);
        if (advancement == null) {
            return;
        }

        AdvancementProgress progress = serverPlayer.getAdvancements().getOrStartProgress(advancement);
        if (progress.isDone() || !hasQuran(serverPlayer)) {
            return;
        }

        // The advancement reward unlocks taroflavoured:sidi_amar_boussena for the player.
        serverPlayer.getAdvancements().award(advancement, CRITERION);
    }

    private static boolean hasQuran(ServerPlayer player) {
        for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
            ItemStack stack = player.getInventory().getItem(slot);
            if (stack.isEmpty()) {
                continue;
            }

            ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
            if ("quran".equals(id.getPath())) {
                return true;
            }
        }
        return false;
    }
}
