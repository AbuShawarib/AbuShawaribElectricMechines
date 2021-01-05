package me.abushawarib.bluishmachines;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import io.github.thebusybiscuit.slimefun4.utils.itemstack.ItemStackWrapper;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu.AdvancedMenuClickHandler;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.CSCoreLibPlugin.cscorelib2.item.CustomItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces.InventoryBlock;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.cscorelib2.inventory.InvUtils;

public abstract class NonElectricAContainer extends SlimefunItem implements InventoryBlock {

    private static final int[] BORDER = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 13, 31, 36, 37, 38, 39, 40, 41, 42, 43, 44 };
    private static final int[] BORDER_IN = { 9, 10, 11, 12, 18, 21, 27, 28, 29, 30 };
    private static final int[] BORDER_OUT = { 14, 15, 16, 17, 23, 26, 32, 33, 34, 35 };

    public static Map<Block, MachineRecipe> processing = new HashMap<>();
    public static Map<Block, Integer> progress = new HashMap<>();

    protected final List<MachineRecipe> recipes = new ArrayList<>();

    private int processingSpeed = -1;

    boolean requiresWater = false;

    @ParametersAreNonnullByDefault
    public NonElectricAContainer(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        createPreset(this, getInventoryTitle(), this::constructMenu);

        registerBlockHandler(item.getItemId(), (p, b, tool, reason) -> {
            BlockMenu inv = BlockStorage.getInventory(b);

            if (inv != null) {
                inv.dropItems(b.getLocation(), getInputSlots());
                inv.dropItems(b.getLocation(), getOutputSlots());
            }

            progress.remove(b);
            processing.remove(b);
            return true;
        });

        registerDefaultRecipes();
    }

    @ParametersAreNonnullByDefault
    public NonElectricAContainer(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput) {
        this(category, item, recipeType, recipe);
        this.recipeOutput = recipeOutput;
    }

    protected void constructMenu(BlockMenuPreset preset) {
        for (int i : BORDER) {
            preset.addItem(i, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
        }

        for (int i : BORDER_IN) {
            preset.addItem(i, ChestMenuUtils.getInputSlotTexture(), ChestMenuUtils.getEmptyClickHandler());
        }

        for (int i : BORDER_OUT) {
            preset.addItem(i, ChestMenuUtils.getOutputSlotTexture(), ChestMenuUtils.getEmptyClickHandler());
        }

        preset.addItem(22, new CustomItem(Material.BLACK_STAINED_GLASS_PANE, " "), ChestMenuUtils.getEmptyClickHandler());

        for (int i : getOutputSlots()) {
            preset.addMenuClickHandler(i, new AdvancedMenuClickHandler() {

                @Override
                public boolean onClick(Player p, int slot, ItemStack cursor, ClickAction action) {
                    return false;
                }

                @Override
                public boolean onClick(InventoryClickEvent e, Player p, int slot, ItemStack cursor, ClickAction action) {
                    return cursor == null || cursor.getType() == null || cursor.getType() == Material.AIR;
                }
            });
        }
    }

    /**
     * This method returns the title that is used for the {@link Inventory} of an
     * {@link NonElectricAContainer} that has been opened by a Player.
     * 
     * Override this method to set the title.
     * 
     * @return The title of the {@link Inventory} of this {@link NonElectricAContainer}
     */
    @Nonnull
    public String getInventoryTitle() {
        return getItemName();
    }

    /**
     * This method returns the {@link ItemStack} that this {@link NonElectricAContainer} will
     * use as a progress bar.
     * 
     * Override this method to set the progress bar.
     * 
     * @return The {@link ItemStack} to use as the progress bar
     */
    public abstract ItemStack getProgressBar();

    /**
     * This method returns the speed at which this machine will operate.
     * This can be implemented on an instantiation-level to create different tiers
     * of machines.
     * 
     * @return The speed of this machine
     */
    public int getSpeed() {
        return processingSpeed;
    }

    /**
     * This sets the speed of this machine.
     * 
     * @param speed
     *            The speed multiplier for this machine, must be above zero
     * 
     * @return This method will return the current instance of {@link NonElectricAContainer}, so that can be chained.
     */
    public final NonElectricAContainer setProcessingSpeed(int speed) {
        Validate.isTrue(speed > 0, "The speed must be greater than zero!");

        this.processingSpeed = speed;
        return this;
    }

    @Override
    public void register(@Nonnull SlimefunAddon addon) {
        this.addon = addon;

        if (getSpeed() <= 0) {
            warn("The processing speed has not been configured correctly. The Item was disabled.");
            warn("Make sure to call '" + getClass().getSimpleName() + "#setProcessingSpeed(...)' before registering!");
        }

        if (getSpeed() > 0) {
            super.register(addon);
        }
    }

    /**
     * This method returns an internal identifier that is used to identify this {@link NonElectricAContainer}
     * and its recipes.
     * 
     * When adding recipes to an {@link NonElectricAContainer} we will use this identifier to
     * identify all instances of the same {@link NonElectricAContainer}.
     * This way we can add the recipes to all instances of the same machine.
     * 
     * <strong>This method will be deprecated and replaced in the future</strong>
     * 
     * @return The identifier of this machine
     */
    @Nonnull
    public abstract String getMachineIdentifier();

    /**
     * This method registers all default recipes for this machine.
     */
    protected void registerDefaultRecipes() {
        // Override this method to register your machine recipes
    }

    public List<MachineRecipe> getMachineRecipes() {
        return recipes;
    }

    public List<ItemStack> getDisplayRecipes() {
        List<ItemStack> displayRecipes = new ArrayList<>(recipes.size() * 2);

        for (MachineRecipe recipe : recipes) {
            if (recipe.getInput().length != 1) {
                continue;
            }

            displayRecipes.add(recipe.getInput()[0]);
            displayRecipes.add(recipe.getOutput()[0]);
        }

        return displayRecipes;
    }

    @Override
    public int[] getInputSlots() {
        return new int[] { 19, 20 };
    }

    @Override
    public int[] getOutputSlots() {
        return new int[] { 24, 25 };
    }

    public MachineRecipe getProcessing(Block b) {
        return processing.get(b);
    }

    public boolean isProcessing(Block b) {
        return getProcessing(b) != null;
    }

    public void registerRecipe(MachineRecipe recipe) {
        recipe.setTicks(recipe.getTicks() / getSpeed());
        recipes.add(recipe);
    }

    public void registerRecipe(int seconds, ItemStack[] input, ItemStack[] output) {
        registerRecipe(new MachineRecipe(seconds, input, output));
    }

    public void registerRecipe(int seconds, ItemStack input, ItemStack output) {
        registerRecipe(new MachineRecipe(seconds, new ItemStack[] { input }, new ItemStack[] { output }));
    }

    @Override
    public void preRegister() {
        addItemHandler(new BlockTicker() {

            @Override
            public void tick(Block b, SlimefunItem sf, Config data) {
                NonElectricAContainer.this.tick(b);
            }

            @Override
            public boolean isSynchronized() {
                return false;
            }
        });
    }

    protected void tick(Block b) {
        BlockMenu inv = BlockStorage.getInventory(b);


        if (isProcessing(b)) {

                int timeleft = progress.get(b);

                if (timeleft > 0) {
                    ChestMenuUtils.updateProgressbar(inv, 22, timeleft, processing.get(b).getTicks(), getProgressBar());

                    progress.put(b, timeleft - 1);
                } else {

                    inv.replaceExistingItem(22, new CustomItem(Material.BLACK_STAINED_GLASS_PANE, " "));

                    for (ItemStack output : processing.get(b).getOutput()) {
                        inv.pushItem(output.clone(), getOutputSlots());
                    }

                    Bukkit.getPluginManager().callEvent(
                        new AsyncMachineProcessCompleteEvent(b.getLocation(), NonElectricAContainer.this, getProcessing(b))
                    );

                    progress.remove(b);
                    processing.remove(b);
                }

        } else if (!requiresWater || waterBelow(b)) {
            MachineRecipe next = findNextRecipe(inv);

            if (next != null) {
                processing.put(b, next);
                progress.put(b, next.getTicks());
            }
        }
    }

    private boolean waterBelow(Block b) {

        Block fluid = b.getRelative(BlockFace.DOWN);
        BlockData data = fluid.getBlockData();

        if (
            fluid.isLiquid() &&
            (fluid.getType() == Material.WATER || fluid.getType() == Material.BUBBLE_COLUMN)
        ){
            if (data instanceof Levelled) {
                // Check if this is a full block.
                Levelled levelled = (Levelled) data;
                return levelled.getLevel() == 0;
            }
        }
        return false;
    }

    protected MachineRecipe findNextRecipe(BlockMenu inv) {
        Map<Integer, ItemStack> inventory = new HashMap<>();

        for (int slot : getInputSlots()) {
            ItemStack item = inv.getItemInSlot(slot);

            if (item != null) {
                inventory.put(slot, new ItemStackWrapper(item));
            }
        }

        Map<Integer, Integer> found = new HashMap<>();

        for (MachineRecipe recipe : recipes) {
            for (ItemStack input : recipe.getInput()) {
                for (int slot : getInputSlots()) {
                    if (SlimefunUtils.isItemSimilar(inventory.get(slot), input, true)) {
                        found.put(slot, input.getAmount());
                        break;
                    }
                }
            }

            if (found.size() == recipe.getInput().length) {
                if (!InvUtils.fitAll(inv.toInventory(), recipe.getOutput(), getOutputSlots())) {
                    return null;
                }

                for (Map.Entry<Integer, Integer> entry : found.entrySet()) {
                    inv.consumeItem(entry.getKey(), entry.getValue());
                }

                return recipe;
            } else {
                found.clear();
            }
        }

        return null;
    }
}