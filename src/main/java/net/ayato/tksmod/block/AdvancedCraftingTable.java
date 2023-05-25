package net.ayato.tksmod.block;

import net.ayato.tksmod.block.entity.AdvancedCraftingTableEntity;
import net.ayato.tksmod.block.entity.Debug_BlockEntity;
import net.ayato.tksmod.block.entity.TKSBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import org.jetbrains.annotations.Nullable;

public class AdvancedCraftingTable extends AbstractTKSBlockAdditionalEntity{
    public static final String ID = "advanced_crafting_table";
    public AdvancedCraftingTable() {
        super(BlockBehaviour.Properties.of(Material.METAL).destroyTime(1F).noOcclusion());
    }

    @Override
    protected String getDescripted() {
        return ID;
    }

    @Override
    protected <E extends BlockEntity> BlockEntityType<E> getEntityType() {
        return (BlockEntityType<E>) TKSBlockEntities.ADVANCED_CRAFTING_TABLE.get();
    }

    @Override
    protected <E extends BlockEntity> BlockEntityTicker<E> getTickeMethod() {
        return AdvancedCraftingTableEntity::tick;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new AdvancedCraftingTableEntity(pPos, pState);
    }


}
