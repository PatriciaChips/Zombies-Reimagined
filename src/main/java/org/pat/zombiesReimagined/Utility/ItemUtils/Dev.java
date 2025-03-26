package org.pat.zombiesReimagined.Utility.ItemUtils;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Slab;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.joml.Matrix4f;
import org.pat.pattyEssentialsV3.ColoredText;
import org.pat.zombiesReimagined.Utility.MapUtils.FeatureType;
import org.pat.zombiesReimagined.Utility.MapUtils.IdentifiedStructures;
import org.pat.zombiesReimagined.Utility.MapUtils.MapFeature;
import org.pat.zombiesReimagined.Utility.MapUtils.StructUtils.Selection;
import org.pat.zombiesReimagined.Utility.MapUtils.StructUtils.SpawnStructure;
import org.pat.zombiesReimagined.Utility.ZUtils;

import java.util.*;

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
                                                                break; // exit blockface check
                                                            }
                                                        }
                                                    } else { /** WINDOW || POWERUP */
                                                        Location centerLoc1 = loc.clone();
                                                        Location upLoc = relativeLoc.clone().add(0, 1, 0);
                                                        checkedBlocks.add(upLoc.getBlock());
                                                        checkedBlocks.add(centerLoc1.getBlock());
                                                        centerLoc1 = adjustLocationDirections(centerLoc1, loc.getBlock().getRelative(face).getRelative(face).getLocation());
                                                        Location rightLoc = centerLoc1.clone();
                                                        rightLoc.setYaw(rightLoc.getYaw() + 90);
                                                        Location leftLoc = centerLoc1.clone();
                                                        leftLoc.setYaw(leftLoc.getYaw() - 90);
                                                        if (IdentifiedStructures.powerupIndentifiers.containsKey(upLoc.clone().add(0, -1, 0).getBlock().getType())) { /** POWERUP indentified */
                                                            MapFeature mapFeature = IdentifiedStructures.powerupIndentifiers.get(upLoc.clone().add(0, -1, 0).getBlock().getType()).clone();
                                                            mapFeature.setExtraBlocks(getBlocksInArea(centerLoc1.clone().add(0, 1, 0).add(rightLoc.getDirection().normalize()), centerLoc1.getBlock().getRelative(face).getRelative(face).getLocation().add(0, 3, 0).add(leftLoc.getDirection().normalize())));
                                                            //centerLoc1.clone().add(0, 1, 0).add(rightLoc.getDirection().normalize()).getBlock().setType(centerLoc1.getBlock().getRelative(face).getType());
                                                            //centerLoc1.getBlock().getRelative(face).getRelative(face).getLocation().add(0, 3, 0).add(leftLoc.getDirection().normalize()).getBlock().setType(centerLoc1.getBlock().getRelative(face).getType());
                                                            MapFeature.storedStructures.put(mapFeature, centerLoc1.clone().add(0.5, 0, 0.5));
                                                            break; // exit blockface check
                                                        } else { /** WINDOW*/
                                                            MapFeature mapFeature = new MapFeature(FeatureType.WINDOW);
                                                            for (Block block1 : getBlocksInArea(centerLoc1.clone().getBlock().getRelative(face).getLocation().add(0, 1, 0).add(rightLoc.getDirection().normalize()), centerLoc1.getBlock().getRelative(face).getRelative(face).getRelative(face).getRelative(face).getRelative(face).getRelative(face).getRelative(face).getLocation().add(0, 1, 0).add(leftLoc.getDirection().normalize()))) {
                                                                if (block1.getType() == Material.BLACK_CONCRETE_POWDER) {
                                                                    mapFeature.setLoc(block1.getLocation().add(0.5, 1, 0.5)); // Mob spawn location
                                                                    break;
                                                                }
                                                            }
                                                            List<Block> barricadeBlocks = getBlocksInArea(centerLoc1.clone().getBlock().getRelative(face).getLocation().add(0, 2, 0).add(rightLoc.getDirection().normalize()), centerLoc1.getBlock().getRelative(face).getLocation().add(0, 3, 0).add(leftLoc.getDirection().normalize()));
                                                            mapFeature.setExtraBlocks(barricadeBlocks);
                                                            mapFeature.setMaterial(barricadeBlocks.getFirst().getType());
                                                            MapFeature.storedStructures.put(mapFeature, centerLoc1.clone().add(0.5, 0, 0.5));
                                                            Location tLoc = centerLoc1.clone();
                                                            Random random = new Random();
                                                            new BukkitRunnable() {
                                                                public void run() {

                                                                    if (!MapFeature.storedStructures.containsKey(mapFeature))
                                                                        cancel();

                                                                    boolean underAttack = false;
                                                                    for (Entity entity : getEntities(tLoc.clone().add(0, 1, 0).getBlock().getRelative(face).getRelative(face).getLocation().add(leftLoc.getDirection()), tLoc.clone().add(0, 1, 0).getBlock().getRelative(face).getRelative(face).getLocation().add(0, 3, 0).add(rightLoc.getDirection()))) { // Player Shift AREA
                                                                        if (!(entity instanceof Player)) {
                                                                            boolean containsType = false;
                                                                            for (Block block2 : mapFeature.getExtraBlocks()) {
                                                                                Block block1 = block2.getLocation().getBlock();
                                                                                if (block1.getType() == mapFeature.getMaterial()) {
                                                                                    containsType = true;
                                                                                    break;
                                                                                }
                                                                            }
                                                                            if (containsType) {
                                                                                Block randomBlock;
                                                                                do {
                                                                                    int randomIndex = random.nextInt(mapFeature.getExtraBlocks().size()); // Get a random index within the list size
                                                                                    randomBlock = mapFeature.getExtraBlocks().get(randomIndex).getLocation().getBlock(); // Get the material at the random index
                                                                                } while (!randomBlock.getType().equals(mapFeature.getMaterial()));
                                                                                randomBlock.setType(Material.AIR);
                                                                                block.getLocation().getWorld().playSound(block.getLocation().add(0.5, 0.5, 0.5), Sound.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR, 0.5F, 1);
                                                                            }
                                                                            break;
                                                                        }
                                                                    }
                                                                    if (!underAttack) {
                                                                        for (Entity entity : getEntities(tLoc.clone().add(leftLoc.getDirection()), tLoc.clone().add(0, 3, 0).add(rightLoc.getDirection()))) { // Player Shift AREA
                                                                            if (entity instanceof Player p) {
                                                                                if (p.isSneaking()) {
                                                                                    boolean containsType = false;
                                                                                    for (Block block2 : mapFeature.getExtraBlocks()) {
                                                                                        Block block1 = block2.getLocation().getBlock();
                                                                                        if (block1.getType() == Material.AIR) {
                                                                                            containsType = true;
                                                                                            break;
                                                                                        }
                                                                                    }
                                                                                    if (containsType) {
                                                                                        Block randomBlock;
                                                                                        do {
                                                                                            int randomIndex = random.nextInt(mapFeature.getExtraBlocks().size()); // Get a random index within the list size
                                                                                            randomBlock = mapFeature.getExtraBlocks().get(randomIndex).getLocation().getBlock(); // Get the material at the random index
                                                                                        } while (!randomBlock.getType().equals(Material.AIR));
                                                                                        randomBlock.setType(mapFeature.getMaterial());
                                                                                        Slab slab = (Slab) randomBlock.getBlockData();
                                                                                        slab.setType(Slab.Type.TOP);
                                                                                        randomBlock.setBlockData(slab);
                                                                                        block.getLocation().getWorld().playSound(block.getLocation().add(0.5, 0.5, 0.5), Sound.BLOCK_STONE_PLACE, 0.5F, 0.8F);
                                                                                    }
                                                                                }
                                                                                break;
                                                                            }
                                                                        }
                                                                    }
                                                                    mapFeature.fillBlankWindowBlocks();
                                                                }
                                                            }.runTaskTimer(ZUtils.plugin, 0L, 25L);
                                                            break; // exit blockface check
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
                                                            break; // exit blockface check
                                                        } else if (IdentifiedStructures.armourIndentifiers.containsKey(centerLoc.clone().add(0, -1, 0).getBlock().getType())) { //ARMOUR indentified
                                                            MapFeature mapFeature = IdentifiedStructures.armourIndentifiers.get(centerLoc.clone().add(0, -1, 0).getBlock().getType()).clone();
                                                            mapFeature.setExtraBlocks(getBlocksInArea(loc1, loc2));
                                                            MapFeature.storedStructures.put(mapFeature, centerLoc.add(0.5, -1, 0.5));
                                                            break; // exit blockface check
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

    public static List<Entity> getEntities(Location loc, Location loc1) {
        List<Entity> entitiesInRegion = new ArrayList<>();

        loc = loc.clone();
        loc1 = loc1.clone();

        // Get the world from one of the locations (both should be in the same world)
        World world = loc.getWorld();
        if (world == null) {
            return entitiesInRegion; // If the world is null, return an empty list
        }

        // Calculate the center point between the two locations
        double centerX = (loc.getX() + loc1.getX()) / 2;
        double centerY = (loc.getY() + loc1.getY()) / 2;
        double centerZ = (loc.getZ() + loc1.getZ()) / 2;
        Location centerLocation = new Location(world, centerX, centerY, centerZ);

        // Calculate the radius (distance to a corner of the bounding box)
        double radius = Math.max(
                Math.max(Math.abs(loc.getX() - loc1.getX()), Math.abs(loc.getY() - loc1.getY())),
                Math.abs(loc.getZ() - loc1.getZ())
        );

        // Get all nearby living entities within the calculated radius
        for (Entity entity : centerLocation.getNearbyLivingEntities(radius, radius, radius)) {
            entitiesInRegion.add(entity);
        }

        return entitiesInRegion;
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
