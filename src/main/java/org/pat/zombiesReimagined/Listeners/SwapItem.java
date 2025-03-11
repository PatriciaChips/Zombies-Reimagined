package org.pat.zombiesReimagined.Listeners;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.pat.pattyEssentialsV3.Utils;
import org.pat.zombiesReimagined.Utility.MapUtils.IdentifiedStructures;
import org.pat.zombiesReimagined.Utility.ZUtils;

import java.io.IOException;

public class SwapItem implements Listener {

    @EventHandler
    public void playerSwap(PlayerItemHeldEvent e) throws IOException {

        Player p = e.getPlayer();
        ItemStack swapToItem = p.getInventory().getItem(e.getNewSlot());
        ItemStack swapFromItem = p.getInventory().getItem(e.getPreviousSlot());

        if (swapToItem != null && swapToItem.getPersistentDataContainer().get(ZUtils.key, PersistentDataType.STRING) != null) {
            for (var v : IdentifiedStructures.gunIdentifiers.entrySet()) {
                if (swapToItem.getPersistentDataContainer().get(ZUtils.key, PersistentDataType.STRING).equals(v.getValue().getKey())) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (player != p) {
                            Utils.scheduler.runTaskLater(ZUtils.plugin, () -> {
                                ItemStack adjustedItem = swapToItem.clone();
                                ItemMeta itemMeta = adjustedItem.getItemMeta();
                                itemMeta.setItemModel(new NamespacedKey("minecraft", adjustedItem.getType().toString().toLowerCase()));
                                adjustedItem.setItemMeta(itemMeta);
                                player.sendEquipmentChange(p, EquipmentSlot.HAND, adjustedItem);
                            }, 2);
                        }
                    }
                }
            }
        }

    }

}
