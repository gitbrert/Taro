package com.taroflavoured;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.UseItemOnBlockEvent;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = TaroFlavoured.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public final class FavourInteractionHandler {
    private FavourInteractionHandler() {
    }

    @SubscribeEvent
    public static void openFavourMenu(UseItemOnBlockEvent event) {
        if (event.getUsePhase() != UseItemOnBlockEvent.UsePhase.BLOCK) return;

        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        Player player = event.getPlayer();

        if (player == null || !level.getBlockState(pos).is(Blocks.ENCHANTING_TABLE)) return;

        if (!level.isClientSide && player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
            List<RecipeHolder<?>> recipes = new ArrayList<>();
            for (RecipeHolder<FavourRecipe> recipe : level.getRecipeManager().getAllRecipesFor(TaroFlavoured.FAVOUR_RECIPE_TYPE.get())) {
                if (!recipe.id().getPath().equals("favour_sidi_amar_boussena")) recipes.add(recipe);
            }
            serverPlayer.awardRecipes(recipes);

            serverPlayer.openMenu(
                    new SimpleMenuProvider(
                            (containerId, inventory, ignoredPlayer) -> new FavourMenu(containerId, inventory, level, pos),
                            Component.translatable("container.taroflavoured.favour_enchanting")
                    ),
                    buffer -> buffer.writeBlockPos(pos)
            );
        }

        event.cancelWithResult(ItemInteractionResult.sidedSuccess(level.isClientSide));
    }
}
