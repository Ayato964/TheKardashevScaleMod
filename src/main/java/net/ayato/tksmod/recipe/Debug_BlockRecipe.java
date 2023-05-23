package net.ayato.tksmod.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.ayato.tksmod.TheKardashevScaleMod;
import net.ayato.tksmod.block.Debug_Block;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import org.jetbrains.annotations.Nullable;

public class Debug_BlockRecipe extends AbstractTKSRecipe{

    public Debug_BlockRecipe(ResourceLocation id, ItemStack output, NonNullList<Ingredient> recipeItems) {
        super(id, output, recipeItems);
    }

    @Override
    protected RecipeSerializer<?> getMySerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    protected RecipeType<?> getMyType() {
        return Type.INSTANCE;
    }
    public static class Type extends AbstractTKSRecipe.Type implements RecipeType<Debug_BlockRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "debug_block";
    }


    public static class Serializer implements RecipeSerializer<Debug_BlockRecipe> {

        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(TheKardashevScaleMod.MODID, "debug_block");

        @Override
        public Debug_BlockRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "output"));

            JsonArray ingredients = GsonHelper.getAsJsonArray(pSerializedRecipe, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(1, Ingredient.EMPTY);
            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }

            return new Debug_BlockRecipe(pRecipeId, output, inputs);
        }

        @Override
        public @Nullable Debug_BlockRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(buf.readInt(), Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromNetwork(buf));
            }

            ItemStack output = buf.readItem();
            return new Debug_BlockRecipe(id, output, inputs);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, Debug_BlockRecipe recipe) {
            buf.writeInt(recipe.getIngredients().size());

            for (Ingredient ing : recipe.getIngredients()) {
                ing.toNetwork(buf);
            }
            buf.writeItemStack(recipe.getResultItem(), false);
        }
    }
}
