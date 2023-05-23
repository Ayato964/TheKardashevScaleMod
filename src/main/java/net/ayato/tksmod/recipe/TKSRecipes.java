package net.ayato.tksmod.recipe;

import net.ayato.tksmod.TheKardashevScaleMod;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class TKSRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, TheKardashevScaleMod.MODID);

    public static final RegistryObject<Debug_BlockRecipe.Serializer> DEBUG_BLOCK_SERIAL =
            SERIALIZERS.register("debug_block", ()-> Debug_BlockRecipe.Serializer.INSTANCE);
    public static final RegistryObject<EnergyTestBlockRecipe.Serializer> ENERGY_TEST_BLOCK_SERIAL =
            SERIALIZERS.register("energy_test_block", ()-> EnergyTestBlockRecipe.Serializer.INSTANCE);

    public static void register(IEventBus bus){
        SERIALIZERS.register(bus);
    }
}
