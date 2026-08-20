package com.taroflavoured;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.registries.BuiltInRegistries;

public class FavourScreen extends AbstractContainerScreen<FavourMenu> {
    private static final ResourceLocation GUI_TEXTURE = ResourceLocation.fromNamespaceAndPath(
            TaroFlavoured.MOD_ID, "textures/gui/enchanting_table.png");
    private static final ResourceLocation RECIPE_BOOK_BUTTON = ResourceLocation.withDefaultNamespace(
            "recipe_book/button");

    private final FavourRecipeBook recipeBook;

    public FavourScreen(FavourMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
        this.titleLabelX = 8;
        this.inventoryLabelX = 8;
        this.recipeBook = new FavourRecipeBook(Minecraft.getInstance());
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(GUI_TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight, 256, 256);
        guiGraphics.blitSprite(RECIPE_BOOK_BUTTON, leftPos + 14, topPos + 17, 20, 18);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        recipeBook.init(leftPos - 147, topPos);
        renderRecipeGhost(guiGraphics);
        recipeBook.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    private void renderRecipeGhost(GuiGraphics graphics) {
        FavourRecipe recipe = recipeBook.selected();
        if (!recipeBook.isVisible() || recipe == null) return;

        ItemStack input = recipe.favour().is(TaroFlavoured.ENVIOUS_BOOK.get())
                ? new ItemStack(net.minecraft.world.item.Items.BOOK)
                : TaroFlavoured.ENVIOUS_BOOK.get().getDefaultInstance();
        graphics.renderFakeItem(input, leftPos + 15, topPos + 47);

        String[] ingredients = recipe.ingredients();
        for (int i = 0; i < ingredients.length && i < 5; i++) {
            ItemStack stack = displayIngredient(ingredients[i]);
            if (!stack.isEmpty()) graphics.renderFakeItem(stack, leftPos + 66 + i * 20, topPos + 35);
        }
    }

    private ItemStack displayIngredient(String id) {
        if (id.equals("minecraft:any_carpet")) return new ItemStack(net.minecraft.world.item.Items.WHITE_CARPET);
        return BuiltInRegistries.ITEM.getOptional(ResourceLocation.parse(id)).map(ItemStack::new).orElse(ItemStack.EMPTY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        recipeBook.init(leftPos - 147, topPos);
        if (recipeBook.isVisible() && recipeBook.mouseClicked(mouseX, mouseY, button)) return true;

        int buttonLeft = leftPos + 14;
        int buttonTop = topPos + 17;
        if (mouseX >= buttonLeft && mouseX < buttonLeft + 20 && mouseY >= buttonTop && mouseY < buttonTop + 18) {
            recipeBook.toggle();
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (recipeBook.isVisible() && recipeBook.mouseReleased(mouseX, mouseY, button)) return true;
        return super.mouseReleased(mouseX, mouseY, button);
    }
}
