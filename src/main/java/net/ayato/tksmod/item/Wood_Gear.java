package net.ayato.tksmod.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Wood_Gear extends AbstractTKSItem {
    public static final String ID = "wood_gear";
    public Wood_Gear() {
        super(new Properties().stacksTo(64));
    }

    @Override
    protected String getDescripted() {
        return ID;
    }
}
