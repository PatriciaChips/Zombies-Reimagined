package org.pat.zombiesReimagined.Listeners;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.pat.pattyEssentialsV3.ColoredText;
import org.pat.pattyEssentialsV3.Utils;
import org.pat.zombiesReimagined.Commands.Test;
import org.pat.zombiesReimagined.Utility.MapUtils.IndentifiedStructures;
import org.pat.zombiesReimagined.Utility.MapUtils.MapFeature;
import org.pat.zombiesReimagined.Utility.ZUtils;

import java.io.IOException;
import java.util.*;

public class Interact implements Listener {

    public static HashMap<Player, Location[]> selection = new HashMap<>();
    public static HashMap<Player, BlockDisplay> selectionDisplay = new HashMap<>();

    public static HashMap<MapFeature, Location> storedStructures = new HashMap<>();
    public static Set<Entity> structureEntities = new HashSet<>();

    @EventHandler
    public void playerInteract(PlayerInteractEvent e) throws IOException {

        Player p = e.getPlayer();
        ItemStack item = e.getItem();
        Block block = e.getClickedBlock();

        if (e.getAction().isRightClick()) {
            if (item != null) {

                if (item != null && item.hasItemMeta() && item.getItemMeta().getPersistentDataContainer().has(ZUtils.key, PersistentDataType.STRING)) {
                    String storedValue = item.getItemMeta().getPersistentDataContainer().get(ZUtils.key, PersistentDataType.STRING);

                    switch (storedValue.toLowerCase()) {
                        /** DEV-TOOLS */
                        case "selection_wand":
                            if (block != null) {
                                if (selection.containsKey(p) && selection.get(p).length == 2) {
                                    selectionDisplay.get(p).remove();
                                    selectionDisplay.remove(p);
                                    selection.remove(p);
                                    return;
                                } else {
                                    if (selection.containsKey(p) && selection.get(p).length == 1) {
                                        if (!selection.get(p)[0].equals(block.getLocation())) {
                                            selection.put(p, new Location[]{selection.get(p)[0], block.getLocation()});
                                            for (Entity entity : selection.get(p)[0].getNearbyEntitiesByType(BlockDisplay.class, 0.1, 0.1, 0.1)) {
                                                if (!entity.hasMetadata(p.getUniqueId().toString()))
                                                    continue;

                                                BlockDisplay selectionDisplay = (BlockDisplay) entity;

                                                Location topCorner = selection.get(p)[1].clone();
                                                Location bottomCorner = selection.get(p)[0].clone();

                                                double positiveAdjustment = 1.01;
                                                double negativeAdjustment = -0.01;

                                                Vector vec = topCorner.toVector().subtract(bottomCorner.toVector());

                                                if (vec.getX() >= 0 && vec.getY() >= 0 && vec.getZ() >= 0) {
                                                    topCorner.add(positiveAdjustment, positiveAdjustment, positiveAdjustment);
                                                    bottomCorner.add(negativeAdjustment, negativeAdjustment, negativeAdjustment);
                                                } else if (vec.getX() >= 0 && vec.getY() < 0 && vec.getZ() >= 0) {
                                                    topCorner.add(positiveAdjustment, negativeAdjustment, positiveAdjustment);
                                                    bottomCorner.add(negativeAdjustment, positiveAdjustment, negativeAdjustment);
                                                } else if (vec.getX() >= 0 && vec.getY() < 0 && vec.getZ() < 0) {
                                                    topCorner.add(positiveAdjustment, negativeAdjustment, negativeAdjustment);
                                                    bottomCorner.add(negativeAdjustment, positiveAdjustment, positiveAdjustment);
                                                } else if (vec.getX() >= 0 && vec.getY() >= 0 && vec.getZ() < 0) {
                                                    topCorner.add(positiveAdjustment, positiveAdjustment, negativeAdjustment);
                                                    bottomCorner.add(negativeAdjustment, negativeAdjustment, positiveAdjustment);
                                                } else if (vec.getX() < 0 && vec.getY() <= 0 && vec.getZ() < 0) {
                                                    bottomCorner.add(positiveAdjustment, positiveAdjustment, positiveAdjustment);
                                                    topCorner.add(negativeAdjustment, negativeAdjustment, negativeAdjustment);
                                                } else if (vec.getX() <= 0 && vec.getY() > 0 && vec.getZ() <= 0) {
                                                    topCorner.add(negativeAdjustment, positiveAdjustment, negativeAdjustment);
                                                    bottomCorner.add(positiveAdjustment, negativeAdjustment, positiveAdjustment);
                                                } else if (vec.getX() < 0 && vec.getY() <= 0 && vec.getZ() >= 0) {
                                                    topCorner.add(negativeAdjustment, negativeAdjustment, positiveAdjustment);
                                                    bottomCorner.add(positiveAdjustment, positiveAdjustment, negativeAdjustment);
                                                } else if (vec.getX() < 0 && vec.getY() > 0 && vec.getZ() > 0) {
                                                    topCorner.add(negativeAdjustment, positiveAdjustment, positiveAdjustment);
                                                    bottomCorner.add(positiveAdjustment, negativeAdjustment, negativeAdjustment);
                                                }

                                                //DEBUG
                                                //p.sendMessage((float) (topCorner.getX() - bottomCorner.getX()) + " | " + (float) (topCorner.getY() - bottomCorner.getY()) + " | " + (float) (topCorner.getZ() - bottomCorner.getZ()));

                                                if (!(((float) (topCorner.getX() - bottomCorner.getX()) > 0 && (float) (topCorner.getY() - bottomCorner.getY()) < 0 && (float) (topCorner.getZ() - bottomCorner.getZ()) > 0)
                                                        || ((float) (topCorner.getX() - bottomCorner.getX()) < 0 && (float) (topCorner.getY() - bottomCorner.getY()) < 0 && (float) (topCorner.getZ() - bottomCorner.getZ()) < 0)
                                                        || ((float) (topCorner.getX() - bottomCorner.getX()) > 0 && (float) (topCorner.getY() - bottomCorner.getY()) > 0 && (float) (topCorner.getZ() - bottomCorner.getZ()) < 0))) {
                                                    Location tempLoc = topCorner.clone();
                                                    topCorner = bottomCorner;
                                                    bottomCorner = tempLoc;
                                                }

                                                selectionDisplay.setTransformationMatrix(new Matrix4f()
                                                        .scale((float) (topCorner.getX() - bottomCorner.getX()), (float) (topCorner.getY() - bottomCorner.getY()), (float) (topCorner.getZ() - bottomCorner.getZ())));
                                                selectionDisplay.teleport(bottomCorner);
                                                break;
                                            }
                                            return;
                                        } else {
                                            return;
                                        }
                                    } else {
                                        selection.put(p, new Location[]{block.getLocation()});
                                    }
                                }
                                for (Location loc : selection.get(p)) {
                                    BlockDisplay blockDisplay = (BlockDisplay) p.getWorld().spawn(loc.clone().add(0.005, 0.005, 0.005), BlockDisplay.class);
                                    blockDisplay.setBlock(Material.WHITE_STAINED_GLASS.createBlockData());
                                    blockDisplay.setMetadata(p.getUniqueId().toString(), new FixedMetadataValue(ZUtils.plugin, p.getUniqueId().toString()));
                                    blockDisplay.setGlowing(true);
                                    blockDisplay.setTransformationMatrix(new Matrix4f().scaleLocal(0.99F));
                                    selectionDisplay.put(p, blockDisplay);
                                }
                            }
                            break;
                        case "execution_wand":
                            if (storedStructures.size() == 0) {
                                if (selection.containsKey(p) && selection.get(p).length == 2) {
                                    p.sendMessage(ColoredText.t("&7&oExecuted a feature detection.."));

                                    Location corner1 = selection.get(p)[0];
                                    Location corner2 = selection.get(p)[1];

                                    World world = corner1.getWorld();

                                    int minX = Math.min(corner1.getBlockX(), corner2.getBlockX());
                                    int maxX = Math.max(corner1.getBlockX(), corner2.getBlockX());
                                    int minY = Math.min(corner1.getBlockY(), corner2.getBlockY());
                                    int maxY = Math.max(corner1.getBlockY(), corner2.getBlockY());
                                    int minZ = Math.min(corner1.getBlockZ(), corner2.getBlockZ());
                                    int maxZ = Math.max(corner1.getBlockZ(), corner2.getBlockZ());

                                    Set<Block> checkedBlocks = new HashSet<>();

                                    for (int x = minX; x <= maxX; x++) {
                                        for (int y = minY; y <= maxY; y++) {
                                            for (int z = minZ; z <= maxZ; z++) {
                                                //world.getBlockAt(x, y, z).setType(Material.AIR);
                                                Location loc = new Location(p.getWorld(), x, y, z);
                                                if (loc.getBlock().getType() == Material.BEDROCK) {
                                                    if (!checkedBlocks.contains(loc.getBlock())) {
                                                        for (BlockFace face : new BlockFace[]{BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST}) {
                                                            Location relativeLoc = loc.getBlock().getRelative(face).getLocation();
                                                            if (relativeLoc.clone().add(0, 1, 0).getBlock().getType() == Material.BEDROCK) {
                                                                if (relativeLoc.getBlock().getRelative(face).getLocation().getBlock().getType() == Material.BEDROCK) { /** UNLOCK-DOOR */
                                                                    Location centerLoc = relativeLoc.clone().add(0, 1, 0);
                                                                    Location directionLoc = null;
                                                                    if (loc.clone().add(0, -1, 0).getBlock().getType() == Material.BEDROCK) {
                                                                        directionLoc = loc.clone().add(0, -1, 0);
                                                                    } else if (relativeLoc.getBlock().getRelative(face).getLocation().clone().add(0, -1, 0).getBlock().getType() == Material.BEDROCK) {
                                                                        directionLoc = relativeLoc.getBlock().getRelative(face).getLocation().clone().add(0, -1, 0);
                                                                    }
                                                                    if (directionLoc != null) {
                                                                        checkedBlocks.add(centerLoc.getBlock());
                                                                        checkedBlocks.add(relativeLoc.getBlock().getRelative(face).getLocation().getBlock());
                                                                        checkedBlocks.add(loc.getBlock());
                                                                        checkedBlocks.add(directionLoc.getBlock());
                                                                        centerLoc = adjustLocationDirections(centerLoc, directionLoc);
                                                                        if (IndentifiedStructures.unlockDoorIndentifiers.containsKey(centerLoc.clone().add(0, -1, 0).getBlock().getType())) { //UNLOCK_DOOR indentified
                                                                            storedStructures.put(IndentifiedStructures.unlockDoorIndentifiers.get(centerLoc.clone().add(0, -1, 0).getBlock().getType()).clone(), centerLoc.add(0.5, -1, 0.5));
                                                                            break; //exit blockface check
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
                                for (var featureMap : storedStructures.entrySet()) {
                                    MapFeature feature = featureMap.getKey();
                                    Location loc = featureMap.getValue().clone();

                                    switch (feature.getType()) {
                                        case UNLOCK_DOOR:
                                            loc.add(0, 3.3, 0);

                                            TextDisplay nameText = loc.getWorld().spawn(loc.clone().add(loc.getDirection().multiply(0.501)), TextDisplay.class);
                                            nameText.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
                                            nameText.setShadowed(true);
                                            nameText.setBillboard(Display.Billboard.FIXED);
                                            nameText.setText(feature.getName());
                                            nameText.setAlignment(TextDisplay.TextAlignment.CENTER);
                                            nameText.setTransformationMatrix(new Matrix4f().scaleLocal(2.5F, 2.5F, 2.5F));

                                            TextDisplay nameText1 = loc.getWorld().spawn(loc.clone().add(loc.getDirection().multiply(-0.501)), TextDisplay.class);
                                            nameText1.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
                                            nameText1.setShadowed(true);
                                            nameText1.setBillboard(Display.Billboard.FIXED);
                                            nameText1.setText(feature.getName1());
                                            nameText1.setAlignment(TextDisplay.TextAlignment.CENTER);
                                            nameText1.setTransformationMatrix(new Matrix4f().scaleLocal(-2.5F, 2.5F, -2.5F));  // Flip horizontally

                                            loc.add(0, -0.2, 0);
                                            String underText = ColoredText.t("&7Click to purchase!");

                                            TextDisplay nameUnderText = loc.getWorld().spawn(loc.clone().add(loc.getDirection().multiply(0.501)), TextDisplay.class);
                                            nameUnderText.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
                                            nameUnderText.setShadowed(true);
                                            nameUnderText.setBillboard(Display.Billboard.FIXED);
                                            nameUnderText.setText(underText);
                                            nameUnderText.setAlignment(TextDisplay.TextAlignment.CENTER);
                                            nameUnderText.setTransformationMatrix(new Matrix4f().scaleLocal(1F));

                                            TextDisplay nameUnderText1 = loc.getWorld().spawn(loc.clone().add(loc.getDirection().multiply(-0.501)), TextDisplay.class);
                                            nameUnderText1.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
                                            nameUnderText1.setShadowed(true);
                                            nameUnderText1.setBillboard(Display.Billboard.FIXED);
                                            nameUnderText1.setText(underText);
                                            nameUnderText1.setAlignment(TextDisplay.TextAlignment.CENTER);
                                            nameUnderText1.setTransformationMatrix(new Matrix4f().scaleLocal(-1F, 1F, -1F));  // Flip horizontally

                                            loc.add(0, -0.5, 0);

                                            TextDisplay costText = loc.getWorld().spawn(loc.clone().add(loc.getDirection().multiply(0.501)), TextDisplay.class);
                                            costText.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
                                            costText.setShadowed(true);
                                            costText.setBillboard(Display.Billboard.FIXED);
                                            costText.setText(ColoredText.t("&#29ab34$" + feature.getCost()));
                                            costText.setAlignment(TextDisplay.TextAlignment.CENTER);
                                            costText.setTransformationMatrix(new Matrix4f().scaleLocal(1.3F));

                                            TextDisplay costText1 = loc.getWorld().spawn(loc.clone().add(loc.getDirection().multiply(-0.501)), TextDisplay.class);
                                            costText1.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
                                            costText1.setShadowed(true);
                                            costText1.setBillboard(Display.Billboard.FIXED);
                                            costText1.setText(ColoredText.t("&#29ab34$" + feature.getCost1()));
                                            costText1.setAlignment(TextDisplay.TextAlignment.CENTER);
                                            costText1.setTransformationMatrix(new Matrix4f().scaleLocal(-1.3F, 1.3F, -1.3F));  // Flip horizontally

                                            structureEntities.add(nameText);
                                            structureEntities.add(nameText1);
                                            structureEntities.add(nameUnderText);
                                            structureEntities.add(nameUnderText1);
                                            structureEntities.add(costText);
                                            structureEntities.add(costText1);
                                            break;
                                    }
                                }
                                p.sendMessage(ColoredText.t("&7&oExecuted a feature iteration.."));
                            }
                            break;
                        case "reset_wand":
                            for (Entity entity : structureEntities) {
                                entity.remove();
                            }
                            storedStructures.clear();
                            structureEntities.clear();
                            p.sendMessage(ColoredText.t("&7&oCleared stored structures map.."));
                            break;
                        /** GUNS */
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

    public static Location adjustLocationDirections(Location centerLoc, Location directionLoc) {
        Vector direction = centerLoc.toVector().subtract(directionLoc.toVector());
        direction.setY(0);
        direction = direction.normalize().multiply(-1);

        double yaw = Math.atan2(-direction.getX(), direction.getZ()) * (180 / Math.PI);
        double pitch = Math.asin(-direction.getY() / direction.length()) * (180 / Math.PI);

        // Normalize the yaw to be within the range of -180 to 180
        if (yaw > 180) {
            yaw -= 360;  // This ensures the yaw stays within the desired range
        } else if (yaw < -180) {
            yaw += 360;  // This ensures the yaw stays within the desired range
        }

        centerLoc.setPitch((float) pitch);
        centerLoc.setYaw((float) yaw);
        centerLoc.setDirection(direction);

        // Output the results
        return centerLoc;
    }

}
