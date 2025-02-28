package org.pat.zombiesReimagined.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.pat.pattyEssentialsV3.Utils;
import org.pat.zombiesReimagined.Commands.Test;
import org.pat.zombiesReimagined.Utility.ZUtils;

import java.io.IOException;

public class Interact implements Listener {

    @EventHandler
    public void playerInteract(PlayerInteractEvent e) throws IOException {

        Player p = e.getPlayer();
        ItemStack item = e.getItem();

        if (e.getAction().isRightClick()) {
            if (item != null) {

                if (item != null && item.hasItemMeta() && item.getItemMeta().getPersistentDataContainer().has(ZUtils.key, PersistentDataType.STRING)) {
                    String storedValue = item.getItemMeta().getPersistentDataContainer().get(ZUtils.key, PersistentDataType.STRING);

                    switch (storedValue.toLowerCase()) {
                        case "rifle":
                            Test.shootGun(p);
                            Utils.scheduler.runTaskLater(ZUtils.plugin, () -> {
                                Test.shootGun(p);
                                Utils.scheduler.runTaskLater(ZUtils.plugin, () -> {
                                    Test.shootGun(p);
                                    Utils.scheduler.runTaskLater(ZUtils.plugin, () -> {
                                        Test.shootGun(p);
                                    }, 1);
                                }, 1);
                            }, 1);
                            break;
                        case "sniper":
                            Test.shootGun(p);
                            break;
                    }

                }

            }
        }

    }

}
