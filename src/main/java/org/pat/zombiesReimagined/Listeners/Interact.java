package org.pat.zombiesReimagined.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.pat.pattyEssentialsV3.Utils;

import java.io.IOException;

public class Interact implements Listener {

    @EventHandler
    public void playerInteract(PlayerInteractEvent e) throws IOException {

        Player p = e.getPlayer();
        ItemStack item = e.getItem();

        if (e.getAction().isRightClick()) {
            if (item != null) {

                switch (item.getType()) {
                    case IRON_HOE:
                        p.performCommand("ztest 1");
                        Utils.scheduler.runTaskLater(Utils.plugin, () -> {
                            p.performCommand("ztest 1");
                        }, 1);
                        Utils.scheduler.runTaskLater(Utils.plugin, () -> {
                            p.performCommand("ztest 1");
                        }, 2);
                        break;
                    case NETHERITE_HOE:
                        p.performCommand("ztest 2");
                        break;
                }
            }
        }

    }

}
