package org.pat.zombiesReimagined;

import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.pat.pattyEssentialsV3.ColoredText;
import org.pat.zombiesReimagined.Commands.Test;
import org.pat.zombiesReimagined.Commands.ZombiesItems;
import org.pat.zombiesReimagined.Listeners.Interact;
import org.pat.zombiesReimagined.Listeners.Join;
import org.pat.zombiesReimagined.Listeners.SwapItem;
import org.pat.zombiesReimagined.Utility.FlatChunkGen;
import org.pat.zombiesReimagined.Utility.ItemUtils.Guns;
import org.pat.zombiesReimagined.Utility.ItemUtils.Item;
import org.pat.zombiesReimagined.Utility.MapUtils.StructUtils.Selection;
import org.pat.zombiesReimagined.Utility.MapUtils.StructUtils.SpawnStructure;
import org.pat.zombiesReimagined.Utility.ZUtils;

public final class Zombies extends JavaPlugin {

    @Override
    public void onEnable() {

        World devWorld = createPracticeWorld();

        /** Initialize commands */
        getCommand("ztest").setExecutor(new Test());
        getCommand("zitems").setExecutor(new ZombiesItems());

        /** Initialize listeners */
        getServer().getPluginManager().registerEvents(new Interact(), this);
        getServer().getPluginManager().registerEvents(new SwapItem(), this);
        getServer().getPluginManager().registerEvents(new Join(), this);

        new BukkitRunnable() {
            public void run() {

                for (Player player : Bukkit.getOnlinePlayers()) {
                    ItemStack itemStack = player.getInventory().getItemInMainHand();
                    if (itemStack != null && Guns.registeredGuns.containsKey(itemStack)) {
                        Item gun = Guns.registeredGuns.get(itemStack);
                        player.sendActionBar(Component.text(ColoredText.t("&7" + gun.getAmmo() + " / " + gun.getMagSize())));
                        player.setLevel(gun.getExtraAmmo());
                        player.setExp(0);
                        player.setExp(gun.getAmmo() == gun.getMagSize() ? 1 : (float) gun.getAmmo()/(float) gun.getMagSize());
                    }
                }

            }
        }.runTaskTimer(ZUtils.plugin, 0L, 0L);

    }

    @Override
    public void onDisable() {

        SpawnStructure.reset(null);

        for (var selectionDisplay : Selection.selectionDisplay.entrySet()) {
            selectionDisplay.getValue().remove();
        }

    }

    public static World createPracticeWorld() {
        WorldCreator wc = new WorldCreator("zombies-dev");

        wc.environment(World.Environment.NORMAL);
        //wc.type(WorldType.FLAT);
        wc.generateStructures(false);
        wc.generator(new FlatChunkGen());
        //wc.generatorSettings("{ \"structures\"\\: {}, \"layers\"\\: [ { \"height\"\\: 1, \"block\"\\: \"minecraft\\:pink_concrete\" } ], \"biome\"\\: \"minecraft\\:the_void\", \"flat_world_options\"\\: \"minecraft\\:pink_concrete;minecraft\\:the_void\" }");

        Bukkit.getServer().createWorld(wc);

        World world = Bukkit.getWorld("zombies-dev");

        world.setDifficulty(Difficulty.HARD);
        world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
        world.setGameRule(GameRule.KEEP_INVENTORY, true);
        world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        world.setGameRule(GameRule.COMMAND_BLOCK_OUTPUT, false);
        world.setGameRule(GameRule.DO_MOB_LOOT, false);
        world.setGameRule(GameRule.SPAWN_RADIUS, 5000);
        world.getWorldBorder().setSize(5500);
        return world;
    }

}
