package net.ayato.tksmod.block;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class AbstractTKSBlock extends Block {
    public AbstractTKSBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack p_49816_, @Nullable BlockGetter p_49817_, List<Component> p_49818_, TooltipFlag p_49819_) {
        super.appendHoverText(p_49816_, p_49817_, p_49818_, p_49819_);
        p_49818_.add(Component.translatable("item.tksmod." + getDescripted() + ".desc").withStyle(ChatFormatting.GRAY));

    }

    protected abstract String getDescripted();
}
