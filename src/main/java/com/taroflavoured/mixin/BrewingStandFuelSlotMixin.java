package com.taroflavoured.mixin;

import com.taroflavoured.TaroFlavoured;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.world.inventory.BrewingStandMenu$FuelSlot")
public abstract class BrewingStandFuelSlotMixin {
    @Inject(method = "mayPlaceItem", at = @At("HEAD"), cancellable = true)
    private static void taroFlavoured$allowRawEstus(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (stack.is(TaroFlavoured.RAW_ESTUS.get())) {
            cir.setReturnValue(true);
        }
    }
}
