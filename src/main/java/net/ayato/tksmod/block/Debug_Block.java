package net.ayato.tksmod.block;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

public class Debug_Block extends AbstractTKSBlock{
    public static final String ID = "debug_block";
    public Debug_Block() {
        super(BlockBehaviour.Properties.of(Material.METAL).noOcclusion().strength(6f));
    }

    @Override
    protected String getDescripted() {
        return ID;
    }
}
