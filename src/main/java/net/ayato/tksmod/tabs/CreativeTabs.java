package net.ayato.tksmod.tabs;

import net.ayato.tksmod.TheKardashevScaleMod;
import net.ayato.tksmod.item.TKSItems;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.awt.*;

@Mod.EventBusSubscriber(modid = TheKardashevScaleMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CreativeTabs {
    public static CreativeModeTab ITEM_TAB;
    public static CreativeModeTab BLOCK_TAB;
    @SubscribeEvent
    public static void registerCreativeTabs(CreativeModeTabEvent.Register event){
        ITEM_TAB = event.registerCreativeModeTab(new ResourceLocation(TheKardashevScaleMod.MODID, "item_tab"),
                builder -> builder.icon(()-> new ItemStack(TKSItems.WOOD_GEAR.get())).title(Component.translatable("tkstabs.item_tab")));
        BLOCK_TAB = event.registerCreativeModeTab(new ResourceLocation(TheKardashevScaleMod.MODID, "block_tab"),
                builder -> builder.icon(()-> new ItemStack(TKSItems.STEEL_INGOT.get())).title(Component.translatable("tkstabs.block_tab")));
    }
}
