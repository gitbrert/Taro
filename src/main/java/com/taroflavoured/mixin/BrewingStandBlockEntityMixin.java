package com.taroflavoured.mixin;

import com.taroflavoured.TaroFlavoured;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BrewingStandBlockEntity.class)
public abstract class BrewingStandBlockEntityMixin {
    @Shadow private NonNullList<ItemStack> items;
    @Shadow int fuel;

    @Inject(method = "serverTick", at = @At("HEAD"))
    private static void taroFlavoured$refillWithRawEstus(
            Level level,
            BlockPos pos,
            BlockState state,
            BrewingStandBlockEntity entity,
            CallbackInfo ci
    ) {
        BrewingStandBlockEntityMixin mixin = (BrewingStandBlockEntityMixin) (Object) entity;
        if (mixin.fuel <= 0 && mixin.items.get(4).is(TaroFlavoured.RAW_ESTUS.get())) {
            mixin.fuel = 20;
            mixin.items.get(4).shrink(1);
            entity.setChanged();
        }
    }

    @Inject(method = "canPlaceItem", at = @At("HEAD"), cancellable = true)
    private void taroFlavoured$allowRawEstus(int slot, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (slot == 4 && stack.is(TaroFlavoured.RAW_ESTUS.get())) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "canPlaceItemThroughFace", at = @At("HEAD"), cancellable = true)
    private void taroFlavoured$allowRawEstusThroughFace(
            int slot,
            ItemStack stack,
            Direction direction,
            CallbackInfoReturnable<Boolean> cir
    ) {
        if (slot == 4 && stack.is(TaroFlavoured.RAW_ESTUS.get())) {
            cir.setReturnValue(true);
        }
    }
}
