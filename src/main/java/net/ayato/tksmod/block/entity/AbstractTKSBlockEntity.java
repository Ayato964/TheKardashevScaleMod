package net.ayato.tksmod.block.entity;

import net.ayato.tksmod.recipe.AbstractTKSRecipe;
import net.ayato.tksmod.util.entity.ITKSBlockEntityAddon;
import net.ayato.tksmod.util.entity.TKSEnergyEntityAddon;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Optional;

public abstract class AbstractTKSBlockEntity extends BlockEntity implements MenuProvider {

    private final ArrayList<ITKSBlockEntityAddon> addons = setAddons(new ArrayList<>());
    public final ItemStackHandler itemStackHandler = new ItemStackHandler(getStackBoxCount()){
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    protected LazyOptional<ItemStackHandler> lazyItemHandler = LazyOptional.empty();
    protected final ContainerData data;
    /**
     * Progress is dynamic.
     */
    public int progress = 0;
    //private int maxProgress = 78;
    public int maxProgress = getMaxProgress();



    public AbstractTKSBlockEntity(BlockEntityType<?> type,BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index){
                    case 0->AbstractTKSBlockEntity.this.progress;
                    case 1 -> AbstractTKSBlockEntity.this.maxProgress;
                    default -> getAdditionalProgressData(index);
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index){
                    case 0->AbstractTKSBlockEntity.this.progress = value;
                    case 1->AbstractTKSBlockEntity.this.maxProgress = value;
                    default -> setAdditionalProgressData(index, value);
                }

            }

            @Override
            public int getCount() {
                return Math.max(getContainerDataCount(), 2);
            }


        };
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("blockentity.tksmod." + getName());
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return getCreateMenu(id, inventory, player);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ITEM_HANDLER){
            return lazyItemHandler.cast();
        }
        for(ITKSBlockEntityAddon addon : addons){
            LazyOptional<T> l = addon.getCapability(cap, side);
            if(l != null)
                return l;
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemStackHandler);

        for(ITKSBlockEntityAddon addon : addons) addon.onLoad();
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
        for(ITKSBlockEntityAddon addon : addons) addon.invalidateCaps();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.put("inventory", itemStackHandler.serializeNBT());
        nbt.putInt(getName() + ".progress", this.progress);

        for(ITKSBlockEntityAddon addon : addons) addon.saveAdditional(nbt);

        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        itemStackHandler.deserializeNBT(nbt.getCompound("inventory"));
        progress = nbt.getInt(getName() + ".progress");

        for(ITKSBlockEntityAddon addon : addons) addon.load(nbt);
        super.load(nbt);
    }
    public void drops(){
       SimpleContainer inventory = convertItemHandlerToContainer();
        Containers.dropContents(level, worldPosition, inventory);
    }
    public static <E extends BlockEntity> void tick(Level level, BlockPos blockPos, BlockState state, E e) {
        if(level.isClientSide()){
            return;
        }
        if(e instanceof AbstractTKSBlockEntity) {
            AbstractTKSBlockEntity entity = ((AbstractTKSBlockEntity) e);
            entity.runningAlways(level, blockPos, state);

            for(ITKSBlockEntityAddon addon : entity.addons) addon.runningAlways(level, blockPos, state);

            if (hasRecipe(e) && entity.getCondition(level, blockPos, state) && entity.getAddonsConditions(level, blockPos, state)) {
                entity.progress++;
                for(ITKSBlockEntityAddon addon : entity.addons) addon.runningHaveRecipe(level, blockPos, state);

                setChanged(level, blockPos, state);

                if (entity.progress >= entity.maxProgress) {
                    craftItem(entity);
                } else {
                    setChanged(level, blockPos, state);
                }

            }
        }
    }

    protected boolean getAddonsConditions(Level level, BlockPos blockPos, BlockState state){
        for(ITKSBlockEntityAddon addon : addons){
            if(!addon.getCondition(level, blockPos, state, this)){
                return false;
            }
        }
        return true;
    }


    private void resetProgress() {
        this.progress = 0;
    }


    protected static void craftItem(AbstractTKSBlockEntity entity) {
        Level level = entity.level;
        SimpleContainer inventory = entity.convertItemHandlerToContainer();
        Optional<? extends AbstractTKSRecipe> recipe  = entity.getRecipe(inventory, level);


        if(hasRecipe(entity)){
            entity.itemStackHandler.extractItem(0, 1, false);
            entity.itemStackHandler.setStackInSlot(1, new ItemStack(recipe.get().getResultItem().getItem(),
                    entity.itemStackHandler.getStackInSlot(1).getCount() + 1));
            entity.resetProgress();
        }
    }

    protected static <E extends BlockEntity> boolean hasRecipe(E e) {
        if(e instanceof AbstractTKSBlockEntity){
            AbstractTKSBlockEntity entity = (AbstractTKSBlockEntity) e;
            Level level = entity.level;
            SimpleContainer inventory = entity.convertItemHandlerToContainer();

            /**
             * This Code is not Objects
             */
            //Optional<Debug_BlockRecipe> recipe = level.getRecipeManager().getRecipeFor(Debug_BlockRecipe.Type.INSTANCE, inventory, level);
            Optional<? extends AbstractTKSRecipe> recipe = entity.getRecipe(inventory, level);

            return recipe.isPresent() && canInsertAmountIntoOutputSlot(inventory) &&
                    canInsertItemIntoOutputSlot(inventory, recipe.get().getResultItem());

        }
        return false;
    }
    private static boolean canInsertItemIntoOutputSlot(SimpleContainer inventory, ItemStack itemStack) {
        return inventory.getItem(1).getItem() == itemStack.getItem() || inventory.getItem(1).isEmpty();
    }
    private static boolean canInsertAmountIntoOutputSlot(SimpleContainer inventory) {
        return  inventory.getItem(1).getMaxStackSize() > inventory.getItem(1).getCount();
    }

    protected SimpleContainer convertItemHandlerToContainer(){
        SimpleContainer inventory = new SimpleContainer(itemStackHandler.getSlots());
        for(int i = 0; i < itemStackHandler.getSlots(); i ++){
            inventory.setItem(i, itemStackHandler.getStackInSlot(i));
        }
        return inventory;
    }

    protected abstract String getName();
    protected abstract int getStackBoxCount();
    protected abstract AbstractContainerMenu getCreateMenu(int id, Inventory inventory, Player player);
    protected abstract int getMaxProgress();
    protected int getAdditionalProgressData(int index){return 0;}
    protected void setAdditionalProgressData(int index, int value){}
    protected abstract int getContainerDataCount();
    protected abstract Optional<? extends AbstractTKSRecipe> getRecipe(SimpleContainer inventory, Level level);

    protected abstract ArrayList<ITKSBlockEntityAddon> setAddons(ArrayList<ITKSBlockEntityAddon> ad);

    /**
     * Methods to write when an entity is unique and does something special.
     * @param level
     * @param pos
     * @param state
     */
    protected abstract void runningAlways(Level level, BlockPos pos, BlockState state);

    /**
     * Methods to write when the entity is unique and does something special when the recipe is established
     * @param level
     * @param pos
     * @param state
     */
    protected abstract void runningHaveRecipe(Level level, BlockPos pos, BlockState state);


    protected abstract boolean getCondition(Level level, BlockPos pos, BlockState state);

    public ITKSBlockEntityAddon getAddonInstance(Class<TKSEnergyEntityAddon> tksEnergyEntityAddonClass) {
        for(ITKSBlockEntityAddon a : addons){
            if(a.getClass() == tksEnergyEntityAddonClass)
                return a;
        }
        return null;
    }
}
