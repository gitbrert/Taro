package com.taroflavoured;

import net.minecraft.core.NonNullList;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

public class FavourMenu extends RecipeBookMenu<CraftingInput, FavourRecipe> {
    public static final int INPUT_SLOT = 0;
    public static final int INGREDIENT_START = 1;
    public static final int OUTPUT_SLOT = 6;
    public static final int CUSTOM_SLOT_COUNT = 7;
    private static final int RECIPE_GRID_SIZE = 6;

    private final Container container;
    private final ContainerLevelAccess access;
    private final ContainerData data;
    private final BlockPos blockPos;
    private final Inventory playerInventory;

    public FavourMenu(int containerId, Inventory playerInventory, RegistryFriendlyByteBuf extraData) {
        this(containerId, playerInventory, new SimpleContainer(CUSTOM_SLOT_COUNT), ContainerLevelAccess.NULL,
                extraData.readBlockPos(), new net.minecraft.world.inventory.SimpleContainerData(1));
    }

    public FavourMenu(int containerId, Inventory playerInventory, Level level, BlockPos blockPos) {
        this(containerId, playerInventory, new SimpleContainer(CUSTOM_SLOT_COUNT), ContainerLevelAccess.create(level, blockPos),
                blockPos, new net.minecraft.world.inventory.SimpleContainerData(1));
        this.data.set(0, calculateTier(level, blockPos));
    }

    private FavourMenu(int containerId, Inventory playerInventory, Container container, ContainerLevelAccess access,
                       BlockPos blockPos, ContainerData data) {
        super(TaroFlavoured.FAVOUR_MENU.get(), containerId);
        this.container = container;
        this.access = access;
        this.blockPos = blockPos;
        this.data = data;
        this.playerInventory = playerInventory;

        checkContainerSize(container, CUSTOM_SLOT_COUNT);
        container.startOpen(playerInventory.player);

        addSlot(new BookInputSlot(container, INPUT_SLOT, 15, 47));
        for (int i = 0; i < 5; i++) addSlot(new IngredientSlot(container, INGREDIENT_START + i, 66 + i * 20, 35));
        addSlot(new OutputSlot(this, container, OUTPUT_SLOT, 35, 47));

        addPlayerInventory(playerInventory);
        addDataSlots(data);
    }

    private void addPlayerInventory(Inventory inventory) {
        for (int row = 0; row < 3; row++) for (int column = 0; column < 9; column++) addSlot(new Slot(inventory, column + row * 9 + 9, 8 + column * 18, 84 + row * 18));
        for (int column = 0; column < 9; column++) addSlot(new Slot(inventory, column, 8 + column * 18, 142));
    }

    public int getTier() { return data.get(0); }
    public int getActiveIngredientCount() { return switch (getTier()) { case 0 -> 2; case 1 -> 3; case 2 -> 4; default -> 5; }; }
    public BlockPos getBlockPos() { return blockPos; }

    private CraftingInput craftingInput() {
        NonNullList<ItemStack> input = NonNullList.withSize(RECIPE_GRID_SIZE, ItemStack.EMPTY);
        for (int i = 0; i < RECIPE_GRID_SIZE; i++) input.set(i, container.getItem(i).copy());
        return CraftingInput.of(RECIPE_GRID_SIZE, 1, input);
    }

    private void updateResult() {
        RecipeHolder<FavourRecipe> holder = null;
        Level level = accessLevel();
        if (level != null) holder = level.getRecipeManager().getRecipeFor(TaroFlavoured.FAVOUR_RECIPE_TYPE.get(), craftingInput(), level).orElse(null);

        ItemStack result = ItemStack.EMPTY;
        if (holder != null && getTier() >= holder.value().tier()) {
            result = holder.value().assemble(craftingInput(), level.registryAccess());
            if (result.is(TaroFlavoured.ENVIOUS_BOOK.get())) result = TaroFlavoured.createEnviousBook(level.registryAccess());
            else if (TaroFlavoured.isEnviousBook(container.getItem(INPUT_SLOT))) result = FavourEnchantments.apply(result, level.registryAccess());
        }
        container.setItem(OUTPUT_SLOT, result);
    }

    private Level accessLevel() {
        final Level[] result = new Level[1];
        access.execute((level, pos) -> result[0] = level);
        return result[0];
    }

    private void consumeRecipeInputs() {
        for (int i = 0; i < RECIPE_GRID_SIZE; i++) container.removeItem(i, 1);
        container.setItem(OUTPUT_SLOT, ItemStack.EMPTY);
    }

    private boolean takeResult(Player player) {
        updateResult();
        ItemStack result = container.getItem(OUTPUT_SLOT);
        if (result.isEmpty()) return false;
        consumeRecipeInputs();
        player.containerMenu.broadcastChanges();
        return true;
    }

    @Override public boolean recipeMatches(RecipeHolder<FavourRecipe> recipe) { return getTier() >= recipe.value().tier() && recipe.value().matches(craftingInput(), accessLevel()); }
    @Override public void fillCraftSlotsStackedContents(StackedContents itemHelper) { for (int i = 0; i < RECIPE_GRID_SIZE; i++) itemHelper.accountStack(container.getItem(i)); }

    @Override
    public void clearCraftingContent() {
        for (int i = 0; i < RECIPE_GRID_SIZE; i++) {
            ItemStack stack = container.removeItemNoUpdate(i);
            if (!stack.isEmpty()) playerInventory.player.getInventory().placeItemBackInInventory(stack);
        }
        container.setItem(OUTPUT_SLOT, ItemStack.EMPTY);
    }

    @Override protected void beginPlacingRecipe() { }
    @Override protected void finishPlacingRecipe(RecipeHolder<FavourRecipe> recipe) { updateResult(); }
    @Override public int getResultSlotIndex() { return OUTPUT_SLOT; }
    @Override public int getGridWidth() { return RECIPE_GRID_SIZE; }
    @Override public int getGridHeight() { return 1; }
    @Override public int getSize() { return RECIPE_GRID_SIZE; }
    @Override public RecipeBookType getRecipeBookType() { return RecipeBookType.CRAFTING; }
    @Override public List<RecipeBookCategories> getRecipeBookCategories() { return List.of(RecipeBookCategories.CRAFTING_MISC); }
    @Override public boolean shouldMoveToInventory(int slotIndex) { return slotIndex == OUTPUT_SLOT; }

    private static int calculateTier(Level level, BlockPos tablePos) {
        int shelves = 0;
        for (int dx = -2; dx <= 2; dx++) for (int dz = -2; dz <= 2; dz++) {
            if (Math.max(Math.abs(dx), Math.abs(dz)) != 2) continue;
            for (int dy = 0; dy <= 1; dy++) {
                BlockPos shelfPos = tablePos.offset(dx, dy, dz);
                if (level.getBlockState(shelfPos).is(Blocks.BOOKSHELF) && isBookshelfPathClear(level, tablePos, dx, dy, dz)) shelves++;
            }
        }
        return Math.min(3, shelves / 5);
    }

    private static boolean isBookshelfPathClear(Level level, BlockPos tablePos, int dx, int dy, int dz) {
        int stepX = Integer.signum(dx), stepZ = Integer.signum(dz);
        BlockPos firstGap = tablePos.offset(stepX, dy, stepZ);
        if (!level.getBlockState(firstGap).isAir()) return false;
        if (dx != 0 && dz != 0) return level.getBlockState(tablePos.offset(stepX, dy, 0)).isAir() && level.getBlockState(tablePos.offset(0, dy, stepZ)).isAir();
        return true;
    }

    @Override public boolean stillValid(Player player) { return AbstractContainerMenu.stillValid(access, player, Blocks.ENCHANTING_TABLE); }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        Slot slot = slots.get(slotIndex);
        if (slot == null || !slot.hasItem()) return ItemStack.EMPTY;
        if (slotIndex == OUTPUT_SLOT) {
            ItemStack result = slot.getItem().copy();
            if (result.isEmpty() || !moveItemStackTo(result, CUSTOM_SLOT_COUNT, slots.size(), true)) return ItemStack.EMPTY;
            consumeRecipeInputs();
            return result;
        }
        ItemStack source = slot.getItem().copy();
        ItemStack stack = slot.getItem();
        if (slotIndex < CUSTOM_SLOT_COUNT) {
            if (!moveItemStackTo(stack, CUSTOM_SLOT_COUNT, slots.size(), true)) return ItemStack.EMPTY;
        } else if (!moveItemStackTo(stack, 0, CUSTOM_SLOT_COUNT, false)) return ItemStack.EMPTY;
        if (stack.isEmpty()) slot.set(ItemStack.EMPTY); else slot.setChanged();
        return source;
    }

    @Override public void removed(Player player) { super.removed(player); container.stopOpen(player); if (!player.level().isClientSide) clearContainer(player, container); }
    @Override public void broadcastChanges() { access.execute((level, pos) -> data.set(0, calculateTier(level, pos))); updateResult(); super.broadcastChanges(); }

    private static class BookInputSlot extends Slot {
        BookInputSlot(Container container, int slot, int x, int y) { super(container, slot, x, y); }
        @Override public boolean mayPlace(ItemStack stack) { return stack.is(Items.BOOK) || TaroFlavoured.isEnviousBook(stack); }
    }
    private static class IngredientSlot extends Slot {
        IngredientSlot(Container container, int slot, int x, int y) { super(container, slot, x, y); }
        @Override public boolean mayPlace(ItemStack stack) { return true; }
    }
    private static class OutputSlot extends Slot {
        private final FavourMenu menu;
        OutputSlot(FavourMenu menu, Container container, int slot, int x, int y) { super(container, slot, x, y); this.menu = menu; }
        @Override public boolean mayPlace(ItemStack stack) { return false; }
        @Override public void onTake(Player player, ItemStack stack) { menu.takeResult(player); super.onTake(player, stack); }
    }
}
