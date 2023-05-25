package net.ayato.tksmod.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.ayato.tksmod.TheKardashevScaleMod;
import net.ayato.tksmod.block.AdvancedCraftingTable;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class AdvancedCraftingTableRecipe extends AbstractTKSRecipe{
    public AdvancedCraftingTableRecipe(ResourceLocation id, ItemStack output, NonNullList<Ingredient> recipeItems) {
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

    @Override
    protected boolean getMatches(SimpleContainer pContainer, Level pLevel) {
        for(int i = 0; i < 9; i ++)
            if(!recipeItems.get(i).test(pContainer.getItem(i)))
                return false;
        return true;
    }


    public static class Type extends AbstractTKSRecipe.Type implements RecipeType<AdvancedCraftingTableRecipe> {
        private Type() { }
        public static final AdvancedCraftingTableRecipe.Type INSTANCE = new AdvancedCraftingTableRecipe.Type();
        public static final String ID = AdvancedCraftingTable.ID;
    }


    public static class Serializer implements RecipeSerializer<AdvancedCraftingTableRecipe> {

        public static final AdvancedCraftingTableRecipe.Serializer INSTANCE = new AdvancedCraftingTableRecipe.Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(TheKardashevScaleMod.MODID, AdvancedCraftingTable.ID);

        @Override
        public AdvancedCraftingTableRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "output"));

            JsonObject key = GsonHelper.getAsJsonObject(pSerializedRecipe, "key");
            JsonArray pattern = GsonHelper.getAsJsonArray(pSerializedRecipe, "pattern");
            StringBuilder patterns = convertPatterns(pattern);
            NonNullList<Ingredient> inputs = NonNullList.withSize(9, Ingredient.EMPTY);
            for(int i = 0; i < 9; i ++){
                if(patterns.charAt(i) == ' ')
                    inputs.set(i, Ingredient.EMPTY);
                else
                    inputs.set(i, Ingredient.fromJson(key.get(String.valueOf(patterns.charAt(i)))));
            }
            /*
            JsonArray ingredients = GsonHelper.getAsJsonArray(pSerializedRecipe, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(1, Ingredient.EMPTY);
            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }

             */

            return new AdvancedCraftingTableRecipe(pRecipeId, output, inputs);
        }

        private StringBuilder convertPatterns(JsonArray pattern) {
            StringBuilder b = new StringBuilder();
            for(int i = 0; i < 3; i ++){
                b.append(pattern.get(i).getAsString());
            }
            return b;
        }

        @Override
        public @Nullable AdvancedCraftingTableRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(buf.readInt(), Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromNetwork(buf));
            }

            ItemStack output = buf.readItem();
            return new AdvancedCraftingTableRecipe(id, output, inputs);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, AdvancedCraftingTableRecipe recipe) {
            buf.writeInt(recipe.getIngredients().size());

            for (Ingredient ing : recipe.getIngredients()) {
                ing.toNetwork(buf);
            }
            buf.writeItemStack(recipe.getResultItem(), false);
        }
    }
}
