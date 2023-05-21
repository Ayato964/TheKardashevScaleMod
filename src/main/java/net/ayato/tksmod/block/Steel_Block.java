package net.ayato.tksmod.block;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

public class Steel_Block extends AbstractTKSBlock{
    public static final String ID = "steel_block";
    public Steel_Block() {
        super(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(6F));
    }

    @Override
    protected String getDescripted() {
        return ID;
    }
}
