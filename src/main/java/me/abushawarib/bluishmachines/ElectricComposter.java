package me.abushawarib.bluishmachines;

import io.github.thebusybiscuit.slimefun4.core.attributes.MachineTier;
import io.github.thebusybiscuit.slimefun4.core.attributes.MachineType;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun4.core.researching.Research;
import io.github.thebusybiscuit.slimefun4.utils.LoreBuilder;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class ElectricComposter extends AContainer implements RecipeDisplayItem {

    public ElectricComposter(BluishMachinesPlugin plugin,
                             Category category,
                             MachineTier tier,
                             String rank,
                             int consumption,
                             int capacity,
                             int speed,
                             int researchCost,
                             ItemStack ... recipe)
    {
        super(
                category,
                new SlimefunItemStack(
                        "ELECTRIC_COMPOSTER_" + rank, Material.COMPOSTER,
                        "&aElectric Composter (" + rank + ")",
                        " ",
                        LoreBuilder.machine(tier, MachineType.MACHINE),
                        LoreBuilder.speed(speed),
                        LoreBuilder.powerBuffer(capacity),
                        LoreBuilder.powerPerSecond(consumption),
                        " ",
                        "&9[Bluish Machines]"
                ),
                RecipeType.ENHANCED_CRAFTING_TABLE,
                recipe
        );

        this.consumption = consumption;
        this.capacity = capacity;
        this.speed = speed;

        registerRecipe(90, new ItemStack[] { new ItemStack(Material.STONE, 64) }, new ItemStack[] { new ItemStack(Material.NETHERRACK, 8) });
        registerRecipe(90, new ItemStack[] { new ItemStack(Material.SAND, 64) }, new ItemStack[] { new ItemStack(Material.SOUL_SAND, 8) });

        Research research = new Research(
                new NamespacedKey(plugin, "ELECTRIC_COMPOSTER_" + rank),
                ("ELECTRIC_COMPOSTER_" + rank).hashCode(), //what the fuck is this even for
                "Electric Life Cycle " + rank,
                researchCost
        );

        research.addItems(this);
        research.register();
    }

    private final int speed;
    private final int capacity;
    private final int consumption;

    @Override public int getSpeed() { return speed; }
    @Override public int getCapacity() { return capacity; }
    @Override public int getEnergyConsumption() { return consumption; }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.NETHERITE_HOE);
    }
    
    @Override
    public String getMachineIdentifier() {
        return "ELECTRIC_COMPOSTER";
    }
}
