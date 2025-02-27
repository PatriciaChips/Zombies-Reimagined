package org.pat.zombiesReimagined;

import org.bukkit.plugin.java.JavaPlugin;
import org.pat.zombiesReimagined.Commands.Test;
import org.pat.zombiesReimagined.Listeners.Interact;

public final class Zombies extends JavaPlugin {

    @Override
    public void onEnable() {

        /** Initialize commands */
        getCommand("ztest").setExecutor(new Test());

        /** Initialize listeners */
        getServer().getPluginManager().registerEvents(new Interact(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
