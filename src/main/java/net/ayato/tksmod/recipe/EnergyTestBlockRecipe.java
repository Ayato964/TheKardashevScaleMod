package net.ayato.tksmod.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.ayato.tksmod.TheKardashevScaleMod;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class EnergyTestBlockRecipe extends AbstractTKSRecipe{

    public EnergyTestBlockRecipe(ResourceLocation id, ItemStack output, NonNullList<Ingredient> recipeItems) {
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
        return recipeItems.get(0).test(pContainer.getItem(0)); //搬入されたアイテム->pContainer 設定されているレシピ一蘭->recipeItems;
    }


    public static class Type extends AbstractTKSRecipe.Type implements RecipeType<EnergyTestBlockRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "energy_test_block";
    }


    public static class Serializer implements RecipeSerializer<EnergyTestBlockRecipe> {

        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(TheKardashevScaleMod.MODID, "energy_test_block");

        @Override
        public EnergyTestBlockRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "output"));

            JsonArray ingredients = GsonHelper.getAsJsonArray(pSerializedRecipe, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(1, Ingredient.EMPTY);
            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }

            return new EnergyTestBlockRecipe(pRecipeId, output, inputs);
        }

        @Override
        public @Nullable EnergyTestBlockRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(buf.readInt(), Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromNetwork(buf));
            }

            ItemStack output = buf.readItem();
            return new EnergyTestBlockRecipe(id, output, inputs);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, EnergyTestBlockRecipe recipe) {
            buf.writeInt(recipe.getIngredients().size());

            for (Ingredient ing : recipe.getIngredients()) {
                ing.toNetwork(buf);
            }
            buf.writeItemStack(recipe.getResultItem(), false);
        }
    }
}
