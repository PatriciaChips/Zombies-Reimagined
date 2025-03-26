package org.pat.zombiesReimagined.Listeners;

import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.pat.zombiesReimagined.Utility.ItemUtils.Dev;
import org.pat.zombiesReimagined.Utility.ItemUtils.Guns;
import org.pat.zombiesReimagined.Utility.MapUtils.StructUtils.InteractStructure;
import org.pat.zombiesReimagined.Utility.ZUtils;

import java.io.IOException;

public class Interact implements Listener {

    @EventHandler
    public void playerInteract(PlayerInteractEvent e) throws IOException {

        Player p = e.getPlayer();
        ItemStack item = e.getItem();
        Block block = e.getClickedBlock();

        if (e.getHand() == EquipmentSlot.OFF_HAND)
            return;

        if (!(item != null && item.hasItemMeta() && item.getItemMeta().getPersistentDataContainer().has(ZUtils.key, PersistentDataType.STRING))) { // ITEM LACKS DATA RUN STRUCTURE INTERACT CHECK

            if (e.getAction().isRightClick()) {
                InteractStructure.interact(e, block, p); /** Run utils for interacted structures */
            }

        } else {
            if (item != null) {
                e.setCancelled(true); // item != null, assume use as gun || dev-tool
                String key = item.getItemMeta().getPersistentDataContainer().get(ZUtils.key, PersistentDataType.STRING);

                boolean isReload = e.getAction().isLeftClick();

                if (Guns.gunInteract(e, p, item, key, isReload)) // Item matches a gun, stop code
                    return;

                if (e.getAction().isRightClick()) {
                    if (Dev.devToolInteract(e, block, p, key)) // Item matches a dev-tool, stop code
                        return;
                }

            }

        }
    }

}


