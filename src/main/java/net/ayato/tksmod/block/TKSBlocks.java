package net.ayato.tksmod.block;

import net.ayato.tksmod.TheKardashevScaleMod;
import net.ayato.tksmod.item.TKSItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class TKSBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, TheKardashevScaleMod.MODID);

    public static final RegistryObject<Block> STEEL_BLOCK = registerBlock(Steel_Block.ID, Steel_Block::new);
    public static final RegistryObject<Block> DEBUG_BLOCK = registerBlock(Debug_Block.ID, Debug_Block::new);

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block ){
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }
    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block){
        return TKSItems.ITEMS.register(name, ()-> new BlockItem(block.get(), new Item.Properties()));
    }
    public static void register(IEventBus bus){
        BLOCKS.register(bus);
    }

}
