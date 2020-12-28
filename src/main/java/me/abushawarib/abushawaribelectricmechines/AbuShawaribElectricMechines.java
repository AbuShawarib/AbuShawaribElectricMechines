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

        // Create Bluish Mechines Category
        ItemStack categoryItem = new CustomItem(Material.GRAY_TERRACOTTA, "&4Bluish Mechines", "", "&a> Click to open");
        NamespacedKey categoryId = new NamespacedKey(this, "bluish_mechines");
        Category category = new Category(categoryId, categoryItem);

        // Create a new Slimefun ItemStack (Item Description) for different tiers
        SlimefunItemStack electricComposter1Item = new SlimefunItemStack(
            "ELECTRIC_COMPOSTER_I", Material.COMPOSTER,
            "&4Electric Composter (I)", "&c+25% Dryness",
            LoreBuilder.machine(MachineTier.BASIC, MachineType.MACHINE),
            LoreBuilder.speed(1), LoreBuilder.powerPerSecond(16)
        );
        SlimefunItemStack electricComposter2Item = new SlimefunItemStack(
            "ELECTRIC_COMPOSTER_II", Material.COMPOSTER,
            "&4Electric Composter (II)", "&c+50% Dryness",
            LoreBuilder.machine(MachineTier.AVERAGE, MachineType.MACHINE),
            LoreBuilder.speed(2), LoreBuilder.powerPerSecond(30)
        );
        SlimefunItemStack electricComposter3Item = new SlimefunItemStack(
            "ELECTRIC_COMPOSTER_III", Material.COMPOSTER,
            "&4Electric Composter (III)", "&c+100% Dryness",
            LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE),
            LoreBuilder.speed(4), LoreBuilder.powerPerSecond(50)
        );

        // Register items into slimefun with research
        if (cfg.getBoolean("options.electric-composter1")) {
            AContainer composter1 = new ElectricComposter(category, electricComposter1Item, RecipeType.ENHANCED_CRAFTING_TABLE, 
            new ItemStack[] {
                new ItemStack(Material.OAK_SLAB),   SlimefunItems.ELECTRIC_MOTOR,       new ItemStack(Material.OAK_SLAB),
                new ItemStack(Material.OAK_SLAB),   SlimefunItems.HEATING_COIL,         new ItemStack(Material.OAK_SLAB),
                new ItemStack(Material.OAK_SLAB),   new ItemStack(Material.CAULDRON),   new ItemStack(Material.OAK_SLAB)
            })
            .setCapacity(8)
            .setEnergyConsumption(8)
            .setProcessingSpeed(1);
            composter1.register(this);

            AContainer composter2 = new ElectricComposter(category, electricComposter2Item, RecipeType.ENHANCED_CRAFTING_TABLE, 
            new ItemStack[] {
                null,                               SlimefunItems.MAGNESIUM_SALT,       null,
                SlimefunItems.CARBON,               electricComposter1Item,             SlimefunItems.CARBON,
                SlimefunItems.COMPRESSED_CARBON,    SlimefunItems.HEATING_COIL,         SlimefunItems.COMPRESSED_CARBON
            })
            .setCapacity(16)
            .setEnergyConsumption(15)
            .setProcessingSpeed(2);
            composter2.register(this);

            NamespacedKey composter1ResearchKey = new NamespacedKey(this, "electronic_composter_1");
            Research composter1Research = new Research(composter1ResearchKey, 90879848, "Electric Life Cycle I", 1);
            composter1Research.addItems(composter1);
            composter1Research.addItems(composter2);
            composter1Research.register();

            if (cfg.getBoolean("options.electric-composter2")) {
                AContainer composter3 = new ElectricComposter(category, electricComposter3Item, RecipeType.ENHANCED_CRAFTING_TABLE, 
                new ItemStack[] {
                    SlimefunItems.MAGNESIUM_SALT,       SlimefunItems.ELECTRIC_MOTOR,       SlimefunItems.MAGNESIUM_SALT,
                    SlimefunItems.COOLING_UNIT,         electricComposter2Item,             SlimefunItems.HEATING_COIL,
                    SlimefunItems.GILDED_IRON,          SlimefunItems.BLISTERING_INGOT_3,     SlimefunItems.GILDED_IRON
                })
                .setCapacity(32)
                .setEnergyConsumption(25)
                .setProcessingSpeed(4);
                composter3.register(this);
                
                NamespacedKey composter2ResearchKey = new NamespacedKey(this, "electronic_composter_2");
                Research composter2Research = new Research(composter2ResearchKey, 90874568, "Electric Life Cycle II", 1);
                composter2Research.addItems(composter3);
                composter2Research.register();
            }
        }
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
