package com.taroflavoured;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/** Vanilla-textured recipe book for the custom Favour enchanting interface. */
public final class FavourRecipeBook implements Renderable, GuiEventListener, NarratableEntry {
    private static final ResourceLocation BACKGROUND = ResourceLocation.withDefaultNamespace("textures/gui/recipe_book.png");
    private static final ResourceLocation SLOT = ResourceLocation.withDefaultNamespace("recipe_book/slot_uncraftable");
    private static final ResourceLocation SLOT_HOVER = ResourceLocation.withDefaultNamespace("recipe_book/slot_craftable");
    private static final ResourceLocation SIDI_AMAR_BOUSSENA_RECIPE = ResourceLocation.fromNamespaceAndPath(
            TaroFlavoured.MOD_ID, "sidi_amar_boussena");
    private static final int WIDTH = 147;
    private static final int HEIGHT = 166;
    private static final int COLS = 5;
    private static final int ROWS = 4;
    private static final int SLOT_SIZE = 25;

    private final Minecraft minecraft;
    private boolean visible;
    private int left;
    private int top;
    private int page;
    private FavourRecipe selected;
    private final List<FavourRecipe> recipes = new ArrayList<>();

    public FavourRecipeBook(Minecraft minecraft) {
        this.minecraft = minecraft;
        refreshRecipes();
    }

    public void init(int left, int top) { this.left = left; this.top = top; }
    public boolean isVisible() { return visible; }
    public void toggle() { visible = !visible; if (!visible) selected = null; }
    public FavourRecipe selected() { return selected; }

    private void refreshRecipes() {
        recipes.clear();
        recipes.add(FavourRecipe.enviousBookRecipe());
        recipes.addAll(FavourRecipe.allRecipes());

        // Sidi Amar Boussena is the only Favour whose recipe is not available from the start.
        if (!isSidiAmarBoussenaUnlocked()) {
            recipes.removeIf(recipe -> recipe.favour().is(TaroFlavoured.FAVOUR_SIDI_AMAR_BOUSSENA.get()));
        }
    }

    private boolean isSidiAmarBoussenaUnlocked() {
        if (minecraft.player == null || minecraft.level == null) return false;
        return minecraft.level.getRecipeManager().byKey(SIDI_AMAR_BOUSSENA_RECIPE)
                .map(holder -> minecraft.player.getRecipeBook().contains(holder))
                .orElse(false);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        if (!visible) return;
        refreshRecipes();
        graphics.blit(BACKGROUND, left, top, 0, 0, WIDTH, HEIGHT, WIDTH, HEIGHT);
        int start = page * (COLS * ROWS);
        for (int i = 0; i < COLS * ROWS; i++) {
            int index = start + i;
            if (index >= recipes.size()) break;
            int x = left + 8 + (i % COLS) * SLOT_SIZE;
            int y = top + 30 + (i / COLS) * SLOT_SIZE;
            boolean hovered = mouseX >= x && mouseX < x + 25 && mouseY >= y && mouseY < y + 25;
            graphics.blitSprite(hovered ? SLOT_HOVER : SLOT, x, y, 25, 25);
            ItemStack result = recipes.get(index).favour();
            if (result.isEmpty()) result = TaroFlavoured.ENVIOUS_BOOK.get().getDefaultInstance();
            graphics.renderItem(result, x + 4, y + 4);
        }
        if (page > 0) graphics.blitSprite(ResourceLocation.withDefaultNamespace("recipe_book/page_backward"), left + 38, top + 144, 12, 17);
        if ((page + 1) * COLS * ROWS < recipes.size()) graphics.blitSprite(ResourceLocation.withDefaultNamespace("recipe_book/page_forward"), left + 98, top + 144, 12, 17);

        int hoveredIndex = hoveredIndex(mouseX, mouseY);
        if (hoveredIndex >= 0) {
            ItemStack result = recipes.get(hoveredIndex).favour();
            if (result.isEmpty()) result = TaroFlavoured.ENVIOUS_BOOK.get().getDefaultInstance();
            graphics.renderTooltip(minecraft.font, result, mouseX, mouseY);
        }
    }

    private int hoveredIndex(double mouseX, double mouseY) {
        if (!visible || mouseX < left + 8 || mouseY < top + 30) return -1;
        int column = (int) ((mouseX - left - 8) / SLOT_SIZE);
        int row = (int) ((mouseY - top - 30) / SLOT_SIZE);
        if (column < 0 || column >= COLS || row < 0 || row >= ROWS) return -1;
        int index = page * (COLS * ROWS) + row * COLS + column;
        return index < recipes.size() ? index : -1;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!visible || button != 0) return false;
        refreshRecipes();
        if (mouseX >= left + 38 && mouseX < left + 55 && mouseY >= top + 142 && mouseY < top + 164 && page > 0) { page--; selected = null; return true; }
        if (mouseX >= left + 98 && mouseX < left + 115 && mouseY >= top + 142 && mouseY < top + 164 && (page + 1) * COLS * ROWS < recipes.size()) { page++; selected = null; return true; }
        int index = hoveredIndex(mouseX, mouseY);
        if (index >= 0) { selected = recipes.get(index); return true; }
        return false;
    }

    @Override public boolean isMouseOver(double mouseX, double mouseY) { return visible && mouseX >= left && mouseX < left + WIDTH && mouseY >= top && mouseY < top + HEIGHT; }
    @Override public boolean mouseReleased(double mouseX, double mouseY, int button) { return false; }
    @Override public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) { return false; }
    @Override public boolean mouseScrolled(double mouseX, double mouseY, double delta) { return false; }
    @Override public boolean keyPressed(int keyCode, int scanCode, int modifiers) { return false; }
    @Override public boolean keyReleased(int keyCode, int scanCode, int modifiers) { return false; }
    @Override public boolean charTyped(char codePoint, int modifiers) { return false; }
    @Override public void setFocused(boolean focused) {}
    @Override public boolean isFocused() { return visible; }
    @Override public boolean isActive() { return visible; }
    @Override public NarratableEntry.NarrationPriority narrationPriority() { return visible ? NarratableEntry.NarrationPriority.HOVERED : NarratableEntry.NarrationPriority.NONE; }
    @Override public void updateNarration(net.minecraft.client.gui.narration.NarrationElementOutput output) {}
}
