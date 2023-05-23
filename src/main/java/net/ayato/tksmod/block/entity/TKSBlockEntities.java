package net.ayato.tksmod.block.entity;

import net.ayato.tksmod.TheKardashevScaleMod;
import net.ayato.tksmod.block.TKSBlocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class TKSBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, TheKardashevScaleMod.MODID);

    public static final RegistryObject<BlockEntityType<?>> DEBUG_BLOCK =
            BLOCK_ENTITY.register(Debug_BlockEntity.ID, ()->BlockEntityType
                        .Builder.of(Debug_BlockEntity::new, TKSBlocks.DEBUG_BLOCK.get()).build(null));

    public static void register(IEventBus bus){
        BLOCK_ENTITY.register(bus);
    }
}
