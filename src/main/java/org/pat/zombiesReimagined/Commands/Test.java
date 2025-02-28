package org.pat.zombiesReimagined.Commands;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.AxisAngle4f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.pat.pattyEssentialsV3.Utils;
import org.pat.zombiesReimagined.Utility.ZUtils;

import java.util.*;

public class Test implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String cmd, @NotNull String[] args) {

        if (sender instanceof Player p) {

            TextDisplay textDisplayFront = p.getWorld().spawn(p.getLocation(), TextDisplay.class);
            textDisplayFront.setBackgroundColor(Color.fromARGB(50, 50, 250, 50));
            textDisplayFront.setSeeThrough(true);
            textDisplayFront.setText("wake up babe, text displays just dropped.");
            textDisplayFront.setBrightness(new Display.Brightness(5, 5));
            textDisplayFront.setBillboard(Display.Billboard.FIXED);
            textDisplayFront.setTransformationMatrix(new Matrix4f().scaleLocal(1));
            textDisplayFront.setInterpolationDuration(200);
            textDisplayFront.setInterpolationDelay(1);

// Create a flipped version of the text display for the back side
            TextDisplay textDisplayBack = p.getWorld().spawn(p.getLocation(), TextDisplay.class);
            textDisplayBack.setBackgroundColor(Color.fromARGB(50, 50, 250, 50));
            textDisplayBack.setSeeThrough(true);
            textDisplayBack.setText(" ");
            textDisplayBack.setBrightness(new Display.Brightness(5, 5));
            textDisplayBack.setBillboard(Display.Billboard.FIXED);
            textDisplayBack.setTransformationMatrix(new Matrix4f().scaleLocal(-1, 1, 1));  // Flip horizontally
            textDisplayBack.setInterpolationDuration(200);
            textDisplayBack.setInterpolationDelay(1);

        }

        return false;
    }

    public static void shootGun(Player p) {
        float rF = 0.5f + (0.75f - 0.5f) * new Random().nextFloat();
        Location loc = p.getEyeLocation().clone().add(p.getEyeLocation().getDirection().multiply(rF));
        Location loc1 = p.getEyeLocation().clone().add(p.getEyeLocation().getDirection().multiply(rF + 0.01));
        /**
         if (new Random().nextInt(5) == 0) {
         loc.setPitch(loc.getPitch() - 5 + new Random().nextInt(11));
         loc.setYaw(loc.getYaw() - 5 + new Random().nextInt(11));
         } else {
         loc.setPitch(loc.getPitch() - 2 + new Random().nextInt(5));
         loc.setYaw(loc.getYaw() - 2 + new Random().nextInt(5));
         }
         */

        BlockDisplay display = (BlockDisplay) p.getWorld().spawnEntity(loc, EntityType.BLOCK_DISPLAY);
        BlockDisplay display1 = (BlockDisplay) p.getWorld().spawnEntity(loc1, EntityType.BLOCK_DISPLAY);

        display.setBlock(Material.GRAY_STAINED_GLASS.createBlockData());
        display1.setBlock(Material.BLACK_STAINED_GLASS.createBlockData());
        display.setViewRange(256);
        display1.setViewRange(256);
        display.setBillboard(Display.Billboard.FIXED);
        display1.setBillboard(Display.Billboard.FIXED);
        display.setBrightness(new Display.Brightness(15, 15));
        display1.setBrightness(new Display.Brightness(15, 15));

        //display.setTransformationMatrix(new Matrix4f().scale(0.1F, 0.1F, 0.1F).translate(-.5F, -.5F, 0).translate(0, 0, 0.1F));

        display.setInterpolationDuration(10); // 20 ticks = 1 second
        display1.setInterpolationDuration(2); // 20 ticks = 1 second
        display.setInterpolationDelay(1);  // 40

        float randomValue = 0.05f + (0.09f - 0.05f) * new Random().nextFloat();
        float randomRotation = new Random().nextInt(361);

        AxisAngle4f angle = new AxisAngle4f(new Random().nextInt(361), 0, 0, 1);

        Location hitLocation = null;

        Object[] variables = getLookingAtBlockSpot(loc);
        double distance = (double) (variables.length > 0 ? variables[0] : null);
        hitLocation = (Location) (variables.length > 1 ? variables[1] : null);

        display.setTransformationMatrix(new Matrix4f().scale(randomValue, randomValue, (float) distance).transpose().translate(-0.5F + (randomValue / 4), -0.5F + (randomValue / 4), 0).rotateLocal(randomRotation, 0, 0, 1));
        display1.setTransformationMatrix(new Matrix4f().scale(randomValue / 2, randomValue / 2, (float) distance).transpose().translate(-0.5F + (randomValue / 4), -0.5F + (randomValue / 4), 0));
        //if (e.getValue() != null)
        //p.getWorld().createExplosion(e.getValue(), Double.valueOf(args[0]).floatValue());

        Utils.scheduler.runTaskLater(Utils.plugin, () -> {
            display.setTransformationMatrix(new Matrix4f().scale(randomValue+0.03F, randomValue+0.03F, (float) distance).transpose().translate(-0.5F + (randomValue / 4), -0.5F + (randomValue / 4), 0).rotateLocal(randomRotation, 0, 0, 1));
        }, 2);

        BukkitTask task = null;

        if (variables.length == 3) {
            LivingEntity entity = (LivingEntity) variables[2];
            if (p != entity) {
                entity.damage(0);
                entity.setHealth(entity.getHealth() > 5 ? entity.getHealth() - 5 : 0);
                p.getWorld().spawnParticle(Particle.BLOCK, hitLocation, 10, 0.2, 0.2, 0.2, Material.REDSTONE_BLOCK.createBlockData());
            }
        }

        if (hitLocation != null && variables.length == 2) {
            double distanceNorth = getDistanceToFace(hitLocation.clone(), hitLocation.getBlock().getLocation().clone().add(0.5, 0.5, 0.5).add(0, 0, -0.5)); // North face
            double distanceSouth = getDistanceToFace(hitLocation.clone(), hitLocation.getBlock().getLocation().clone().add(0.5, 0.5, 0.5).add(0, 0, 0.5)); // South face
            double distanceWest = getDistanceToFace(hitLocation.clone(), hitLocation.getBlock().getLocation().clone().add(0.5, 0.5, 0.5).add(-0.5, 0, 0)); // West face
            double distanceEast = getDistanceToFace(hitLocation.clone(), hitLocation.getBlock().getLocation().clone().add(0.5, 0.5, 0.5).add(0.5, 0, 0)); // East face
            double distanceUp = getDistanceToFace(hitLocation.clone(), hitLocation.getBlock().getLocation().clone().add(0.5, 0.5, 0.5).add(0, 0.5, 0)); // Up face
            double distanceDown = getDistanceToFace(hitLocation.clone(), hitLocation.getBlock().getLocation().clone().add(0.5, 0.5, 0.5).add(0, -0.5, 0)); // Down face

            // Find the face with the smallest distance
            double minDistance = Math.min(Math.min(Math.min(distanceNorth, distanceSouth), Math.min(distanceWest, distanceEast)), Math.min(distanceUp, distanceDown));

            Location faceLocation = null;
            float pitch = 0;
            float yaw = 0;

            // Return the corresponding face based on the smallest distance
            if (minDistance == distanceNorth) {
                faceLocation = hitLocation.clone().add(0, 0, -0.5);
                yaw = -180;
                pitch = 0;
            } else if (minDistance == distanceSouth) {
                faceLocation = hitLocation.clone().add(0, 0, 0.5);
                yaw = 0;
                pitch = 0;
            } else if (minDistance == distanceWest) {
                faceLocation = hitLocation.clone().add(-0.5, 0, 0);
                yaw = 90;
                pitch = 0;
            } else if (minDistance == distanceEast) {
                faceLocation = hitLocation.clone().add(0.5, 0, 0);
                yaw = -90;
                pitch = 0;
            } else if (minDistance == distanceUp) {
                faceLocation = hitLocation.clone().add(0, 0.5, 0);
                yaw = 0;
                pitch = -90;
            } else {
                faceLocation = hitLocation.clone().add(0, -0.5, 0);
                yaw = 0;
                pitch = 90;
            }

            Vector holeVec = hitLocation.clone().getBlock().getLocation().add(0.5, 0.5, 0.5).toVector().subtract(faceLocation.toVector()).normalize().multiply(-1);

            holeVec = holeVec.multiply(0.01);

            Location holeLocation = hitLocation.clone().add(holeVec);
            holeLocation.setPitch(pitch);
            holeLocation.setYaw(yaw);
            //holeLocation.setDirection(holeVec);

            p.getWorld().spawnParticle(Particle.BLOCK, holeLocation, 5, 0.1, 0.1, 0.1, hitLocation.getBlock().getType().createBlockData());

            BlockDisplay holedisplay = (BlockDisplay) p.getWorld().spawnEntity(holeLocation, EntityType.BLOCK_DISPLAY);
            BlockDisplay holedisplay1 = (BlockDisplay) p.getWorld().spawnEntity(holeLocation.clone().add(holeVec.clone().multiply(0.99)), EntityType.BLOCK_DISPLAY);
            BlockDisplay holeDisplayPuff = (BlockDisplay) p.getWorld().spawnEntity(holeLocation, EntityType.BLOCK_DISPLAY);

            holedisplay.setBlock(Material.GRAY_STAINED_GLASS.createBlockData());
            holedisplay1.setBlock(Material.BLACK_STAINED_GLASS.createBlockData());
            holedisplay.setViewRange(256);
            holedisplay1.setViewRange(256);
            holedisplay.setBillboard(Display.Billboard.FIXED);
            holedisplay1.setBillboard(Display.Billboard.FIXED);
            holedisplay.setBrightness(new Display.Brightness(15, 15));
            holedisplay1.setBrightness(new Display.Brightness(15, 15));
            holeDisplayPuff.setBrightness(new Display.Brightness(0, 0));

            int randomPuffRotation = new Random().nextInt(361);
            holedisplay.setTransformationMatrix(new Matrix4f().scale(0.25F, 0.25F, 0.01F).transpose().translate(-0.5F, -0.5F, 0).rotateLocal(randomPuffRotation, 0, 0, 1));
            holeDisplayPuff.setTransformationMatrix(new Matrix4f().scale(0.25F, 0.25F, 0.01F).transpose().translate(-0.5F, -0.5F, 0));
            holedisplay1.setTransformationMatrix(new Matrix4f().scale(0.1F, 0.1F, 0.01F).transpose().translate(-0.5F, -0.5F, 0).rotateLocal(randomPuffRotation, 0, 0, 1));

            holeDisplayPuff.setBlock(Material.GRAY_STAINED_GLASS.createBlockData());

            holeDisplayPuff.setInterpolationDuration(5); // 20 ticks = 1 second
            holeDisplayPuff.setInterpolationDelay(1);  // 40

            Utils.scheduler.runTaskLater(Utils.plugin, () -> {
                holeDisplayPuff.setTransformationMatrix(new Matrix4f().scale(1F, 1F, 0.6F).transpose().translate(-0.5F, -0.5F, 0));
                holeDisplayPuff.setBrightness(new Display.Brightness(15, 15));
            }, 3);

            Utils.scheduler.runTaskLater(Utils.plugin, () -> {
                holeDisplayPuff.remove();
            }, 6);

            Utils.scheduler.runTaskLater(Utils.plugin, () -> {
                holedisplay.remove();
                holedisplay1.remove();
            }, 260); //25
        }

        Utils.scheduler.runTaskLater(Utils.plugin, () -> {
            display.setBlock(Material.LIGHT_GRAY_STAINED_GLASS.createBlockData());
        }, 3);

        Utils.scheduler.runTaskLater(Utils.plugin, () -> {
            display.setBlock(Material.WHITE_STAINED_GLASS.createBlockData());
        }, 7);

        BukkitTask finalTask = task;
        Utils.scheduler.runTaskLater(Utils.plugin, () -> {
            display.remove();
            display1.remove();
            if (finalTask != null)
                finalTask.cancel();
        }, 12);

    }

    private static double getDistanceToFace(Location targetLocation, Location faceLocation) {
        return targetLocation.distance(faceLocation);
    }

    public static Object[] getLookingAtBlockSpot(Location loc) {
        // Step 1: Get player's eye location and the direction they are looking
        Location eyeLocation = loc;
        Vector direction = eyeLocation.getDirection();

        // Step 2: Set maximum distance for the raytrace (you can adjust this as needed)
        double maxDistance = 100; // Maximum distance you want to trace

        // Step 4: Trace the line of sight and find the first non-air block with 0.01 increments
        for (double distance = 0.01; distance <= maxDistance; distance += 0.01) {
            // Calculate the current location the player is looking at with the smaller increment
            Location currentLocation = eyeLocation.clone().add(direction.clone().multiply(distance));

            for (LivingEntity entity : currentLocation.getNearbyLivingEntities(0.5, 0.5, 0.5)) {
                if (entity.getBoundingBox().contains(currentLocation.toVector())) {
                    return new Object[]{distance, currentLocation, entity};
                }
            }

            // Step 5: Check if the block is solid (not air)
            if (currentLocation.getBlock().getType() != Material.AIR) {
                // Add the exact distance and location to the HashMap
                return new Object[]{distance, currentLocation};
            }
        }

        // If no block is hit within the maximum distance, return an empty map
        return new Object[]{100.0};
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String cmd, @NotNull String[] args) {
        List<String> arguments = new ArrayList<>();

        return arguments;
    }
}
