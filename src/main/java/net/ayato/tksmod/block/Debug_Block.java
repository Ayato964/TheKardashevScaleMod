package net.ayato.tksmod.block;

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

public class Debug_Block extends AbstractTKSBlockAdditionalEntity {
    public static final String ID = "debug_block";
    public Debug_Block() {
        super(BlockBehaviour.Properties.of(Material.METAL).noOcclusion().strength(6f));
    }

    @Override
    protected String getDescripted() {
        return ID;
    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState state) {

        return new Debug_BlockEntity(blockPos, state);
    }

    @Override
    protected <E extends BlockEntity> BlockEntityType<E> getEntityType() {
        return (BlockEntityType<E>) TKSBlockEntities.DEBUG_BLOCK.get();
    }

    @Override
    protected <E extends BlockEntity> BlockEntityTicker<E> getTickeMethod() {
        return Debug_BlockEntity::tick;
    }
}
