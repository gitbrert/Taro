package com.taroflavoured;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class FavourScreen extends AbstractContainerScreen<FavourMenu> {
    private static final ResourceLocation VANILLA_GUI = ResourceLocation.withDefaultNamespace(
            "textures/gui/container/enchanting_table.png");

    public FavourScreen(FavourMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
        this.titleLabelX = 8;
        this.inventoryLabelX = 8;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(VANILLA_GUI, leftPos, topPos, 0, 0, imageWidth, imageHeight);

        // Reuse the vanilla enchanting-table panel and remold its upper area into an anvil-style layout.
        guiGraphics.fill(leftPos + 5, topPos + 7, leftPos + 171, topPos + 82, 0xFFC6C6C6);

        drawArrow(guiGraphics, leftPos + 66, topPos + 30);
        drawVanillaSlot(guiGraphics, 18, 22);
        drawVanillaSlot(guiGraphics, 137, 22);

        int ingredients = menu.getActiveIngredientCount();
        for (int i = 0; i < ingredients; i++) {
            drawVanillaSlot(guiGraphics, 18 + i * 20, 52);
        }
    }

    private void drawVanillaSlot(GuiGraphics guiGraphics, int x, int y) {
        guiGraphics.blit(VANILLA_GUI, leftPos + x, topPos + y, 14, 46, 18, 18);
    }

    private void drawArrow(GuiGraphics guiGraphics, int x, int y) {
        int dark = 0xFF4A4A4A;
        int light = 0xFFBEBEBE;

        guiGraphics.fill(x, y, x + 42, y + 2, light);
        guiGraphics.fill(x, y + 2, x + 42, y + 4, dark);

        for (int i = 0; i < 7; i++) {
            guiGraphics.fill(x + 42 - i, y + 1 - i, x + 44 - i, y + 3 + i, dark);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderLabels(guiGraphics, mouseX, mouseY);
        guiGraphics.drawString(font, Component.literal("Tier " + menu.getTier()), 112, 68, 0x404040, false);
    }
}
