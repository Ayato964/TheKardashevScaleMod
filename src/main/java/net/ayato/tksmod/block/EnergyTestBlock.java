package net.ayato.tksmod.block;

import net.ayato.tksmod.block.entity.Debug_BlockEntity;
import net.ayato.tksmod.block.entity.EnergyTestBlockEntity;
import net.ayato.tksmod.block.entity.TKSBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import org.jetbrains.annotations.Nullable;

public class EnergyTestBlock extends AbstractTKSBlockAdditionalEntity{
    public static final String ID = "energy_test_block";
    public EnergyTestBlock() {
        super(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3F));
    }

    @Override
    protected String getDescripted() {
        return ID;
    }

    @Override
    protected <E extends BlockEntity> BlockEntityType<E> getEntityType() {

        return (BlockEntityType<E>) TKSBlockEntities.ENERGY_TEST_BLOCK.get();
    }

    @Override
    protected <E extends BlockEntity> BlockEntityTicker<E> getTickeMethod() {
        return EnergyTestBlockEntity::tick;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {

        return new EnergyTestBlockEntity(pPos, pState);
    }
}
