package org.pat.zombiesReimagined.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.pat.pattyEssentialsV3.Utils;
import org.pat.zombiesReimagined.Utility.MapUtils.FeatureType;
import org.pat.zombiesReimagined.Utility.MapUtils.IdentifiedStructures;
import org.pat.zombiesReimagined.Utility.MapUtils.MapFeature;
import org.pat.zombiesReimagined.Utility.ZUtils;

import java.io.IOException;

public class Join implements Listener {

    @EventHandler
    public void playerJoin(PlayerJoinEvent e) throws IOException {

        for (var v : MapFeature.storedStructures.entrySet()) {
            if (v.getKey().getType() == FeatureType.GUN) {
                v.getKey().statusVisibilitySwap(false);
            } else if (v.getKey().getType() == FeatureType.CHEST) {
                v.getKey().statusVisibilitySwap(true);
            }
        }

    }

}
