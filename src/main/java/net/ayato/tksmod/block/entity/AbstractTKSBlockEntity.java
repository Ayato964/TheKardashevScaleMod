package net.ayato.tksmod.block.entity;

import net.ayato.tksmod.item.TKSItems;
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
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.checkerframework.checker.units.qual.A;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractTKSBlockEntity extends BlockEntity implements MenuProvider {
    protected final ItemStackHandler itemStackHandler = new ItemStackHandler(getStackBoxCount()){
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
    private int progress = 0;
    private int maxProgress = 78;


    public AbstractTKSBlockEntity(BlockEntityType<?> type,BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index){
                    case 0->AbstractTKSBlockEntity.this.progress;
                    case 1 -> AbstractTKSBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index){
                    case 0->AbstractTKSBlockEntity.this.progress = value;
                    case 1->AbstractTKSBlockEntity.this.maxProgress = value;
                }

            }

            @Override
            public int getCount() {
                return 2;
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
        if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemStackHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.put("inventory", itemStackHandler.serializeNBT());
        nbt.putInt(getName() + ".progress", this.progress);
        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        itemStackHandler.deserializeNBT(nbt.getCompound("inventory"));
        progress = nbt.getInt(getName() + ".progress");
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
        if(hasRecipe(e)){
            if(e instanceof AbstractTKSBlockEntity){
                AbstractTKSBlockEntity entity = ((AbstractTKSBlockEntity) e);
                entity.progress ++;
                System.out.println(entity.progress + "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                setChanged(level, blockPos, state);

                if(entity.progress >= entity.maxProgress){
                    craftItem(entity);
                }else {
                    setChanged(level, blockPos, state);
                }
            }
        }
    }

    private void resetProgress() {
        this.progress = 0;
    }

    /**
     * @deprecated
     * @param entity
     */
    private static void craftItem(AbstractTKSBlockEntity entity) {
        if(hasRecipe(entity)){
            entity.itemStackHandler.extractItem(1, 1, false);
            entity.itemStackHandler.setStackInSlot(2, new ItemStack(TKSItems.STEEL_INGOT.get(), entity.itemStackHandler.getStackInSlot(2).getCount() + 1));
            entity.resetProgress();
        }
    }

    protected static <E extends BlockEntity> boolean hasRecipe(E e) {
        if(e instanceof AbstractTKSBlockEntity){
            AbstractTKSBlockEntity entity = (AbstractTKSBlockEntity) e;
            SimpleContainer inventory = entity.convertItemHandlerToContainer();

            /**
             * This Code is not Objects
             */
            boolean hasGearInFirstSlot = entity.itemStackHandler.getStackInSlot(1).getItem() == TKSItems.WOOD_GEAR.get();

            return hasGearInFirstSlot && canInsertAmountIntoOutputSlot(inventory) && canInsertItemIntoOutputSlot(inventory, new ItemStack(TKSItems.STEEL_INGOT.get(),1));
        }
        return false;
    }

    /**
     * @deprecated
     * @param inventory
     * @param itemStack
     * @return
     */
    private static boolean canInsertItemIntoOutputSlot(SimpleContainer inventory, ItemStack itemStack) {
        return inventory.getItem(2).getItem() == itemStack.getItem() || inventory.getItem(2).isEmpty();
    }

    /**
     * @deprecated
     * @param inventory
     * @return
     */
    private static boolean canInsertAmountIntoOutputSlot(SimpleContainer inventory) {
        return  inventory.getItem(2).getMaxStackSize() > inventory.getItem(2).getCount();
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
}
