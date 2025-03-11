package org.pat.zombiesReimagined.Utility.ItemUtils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;
import org.joml.Matrix4f;
import org.pat.pattyEssentialsV3.ColoredText;
import org.pat.zombiesReimagined.Utility.MapUtils.FeatureType;
import org.pat.zombiesReimagined.Utility.MapUtils.IdentifiedStructures;
import org.pat.zombiesReimagined.Utility.MapUtils.MapFeature;
import org.pat.zombiesReimagined.Utility.MapUtils.StructUtils.Selection;
import org.pat.zombiesReimagined.Utility.MapUtils.StructUtils.SpawnStructure;
import org.pat.zombiesReimagined.Utility.ZUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Dev {

    public static boolean devToolInteract(PlayerInteractEvent e, Block block, Player p, String key) {
        /** DEV-TOOLS */
        switch (key) {
            case "selection_wand":
                if (block != null) {
                    Selection.createSelection(p, block);
                }
                break;
            case "execution_wand":
                if (MapFeature.storedStructures.size() == 0) {
                    if (Selection.selection.containsKey(p) && Selection.selection.get(p).length == 2) {
                        p.sendMessage(ColoredText.t("&7&oExecuted a feature detection.."));

                        Location corner1 = Selection.selection.get(p)[0];
                        Location corner2 = Selection.selection.get(p)[1];

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
                                                            if (IdentifiedStructures.unlockDoorIndentifiers.containsKey(centerLoc.clone().add(0, -2, 0).getBlock().getType())) { //UNLOCK_DOOR indentified
                                                                MapFeature mapFeature = IdentifiedStructures.unlockDoorIndentifiers.get(centerLoc.clone().add(0, -2, 0).getBlock().getType()).clone();
                                                                Boolean directionCheck = centerLoc.getYaw() == 90.0F || centerLoc.getYaw() == 270.0F;
                                                                Location loc1 = centerLoc.clone().add(new Vector((!directionCheck) ? 2 : 0, 0, (!directionCheck) ? 0 : 2));
                                                                Location loc2 = centerLoc.clone().add(new Vector((!directionCheck) ? -2 : 0, 3, (!directionCheck) ? 0 : -2));
                                                                mapFeature.setExtraBlocks(getBlocksInArea(loc1, loc2));
                                                                MapFeature.storedStructures.put(mapFeature, centerLoc.add(0.5, -1, 0.5));
                                                                break; //exit blockface check
                                                            }
                                                        }
                                                    }
                                                } else if (relativeLoc.clone().add(0, 2, 0).getBlock().getType() == Material.BEDROCK) { /** GUN || ARMOUR */
                                                    Location centerLoc = relativeLoc.clone().add(0, 1, 0);
                                                    Location directionLoc = relativeLoc.clone().add(0, 2, 0);
                                                    if (directionLoc != null) {
                                                        checkedBlocks.add(loc.getBlock());
                                                        checkedBlocks.add(directionLoc.getBlock());
                                                        centerLoc = adjustLocationDirections(centerLoc, loc.getBlock().getRelative(face).getRelative(face).getLocation());
                                                        Boolean directionCheck = centerLoc.getYaw() == 90.0F || centerLoc.getYaw() == 270.0F;
                                                        Location loc1 = centerLoc.clone().add(new Vector((!directionCheck) ? 1 : 0, 0, (!directionCheck) ? 0 : 1));
                                                        Location loc2 = centerLoc.clone().add(new Vector((!directionCheck) ? -1 : 0, 3, (!directionCheck) ? 0 : -1));
                                                        if (IdentifiedStructures.gunIdentifiers.containsKey(centerLoc.clone().add(0, -1, 0).getBlock().getType())) { //GUN indentified
                                                            MapFeature mapFeature = IdentifiedStructures.gunIdentifiers.get(centerLoc.clone().add(0, -1, 0).getBlock().getType()).clone();
                                                            mapFeature.setExtraBlocks(getBlocksInArea(loc1, loc2));
                                                            MapFeature.storedStructures.put(mapFeature, centerLoc.add(0.5, -1, 0.5));
                                                            break; //exit blockface check
                                                        } else if (IdentifiedStructures.armourIndentifiers.containsKey(centerLoc.clone().add(0, -1, 0).getBlock().getType())) { //ARMOUR indentified
                                                            MapFeature mapFeature = IdentifiedStructures.armourIndentifiers.get(centerLoc.clone().add(0, -1, 0).getBlock().getType()).clone();
                                                            mapFeature.setExtraBlocks(getBlocksInArea(loc1, loc2));
                                                            MapFeature.storedStructures.put(mapFeature, centerLoc.add(0.5, -1, 0.5));
                                                            break; //exit blockface check
                                                        }
                                                    }
                                                } else if (relativeLoc.clone().getBlock().getType() == Material.BEDROCK) { /** LUCKY CHEST */
                                                    for (BlockFace face1 : new BlockFace[]{BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST}) {
                                                        Location chestLoc1 = relativeLoc.getBlock().getRelative(face1).getLocation().clone().add(0, 1, 0);
                                                        Location chestLoc2 = loc.getBlock().getRelative(face1).getLocation().clone().add(0, 1, 0);
                                                        Location centerLoc = loc.clone();
                                                        if (chestLoc1.getBlock().getType() == Material.TRAPPED_CHEST) {
                                                            centerLoc = adjustLocationDirections(centerLoc, loc.getBlock().getRelative(face1).getRelative(face1).getLocation());
                                                            checkedBlocks.add(loc.getBlock());
                                                            checkedBlocks.add(relativeLoc.getBlock());
                                                            MapFeature mapFeature = new MapFeature(FeatureType.CHEST, 1000);
                                                            mapFeature.setExtraBlocks(List.of(new Block[]{chestLoc1.getBlock(), chestLoc2.getBlock()}));
                                                            Boolean directionCheck = centerLoc.getYaw() == 90.0F || centerLoc.getYaw() == 270.0F;
                                                            MapFeature.storedStructures.put(mapFeature, centerLoc.add(directionCheck ? 0.5 : 1, 0, directionCheck ? 1 : 0.5));
                                                            break;
                                                        }
                                                    }
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    if (MapFeature.iteratedStructures.contains(p)) //ALREADY SPAWNED ENTITIES
                        break;
                    MapFeature.iteratedStructures.add(p);

                    SpawnStructure.spawnStructures(e, p);

                    p.sendMessage(ColoredText.t("&7&oExecuted a feature iteration.."));
                }
                break;
            case "reset_wand":
                SpawnStructure.reset(p);
                break;
            default:
                return false;
        }

        return true;

    }

    public static Location adjustLocationDirections(Location centerLoc, Location directionLoc) {
        Vector direction = centerLoc.toVector().subtract(directionLoc.toVector());
        direction.setY(0);
        direction = direction.normalize().multiply(-1);

        double yaw = Math.atan2(-direction.getX(), direction.getZ()) * (180 / Math.PI);
        double pitch = Math.asin(-direction.getY() / direction.length()) * (180 / Math.PI);

        if (yaw > 180) {
            yaw -= 360;
        } else if (yaw < -180) {
            yaw += 360;
        }

        centerLoc.setPitch((float) pitch);
        centerLoc.setYaw((float) yaw);
        centerLoc.setDirection(direction);

        return centerLoc;
    }

    public static List<Block> getBlocksInArea(Location loc1, Location loc2) {
        List<Block> blocks = new ArrayList<>();

        // Get the minimum and maximum coordinates for each axis
        int minX = Math.min(loc1.getBlockX(), loc2.getBlockX());
        int maxX = Math.max(loc1.getBlockX(), loc2.getBlockX());
        int minY = Math.min(loc1.getBlockY(), loc2.getBlockY());
        int maxY = Math.max(loc1.getBlockY(), loc2.getBlockY());
        int minZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
        int maxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());

        // Loop through all the blocks in the defined area
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    // Get the block at the current coordinates
                    Location blockLocation = new Location(loc1.getWorld(), x, y, z);
                    Block block = blockLocation.getBlock();
                    blocks.add(block);
                }
            }
        }

        return blocks;
    }

}
