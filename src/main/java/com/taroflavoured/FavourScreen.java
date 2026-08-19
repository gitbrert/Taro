package com.taroflavoured;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class FavourScreen extends AbstractContainerScreen<FavourMenu> {
    private static final ResourceLocation GUI_TEXTURE = ResourceLocation.fromNamespaceAndPath(
            TaroFlavoured.MOD_ID, "textures/gui/enchanting_table.png");
    private static final ResourceLocation RECIPE_BOOK_BUTTON = ResourceLocation.withDefaultNamespace(
            "recipe_book/button");

    public FavourScreen(FavourMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
        this.titleLabelX = 8;
        this.inventoryLabelX = 8;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        // The supplied texture is the complete GUI background.
        guiGraphics.blit(GUI_TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight, 256, 256);

        // Use the vanilla recipe-book button sprite in the empty area above the book slots.
        guiGraphics.blitSprite(RECIPE_BOOK_BUTTON, leftPos + 14, topPos + 17, 20, 18);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        // The supplied texture has its own framing and intentionally has no title/tier text.
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        // Explicitly render the hovered slot tooltip after the container contents.
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
