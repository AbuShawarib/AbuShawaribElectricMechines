package me.abushawarib.abushawaribelectricmechines;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.core.attributes.MachineTier;
import io.github.thebusybiscuit.slimefun4.core.attributes.MachineType;
import io.github.thebusybiscuit.slimefun4.core.researching.Research;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.utils.LoreBuilder;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.cscorelib2.config.Config;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;

public class AbuShawaribElectricMechines extends JavaPlugin implements SlimefunAddon {

    @Override
    public void onEnable() {
        // Read something from your config.yml
        Config cfg = new Config(this);

        if (cfg.getBoolean("options.auto-update")) {
            // TODO You could start an Auto-Updater for example
        }

        // Create a new Category
        // This Category will use this ItemStack
        ItemStack categoryItem = new CustomItem(Material.GRAY_TERRACOTTA, "&4Bluish Mechines", "", "&a> Click to open");

        // Give your Category a unique id.
        NamespacedKey categoryId = new NamespacedKey(this, "bluish_mechines");
        Category category = new Category(categoryId, categoryItem);

        // Create a new Slimefun ItemStack
        // This class has many constructors, it is very important that you give each item a unique id.
        SlimefunItemStack slimefunItem = new SlimefunItemStack(
            "ELECTRIC_COMPOSTER_I", Material.COMPOSTER,
            "&4Electric Composter (I)", "&c+20% Dryness",
            LoreBuilder.machine(MachineTier.BASIC, MachineType.MACHINE),
            LoreBuilder.speed(1), LoreBuilder.powerPerSecond(32)
        );

        // The Recipe is an ItemStack Array with a length of 9.
        // It represents a Shaped Recipe in a 3x3 crafting grid
        // The machine in which this recipe is crafted in is specified further down
        ItemStack[] recipe = { 
            new ItemStack(Material.OAK_SLAB),   SlimefunItems.ELECTRIC_MOTOR,       new ItemStack(Material.OAK_SLAB),
            new ItemStack(Material.OAK_SLAB),   SlimefunItems.HEATING_COIL,         new ItemStack(Material.OAK_SLAB),
            new ItemStack(Material.OAK_SLAB),   new ItemStack(Material.CAULDRON),   new ItemStack(Material.OAK_SLAB)
            };

        // Now you just have to register the item
        // RecipeType.ENHANCED_CRAFTING_TABLE refers to the machine in which this item is crafted in.
        // Recipy Types from Slimefun itself will automatically add the recipe to that machine
        AContainer composter = new ElectricComposter(category, slimefunItem, RecipeType.ENHANCED_CRAFTING_TABLE, recipe)
        .setCapacity(32)
        .setEnergyConsumption(16)
        .setProcessingSpeed(2);
        composter.register(this);

        NamespacedKey researchKey = new NamespacedKey(this, "electronic_composter_1");
        Research research = new Research(researchKey, 90879848, "Electric Life Cycle I", 1);
        research.addItems(composter);

        research.register();
    }

    @Override
    public void onDisable() {
        // Logic for disabling the plugin...
    }

    @Override
    public String getBugTrackerURL() {
        // You can return a link to your Bug Tracker instead of null here
        return null;
    }

    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }

}
