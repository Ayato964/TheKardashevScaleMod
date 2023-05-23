package net.ayato.tksmod.block;

import net.ayato.tksmod.block.entity.AbstractTKSBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractTKSBlockAdditionalEntity extends AbstractTKSBlock implements EntityBlock {

    public AbstractTKSBlockAdditionalEntity(Properties properties) {
        super(properties);
    }

    @Override
    public RenderShape getRenderShape(BlockState p_60550_) {
        return RenderShape.MODEL;
    }


    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult hitResult) {
        if (!level.isClientSide()){
            BlockEntity entity = level.getBlockEntity(blockPos);
            if(entity instanceof AbstractTKSBlockEntity)
                NetworkHooks.openScreen(((ServerPlayer) player),(AbstractTKSBlockEntity) entity, blockPos);
            else
                throw new IllegalStateException(this.getClass() + "のプロバイダーでエラーが発生しました。");

        }

        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos blockPos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()){
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof AbstractTKSBlockEntity){
                ((AbstractTKSBlockEntity) blockEntity).drops();
            }
        }
        super.onRemove(state, level, blockPos, newState, isMoving);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> entityType) {
        return createTickerHelper(entityType, getEntityType(), getTickeMethod());
    }

    @javax.annotation.Nullable
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> p_152133_, BlockEntityType<E> p_152134_, BlockEntityTicker<? super E> p_152135_) {
        return p_152134_ == p_152133_ ? (BlockEntityTicker<A>)p_152135_ : null;
    }
    protected abstract  <E extends  BlockEntity> BlockEntityType<E> getEntityType();
    protected abstract <E extends  BlockEntity> BlockEntityTicker<E> getTickeMethod();
}
