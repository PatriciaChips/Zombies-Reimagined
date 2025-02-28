package org.pat.zombiesReimagined.Utility;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Utility;
import org.bukkit.plugin.Plugin;
import org.pat.pattyEssentialsV3.PattyEssentialsV3;
import org.pat.pattyEssentialsV3.Utils;
import org.pat.zombiesReimagined.Zombies;

public class ZUtils {

    public static Plugin plugin = Zombies.getPlugin(Zombies.class);
    public static NamespacedKey key = new NamespacedKey(ZUtils.plugin, "item");

}
