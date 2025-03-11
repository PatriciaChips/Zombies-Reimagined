package org.pat.zombiesReimagined.Listeners;

import org.bukkit.*;
import org.bukkit.Color;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import org.joml.Matrix4f;
import org.pat.pattyEssentialsV3.ColoredText;
import org.pat.pattyEssentialsV3.Utils;
import org.pat.zombiesReimagined.Commands.Test;
import org.pat.zombiesReimagined.Commands.ZombiesItems;
import org.pat.zombiesReimagined.Utility.ItemUtils.Dev;
import org.pat.zombiesReimagined.Utility.ItemUtils.Guns;
import org.pat.zombiesReimagined.Utility.ItemUtils.Item;
import org.pat.zombiesReimagined.Utility.ItemUtils.UseType;
import org.pat.zombiesReimagined.Utility.MapUtils.FeatureType;
import org.pat.zombiesReimagined.Utility.MapUtils.IdentifiedStructures;
import org.pat.zombiesReimagined.Utility.MapUtils.MapFeature;
import org.pat.zombiesReimagined.Utility.MapUtils.StructUtils.InteractStructure;
import org.pat.zombiesReimagined.Utility.MapUtils.StructUtils.SpawnStructure;
import org.pat.zombiesReimagined.Utility.ZUtils;

import java.io.IOException;
import java.util.*;
import java.util.List;

public class Interact implements Listener {

    @EventHandler
    public void playerInteract(PlayerInteractEvent e) throws IOException {

        Player p = e.getPlayer();
        ItemStack item = e.getItem();
        Block block = e.getClickedBlock();

        if (e.getHand() == EquipmentSlot.OFF_HAND)
            return;

        if (e.getAction().isRightClick()) {

            if (!(item != null && item.hasItemMeta() && item.getItemMeta().getPersistentDataContainer().has(ZUtils.key, PersistentDataType.STRING))) { // ITEM LACKS DATA RUN STRUCTURE INTERACT CHECK

                InteractStructure.interact(e, block, p); /** Run utils for interacted structures */

            } else {
                if (item != null) {
                    e.setCancelled(true); // item != null, assume use as gun || dev-tool
                    String key = item.getItemMeta().getPersistentDataContainer().get(ZUtils.key, PersistentDataType.STRING);

                    if (Guns.gunInteract(e, p, key)) // Item matchs a gun, stop code
                        return;

                    if (Dev.devToolInteract(e, block, p, key)) // Item matchs a dev-tool, stop code
                        return;

                }

            }
        }

    }

}
