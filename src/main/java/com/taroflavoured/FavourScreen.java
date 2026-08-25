package com.taroflavoured;

import net.minecraft.client.ClientRecipeBook;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.client.RecipeBookManager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FavourScreen extends AbstractContainerScreen<FavourMenu> implements RecipeUpdateListener {
    private static final int RECIPE_BOOK_WIDTH_THRESHOLD = 379;
    private static final ResourceLocation GUI_TEXTURE = ResourceLocation.fromNamespaceAndPath(
            TaroFlavoured.MOD_ID, "textures/gui/enchanting_table.png");

    private final RecipeBookComponent recipeBookComponent = new RecipeBookComponent();
    private ImageButton recipeBookButton;
    private boolean widthTooNarrow;

    private Map<RecipeBookCategories, List<RecipeCollection>> savedCollectionsByTab;
    private List<RecipeCollection> savedAllCollections;
    private ClientRecipeBook savedRecipeBook;

    public FavourScreen(FavourMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
        this.titleLabelX = 8;
        this.inventoryLabelX = 8;
    }

    @Override
    protected void init() {
        super.init();

        this.widthTooNarrow = this.width < RECIPE_BOOK_WIDTH_THRESHOLD;
        setupRecipeBookCollections();
        this.recipeBookComponent.init(this.width, this.height, this.minecraft, this.widthTooNarrow, this.menu);
        this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);
        this.topPos = (this.height - this.imageHeight) / 2;

        diagnoseRecipeBook();

        this.recipeBookButton = new ImageButton(
                this.leftPos + 14,
                this.topPos + 17,
                20,
                18,
                RecipeBookComponent.RECIPE_BUTTON_SPRITES,
                button -> {
                    this.recipeBookComponent.initVisuals();
                    this.recipeBookComponent.toggleVisibility();
                    this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);
                    this.topPos = (this.height - this.imageHeight) / 2;
                    positionRecipeBookButton();
                }
        );
        this.addRenderableWidget(this.recipeBookButton);
        positionRecipeBookButton();
    }

    private void setupRecipeBookCollections() {
        if (this.minecraft == null || this.minecraft.player == null || this.minecraft.level == null) return;

        ClientRecipeBook book = this.minecraft.player.getRecipeBook();

        try {
            Field collectionsByTabField = ClientRecipeBook.class.getDeclaredField("collectionsByTab");
            collectionsByTabField.setAccessible(true);
            @SuppressWarnings("unchecked")
            Map<RecipeBookCategories, List<RecipeCollection>> currentCollectionsByTab =
                    (Map<RecipeBookCategories, List<RecipeCollection>>) collectionsByTabField.get(book);

            Field allCollectionsField = ClientRecipeBook.class.getDeclaredField("allCollections");
            allCollectionsField.setAccessible(true);
            @SuppressWarnings("unchecked")
            List<RecipeCollection> currentAllCollections =
                    (List<RecipeCollection>) allCollectionsField.get(book);

            if (this.savedRecipeBook == null) {
                this.savedRecipeBook = book;
                this.savedCollectionsByTab = new HashMap<>(currentCollectionsByTab);
                this.savedAllCollections = new ArrayList<>(currentAllCollections);
            }

            List<RecipeHolder<FavourRecipe>> recipes = this.minecraft.level.getRecipeManager()
                    .getAllRecipesFor(TaroFlavoured.FAVOUR_RECIPE_TYPE.get());
            if (recipes.isEmpty()) return;

            book.setupCollections(
                    this.minecraft.level.getRecipeManager().getRecipes(),
                    this.minecraft.level.registryAccess()
            );

            RecipeBookCategories category = RecipeBookCategories.valueOf("TAROFLAVOURED_FAVOURS");
            List<RecipeCollection> favourCollections = new ArrayList<>();
            for (RecipeHolder<FavourRecipe> recipe : recipes) {
                RecipeCollection collection = new RecipeCollection(
                        this.minecraft.level.registryAccess(),
                        List.of(recipe)
                );
                collection.updateKnownRecipes(book);
                favourCollections.add(collection);
            }

            @SuppressWarnings("unchecked")
            Map<RecipeBookCategories, List<RecipeCollection>> rebuiltCollectionsByTab =
                    (Map<RecipeBookCategories, List<RecipeCollection>>) collectionsByTabField.get(book);
            Map<RecipeBookCategories, List<RecipeCollection>> updated = new HashMap<>(rebuiltCollectionsByTab);
            updated.put(category, List.copyOf(favourCollections));
            collectionsByTabField.set(book, Map.copyOf(updated));

            @SuppressWarnings("unchecked")
            List<RecipeCollection> rebuiltAllCollections =
                    (List<RecipeCollection>) allCollectionsField.get(book);
            List<RecipeCollection> updatedAll = new ArrayList<>(rebuiltAllCollections);
            updatedAll.removeIf(existing -> existing.getRecipes().stream().anyMatch(recipe ->
                    recipe.value().getType() == TaroFlavoured.FAVOUR_RECIPE_TYPE.get()));
            updatedAll.addAll(favourCollections);
            allCollectionsField.set(book, List.copyOf(updatedAll));
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException("Unable to install Taro Favour recipe-book collections", exception);
        }
    }

    private void restoreRecipeBookCollections() {
        if (this.savedRecipeBook == null) return;

        try {
            Field collectionsByTabField = ClientRecipeBook.class.getDeclaredField("collectionsByTab");
            collectionsByTabField.setAccessible(true);
            collectionsByTabField.set(this.savedRecipeBook, Map.copyOf(this.savedCollectionsByTab));

            Field allCollectionsField = ClientRecipeBook.class.getDeclaredField("allCollections");
            allCollectionsField.setAccessible(true);
            allCollectionsField.set(this.savedRecipeBook, List.copyOf(this.savedAllCollections));
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException("Unable to restore recipe-book collections", exception);
        } finally {
            this.savedRecipeBook = null;
            this.savedCollectionsByTab = null;
            this.savedAllCollections = null;
        }
    }

    private void diagnoseRecipeBook() {
        if (this.minecraft == null || this.minecraft.player == null || this.minecraft.level == null) return;

        RecipeBookCategories category = RecipeBookCategories.valueOf("TAROFLAVOURED_FAVOURS");
        ClientRecipeBook book = this.minecraft.player.getRecipeBook();
        List<RecipeHolder<FavourRecipe>> recipes = this.minecraft.level.getRecipeManager()
                .getAllRecipesFor(TaroFlavoured.FAVOUR_RECIPE_TYPE.get());

        System.out.println("=== Taro Favour recipe-book diagnostic ===");
        System.out.println("recipeBookType=" + TaroFlavoured.FAVOUR_RECIPE_BOOK);
        System.out.println("category=" + category);
        System.out.println("registeredCategories=" + RecipeBookManager.getCustomCategoriesOrEmpty(TaroFlavoured.FAVOUR_RECIPE_BOOK));
        System.out.println("clientFavourRecipes=" + recipes.size());
        System.out.println("clientFavourCollections=" + book.getCollection(category).size());
        if (!recipes.isEmpty()) {
            RecipeHolder<FavourRecipe> recipe = recipes.get(0);
            System.out.println("firstRecipe=" + recipe.id());
            System.out.println("firstRecipeCategory=" + RecipeBookManager.findCategories(TaroFlavoured.FAVOUR_RECIPE_TYPE.get(), recipe));
            System.out.println("firstRecipeKnown=" + book.contains(recipe));
        }
        System.out.println("=== End Taro diagnostic ===");
    }

    private void positionRecipeBookButton() {
        if (this.recipeBookButton != null) {
            this.recipeBookButton.setX(this.leftPos + 14);
            this.recipeBookButton.setY(this.topPos + 17);
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(GUI_TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight, 256, 256);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        // The supplied texture has its own framing and intentionally has no title/tier text.
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (this.recipeBookComponent.isVisible() && this.widthTooNarrow) {
            this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
            this.renderBg(guiGraphics, partialTick, mouseX, mouseY);
            this.recipeBookComponent.render(guiGraphics, mouseX, mouseY, partialTick);
        } else {
            super.render(guiGraphics, mouseX, mouseY, partialTick);
            this.recipeBookComponent.render(guiGraphics, mouseX, mouseY, partialTick);
        }

        // Vanilla recipe-book screens render the ghost recipe separately from the book.
        // This is also what provides the missing-ingredient tooltip when a recipe is
        // selected but the player does not have every ingredient.
        this.recipeBookComponent.renderGhostRecipe(
                guiGraphics, this.leftPos, this.topPos, this.widthTooNarrow, partialTick
        );
        this.recipeBookComponent.renderTooltip(guiGraphics, this.leftPos, this.topPos, mouseX, mouseY);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.recipeBookComponent.mouseClicked(mouseX, mouseY, button)) return true;
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected boolean hasClickedOutside(double mouseX, double mouseY, int guiLeft, int guiTop, int mouseButton) {
        if (this.recipeBookComponent.isVisible()) {
            return this.recipeBookComponent.hasClickedOutside(
                    mouseX, mouseY, guiLeft, guiTop, this.imageWidth, this.imageHeight, mouseButton
            );
        }
        return super.hasClickedOutside(mouseX, mouseY, guiLeft, guiTop, mouseButton);
    }

    @Override
    protected void slotClicked(Slot slot, int slotId, int mouseButton, ClickType type) {
        this.recipeBookComponent.slotClicked(slot);
        super.slotClicked(slot, slotId, mouseButton, type);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.recipeBookComponent.keyPressed(keyCode, scanCode, modifiers)) return true;
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (this.recipeBookComponent.charTyped(codePoint, modifiers)) return true;
        return super.charTyped(codePoint, modifiers);
    }

    @Override
    public void containerTick() {
        super.containerTick();
        this.recipeBookComponent.tick();
        positionRecipeBookButton();
    }

    @Override
    public void recipesUpdated() {
        setupRecipeBookCollections();
        this.recipeBookComponent.recipesUpdated();
    }

    @Override
    public void removed() {
        restoreRecipeBookCollections();
        super.removed();
    }

    public RecipeBookComponent getRecipeBookComponent() {
        return recipeBookComponent;
    }
}
