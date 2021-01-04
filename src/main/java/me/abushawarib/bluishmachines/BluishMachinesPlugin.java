package me.abushawarib.bluishmachines;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;

import org.bukkit.plugin.java.JavaPlugin;

public class BluishMachinesPlugin extends JavaPlugin implements SlimefunAddon {

    private BluishItems items;
    public BluishItems getItems() { return items; }

    @Override
    public void onEnable() {

        this.items = new BluishItems(this);
    }

    @Override
    public String getBugTrackerURL() { return null; }

    @Override
    public JavaPlugin getJavaPlugin() { return this; }

}
