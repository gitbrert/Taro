package com.taroflavoured;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class FavourScreen extends AbstractContainerScreen<FavourMenu> {
    private static final ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(
            TaroFlavoured.MOD_ID, "textures/gui/container/favour_enchanting.png");
    private static final ResourceLocation VANILLA_SLOTS = ResourceLocation.withDefaultNamespace(
            "textures/gui/container/enchanting_table.png");

    public FavourScreen(FavourMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, 176, 166);
        this.titleLabelX = 8;
        this.inventoryLabelX = 8;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        guiGraphics.blit(BACKGROUND, leftPos, topPos, 0, 0, imageWidth, imageHeight);

        drawVanillaSlot(guiGraphics, 18, 22);
        drawVanillaSlot(guiGraphics, 137, 22);

        int ingredients = menu.getActiveIngredientCount();
        for (int i = 0; i < ingredients; i++) {
            drawVanillaSlot(guiGraphics, 18 + i * 20, 52);
        }
    }

    private void drawVanillaSlot(GuiGraphics guiGraphics, int x, int y) {
        guiGraphics.blit(VANILLA_SLOTS, leftPos + x, topPos + y, 14, 46, 18, 18);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderLabels(guiGraphics, mouseX, mouseY);
        guiGraphics.drawString(font, Component.literal("Tier " + menu.getTier()), 112, 68, 0x404040, false);
    }
}
