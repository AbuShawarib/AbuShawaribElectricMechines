package me.abushawarib.abushawaribelectricmechines;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class ElectricComposter extends AContainer implements RecipeDisplayItem {

    public ElectricComposter(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    protected void registerDefaultRecipes() {
        registerRecipe(90, new ItemStack[] { new ItemStack(Material.STONE, 64) }, new ItemStack[] { new ItemStack(Material.NETHERRACK, 8) });
        registerRecipe(90, new ItemStack[] { new ItemStack(Material.SAND, 64) }, new ItemStack[] { new ItemStack(Material.SOUL_SAND, 8) });
    }


    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.NETHERITE_HOE);
    }

    @Override
    public String getInventoryTitle() {
        return "&4Electric Composter";
    }

    @Override
    public String getMachineIdentifier() {
        return "ELECTRIC_COMPOSTER_I";
    }
}
