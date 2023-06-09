package net.ayato.tksmod.util.entity;

import net.ayato.tksmod.block.entity.AbstractTKSBlockEntity;
import net.ayato.tksmod.recipe.AbstractTKSRecipe;
import net.ayato.tksmod.recipe.EnergyTestBlockRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Supplier;

public abstract class TKSItemSlotEntityAddon implements ITKSBlockEntityAddon{
    private final AbstractTKSBlockEntity PERCENT;
    private final String blockID;
    public final ItemStackHandler itemStackHandler;

    protected LazyOptional<ItemStackHandler> lazyItemHandler = LazyOptional.empty();
    private final int SLOT_COUNT;
    private final int[] inputSlotNumbers, outputSlotsNumbers;
    private RunningMainProgressMaxed runningMainProgressMaxed;
    private RunningHasRecipe runningHasRecipe;
    public TKSItemSlotEntityAddon(AbstractTKSBlockEntity percent, String blockId, int slotCount, int[] inputSlotNumbers, int[] outputSlotNumbers,  ItemStackHandler handler){
        this.inputSlotNumbers = inputSlotNumbers;
        this.outputSlotsNumbers = outputSlotNumbers;
        SLOT_COUNT = slotCount;
        blockID = blockId;
        PERCENT = percent;
        itemStackHandler = handler;
    }
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ITEM_HANDLER){
            return lazyItemHandler.cast();
        }
        return null;
    }

    @Override
    public void onLoad() {
        lazyItemHandler = LazyOptional.of(() -> itemStackHandler);

    }

    @Override
    public void invalidateCaps() {
        lazyItemHandler.invalidate();

    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        nbt.put("inventory", itemStackHandler.serializeNBT());

    }

    @Override
    public void load(CompoundTag nbt) {
        itemStackHandler.deserializeNBT(nbt.getCompound("inventory"));

    }

    @Override
    public void runningAlways(Level level, BlockPos pos, BlockState state) {}

    @Override
    public void runningHaveRecipe(Level level, BlockPos pos, BlockState state) {
        SimpleContainer inventory = convertItemHandlerToContainer();
        Optional<? extends AbstractTKSRecipe> recipe = getRecipe(inventory, level);
        if(runningHasRecipe != null)
            runningHasRecipe.run(this, inventory, recipe);
    }

    @Override
    public boolean getCondition(Level level, BlockPos pos, BlockState state, AbstractTKSBlockEntity entity) { //hasRecipe();
        SimpleContainer inventory = convertItemHandlerToContainer();

        /**
         * This Code is not Objects
         */
        //Optional<Debug_BlockRecipe> recipe = level.getRecipeManager().getRecipeFor(Debug_BlockRecipe.Type.INSTANCE, inventory, level);
        Optional<? extends AbstractTKSRecipe> recipe = getRecipe(inventory, level);

        return recipe.isPresent() && canInsertAmountIntoOutputSlot(inventory) &&
                canInsertItemIntoOutputSlot(inventory, recipe.get().getResultItem());
    }

    @Override
    public void runningMainProgressMaxed(Level level, BlockPos blockPos, BlockState state, AbstractTKSBlockEntity entity) {
        SimpleContainer inventory = convertItemHandlerToContainer();
        Optional<? extends AbstractTKSRecipe> recipe  = getRecipe(inventory, level);
        if(runningMainProgressMaxed == null) {
            if (getCondition(level, blockPos, state, entity)) {
                removeInputItemAll();
                setOutputItemAll(recipe);
            }
        }else {
            runningMainProgressMaxed.runningMainProgressMaxed(this, inventory, recipe);
        }
    }
    public TKSItemSlotEntityAddon editRunningMainProgressMaxed(RunningMainProgressMaxed method){
        runningMainProgressMaxed = method;
        return this;
    }
    public TKSItemSlotEntityAddon editRunningHasRecipe(RunningHasRecipe r){
        runningHasRecipe = r;
        return this;
    }
    public void removeInputItemAll(){
        for(int i : inputSlotNumbers) {
            removeInputItem(i, 1);
        }
    }
    public void removeInputItem(int id, int num){

        itemStackHandler.extractItem(id, num, false);
    }
    public void setOutputItemAll(Optional<? extends AbstractTKSRecipe> recipe){
        for(int i : outputSlotsNumbers) {
            setOutputItem(i,  recipe, itemStackHandler.getStackInSlot(i).getCount() + 1);
        }
    }
    public void setOutputItem(int id, Optional<? extends AbstractTKSRecipe> recipe, int count){
        itemStackHandler.setStackInSlot(id, new ItemStack(recipe.get().getResultItem().getItem(),
                count));
    }

    @Override
    public void drops(Level level, BlockPos worldPosition) {
        SimpleContainer inventory = convertItemHandlerToContainer();
        Containers.dropContents(level, worldPosition, inventory);
    }

    protected SimpleContainer convertItemHandlerToContainer(){
        SimpleContainer inventory = new SimpleContainer(itemStackHandler.getSlots());
        for(int i = 0; i < itemStackHandler.getSlots(); i ++){
            inventory.setItem(i, itemStackHandler.getStackInSlot(i));
        }
        return inventory;
    }

    @Override
    public Type getType() {
        return Type.ITEM;
    }

    private  boolean canInsertItemIntoOutputSlot(SimpleContainer inventory, ItemStack itemStack) {
        for(int i : outputSlotsNumbers)
            if(!(inventory.getItem(i).getItem() == itemStack.getItem() || inventory.getItem(i).isEmpty()))
                return false;
        return true;
    }
    private  boolean canInsertAmountIntoOutputSlot(SimpleContainer inventory) {
        for(int i : outputSlotsNumbers)
            if(!( inventory.getItem(i).getMaxStackSize() > inventory.getItem(i).getCount()))
                return false;
        return true;
    }

    public abstract  Optional<? extends AbstractTKSRecipe> getRecipe(SimpleContainer inventory, Level level);
    public interface RunningMainProgressMaxed{
        public abstract void runningMainProgressMaxed(TKSItemSlotEntityAddon addon, SimpleContainer inventory, Optional<? extends AbstractTKSRecipe> recipe);
    }
    public interface RunningHasRecipe{
        public abstract void run(TKSItemSlotEntityAddon addon, SimpleContainer inventory, Optional<? extends AbstractTKSRecipe> recipe);

    }
}
