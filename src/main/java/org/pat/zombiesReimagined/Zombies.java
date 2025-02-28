package org.pat.zombiesReimagined;

import org.bukkit.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.pat.zombiesReimagined.Commands.Test;
import org.pat.zombiesReimagined.Commands.ZombiesItems;
import org.pat.zombiesReimagined.Listeners.Interact;
import org.pat.zombiesReimagined.Utility.FlatChunkGen;

public final class Zombies extends JavaPlugin {

    @Override
    public void onEnable() {

        World devWorld = createPracticeWorld();

        /** Initialize commands */
        getCommand("ztest").setExecutor(new Test());
        getCommand("zitems").setExecutor(new ZombiesItems());

        /** Initialize listeners */
        getServer().getPluginManager().registerEvents(new Interact(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
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
