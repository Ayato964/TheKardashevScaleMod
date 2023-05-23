package net.ayato.tksmod;

import com.mojang.logging.LogUtils;
import net.ayato.tksmod.block.TKSBlocks;
import net.ayato.tksmod.block.entity.TKSBlockEntities;
import net.ayato.tksmod.item.TKSItems;
import net.ayato.tksmod.recipe.TKSRecipes;
import net.ayato.tksmod.screen.Debug_BlockScreen;
import net.ayato.tksmod.screen.TKSModMenuTypes;
import net.ayato.tksmod.tabs.CreativeTabs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(TheKardashevScaleMod.MODID)
public class TheKardashevScaleMod
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "tksmod";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public TheKardashevScaleMod()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register the Deferred Register to the mod event bus so blocks get registered
        TKSBlocks.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        TKSItems.register(modEventBus);

        //Register BlockEntities
        TKSBlockEntities.register(modEventBus);

        TKSModMenuTypes.register(modEventBus);

        TKSRecipes.register(modEventBus);
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");
        LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));
    }

    private void addCreative(CreativeModeTabEvent.BuildContents event)
    {
        if (event.getTab() == CreativeTabs.ITEM_TAB){
            event.accept(TKSItems.WOOD_GEAR);
            event.accept(TKSItems.STEEL_INGOT);
        }
        if (event.getTab() == CreativeTabs.BLOCK_TAB) {
            event.accept(TKSBlocks.STEEL_BLOCK);
            event.accept(TKSBlocks.DEBUG_BLOCK);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());

            MenuScreens.register((MenuType)TKSModMenuTypes.DEBUG_BLOCK_MENU.get(), Debug_BlockScreen::new);
        }
    }
}
