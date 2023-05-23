package net.ayato.tksmod.block.entity;

import net.ayato.tksmod.block.Debug_Block;
import net.ayato.tksmod.screen.Debug_BlockMenu;
import net.ayato.tksmod.screen.TKSModMenuTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class Debug_BlockEntity extends AbstractTKSBlockEntity{
    public static final String ID = Debug_Block.ID;
    public Debug_BlockEntity(BlockPos pos, BlockState state) {
        super(TKSBlockEntities.DEBUG_BLOCK.get(), pos, state);
    }
    @Override
    protected String getName() {
        return ID;
    }

    @Override
    protected int getStackBoxCount() {
        return 3;
    }

    @Override
    protected AbstractContainerMenu getCreateMenu(int id, Inventory inventory, Player player) {
        return new Debug_BlockMenu(id, inventory, this, this.data);
    }


}
