package satisfyu.herbalbrews.client.gui.handler;

import de.cristelknight.doapi.client.recipebook.IRecipeBookGroup;
import de.cristelknight.doapi.client.recipebook.handler.AbstractRecipeBookGUIScreenHandler;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import satisfyu.herbalbrews.client.gui.handler.slot.ExtendedSlot;
import satisfyu.herbalbrews.client.recipebook.group.TeaKettleRecipeBookGroup;
import satisfyu.herbalbrews.entities.TeaKettleBlockEntity;
import satisfyu.herbalbrews.recipe.TeaKettleRecipe;
import satisfyu.herbalbrews.registry.ScreenHandlerTypeRegistry;

import java.util.List;

public class TeaKettleGuiHandler extends AbstractRecipeBookGUIScreenHandler {
    private final ContainerData propertyDelegate;
    private static final int INPUTS = 7;

    public TeaKettleGuiHandler(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, new SimpleContainer(8), new SimpleContainerData(2));
    }

    public TeaKettleGuiHandler(int syncId, Inventory playerInventory, Container inventory, ContainerData propertyDelegate) {
        super(ScreenHandlerTypeRegistry.TEA_KETTLE_SCREEN_HANDLER.get(), syncId, INPUTS, playerInventory, inventory, propertyDelegate);

        buildBlockEntityContainer(inventory);
        buildPlayerContainer(playerInventory);

        this.propertyDelegate = propertyDelegate;
        this.addDataSlots(propertyDelegate);
    }

    private void buildBlockEntityContainer(Container inventory) {
        this.addSlot(new ExtendedSlot(inventory, 0, 124, 28, stack -> false));

        for (int row = 0; row < 2; row++) {
            for (int slot = 0; slot < 3; slot++) {
                this.addSlot(new Slot(inventory, 1 + slot + row + (row * 2), 30 + (slot * 18), 17 + (row * 18)));
            }
        }

        this.addSlot(new ExtendedSlot(inventory, 7, 95, 55, stack -> stack.is(Items.GLASS_BOTTLE)));
    }

    private void buildPlayerContainer(Inventory playerInventory) {
        int i;
        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    public boolean isBeingBurned() {
        return propertyDelegate.get(1) != 0;
    }

    public int getScaledProgress(int arrowWidth) {
        final int progress = this.propertyDelegate.get(0);
        final int totalProgress = TeaKettleBlockEntity.MAX_COOKING_TIME;
        if (progress == 0) {
            return 0;
        }
        return progress * arrowWidth / totalProgress + 1;
    }

    @Override
    public List<IRecipeBookGroup> getGroups() {
        return TeaKettleRecipeBookGroup.POT_GROUPS;
    }

    @Override
    public boolean hasIngredient(Recipe<?> recipe) {
        if (recipe instanceof TeaKettleRecipe teaKettleRecipe) {
            for (Ingredient ingredient : teaKettleRecipe.getIngredients()) {
                boolean found = false;
                for (Slot slot : this.slots) {
                    if (ingredient.test(slot.getItem())) {
                        found = true;
                    }
                }
                if (!found) {
                    return false;
                }
            }
            ItemStack container = teaKettleRecipe.getContainer();
            for (Slot slot : this.slots) {
                if (container.getItem() == slot.getItem().getItem()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int getCraftingSlotCount() {
        return INPUTS;
    }
}
