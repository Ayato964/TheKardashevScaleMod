package net.ayato.tksmod.item;

import net.ayato.tksmod.TheKardashevScaleMod;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class TKSItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, TheKardashevScaleMod.MODID);
    public static final RegistryObject<Item> WOOD_GEAR = ITEMS.register(Wood_Gear.ID, Wood_Gear::new );
    public static final RegistryObject<Item> STEEL_INGOT = ITEMS.register(Steel_Ingot.ID, Steel_Ingot::new );

    public static void register(IEventBus bus){
        ITEMS.register(bus);
    }
}
