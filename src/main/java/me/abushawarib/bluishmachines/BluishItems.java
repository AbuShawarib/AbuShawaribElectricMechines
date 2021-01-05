package me.abushawarib.bluishmachines;

import io.github.thebusybiscuit.slimefun4.core.attributes.MachineTier;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;

import me.mrCookieSlime.Slimefun.Objects.Category;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class BluishItems {

    public BluishItems(BluishMachinesPlugin plugin) {

        Category category = SlimefunItems.AUTOMATED_CRAFTING_CHAMBER.getItem().getCategory();

        this.ELECTRIC_COMPOSTER_I = new ElectricComposter(
                plugin,
                category,
                MachineTier.BASIC,
                "I",
                8,
                16,
                1,
                5,
                new ItemStack(Material.OAK_SLAB),   SlimefunItems.ELECTRIC_MOTOR,       new ItemStack(Material.OAK_SLAB),
                new ItemStack(Material.OAK_SLAB),   SlimefunItems.HEATING_COIL,               new ItemStack(Material.OAK_SLAB),
                new ItemStack(Material.OAK_SLAB),   new ItemStack(Material.CAULDRON),   new ItemStack(Material.OAK_SLAB)
        );

        this.ELECTRIC_COMPOSTER_II = new ElectricComposter(
                plugin,
                category,
                MachineTier.AVERAGE,
                "II",
                15,
                30,
                2,
                15,
                null,                               SlimefunItems.MAGNESIUM_SALT,       null,
                SlimefunItems.CARBON,               ELECTRIC_COMPOSTER_I.getItem(),     SlimefunItems.CARBON,
                SlimefunItems.COMPRESSED_CARBON,    SlimefunItems.HEATING_COIL,         SlimefunItems.COMPRESSED_CARBON
        );

        this.ELECTRIC_COMPOSTER_III = new ElectricComposter(
                plugin,
                category,
                MachineTier.ADVANCED,
                "III",
                25,
                50,
                4,
                25,
                SlimefunItems.MAGNESIUM_SALT,       SlimefunItems.ELECTRIC_MOTOR,       SlimefunItems.MAGNESIUM_SALT,
                SlimefunItems.COOLING_UNIT,         ELECTRIC_COMPOSTER_II.getItem(),      SlimefunItems.HEATING_COIL,
                SlimefunItems.GILDED_IRON,          SlimefunItems.BLISTERING_INGOT_3,     SlimefunItems.GILDED_IRON
        );

        this.COBBLE_GENERATOR_I = new CobbleGen(
                plugin,
                category,
                MachineTier.ADVANCED,
                "I",
                1,
                30,
                SlimefunItems.MAGNESIUM_SALT,       SlimefunItems.ELECTRIC_MOTOR,       SlimefunItems.MAGNESIUM_SALT,
                SlimefunItems.COOLING_UNIT,         ELECTRIC_COMPOSTER_II.getItem(),      SlimefunItems.HEATING_COIL,
                SlimefunItems.GILDED_IRON,          SlimefunItems.BLISTERING_INGOT_3,     SlimefunItems.GILDED_IRON
        );

        this.ELECTRIC_COMPOSTER_I  .register(plugin);
        this.ELECTRIC_COMPOSTER_II .register(plugin);
        this.ELECTRIC_COMPOSTER_III.register(plugin);
        this.COBBLE_GENERATOR_I.register(plugin);
    }

    public final ElectricComposter ELECTRIC_COMPOSTER_I;
    public final ElectricComposter ELECTRIC_COMPOSTER_II;
    public final ElectricComposter ELECTRIC_COMPOSTER_III;
    public final CobbleGen COBBLE_GENERATOR_I;

}
