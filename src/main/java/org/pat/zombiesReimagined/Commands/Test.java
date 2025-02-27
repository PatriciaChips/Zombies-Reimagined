package org.pat.zombiesReimagined.Commands;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.AxisAngle4f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.pat.pattyEssentialsV3.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Test implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String cmd, @NotNull String[] args) {

        if (sender instanceof Player p) {

            Location loc = p.getEyeLocation().clone().add(p.getEyeLocation().getDirection().multiply(0.5f + (0.75f - 0.5f) * new Random().nextFloat()));
            if (new Random().nextInt(5) == 0) {
                loc.setPitch(loc.getPitch() - 5 + new Random().nextInt(11));
                loc.setYaw(loc.getYaw() - 5 + new Random().nextInt(11));
            } else {
                loc.setPitch(loc.getPitch() - 2 + new Random().nextInt(5));
                loc.setYaw(loc.getYaw() - 2 + new Random().nextInt(5));
            }

            BlockDisplay display = (BlockDisplay) p.getWorld().spawnEntity(loc, EntityType.BLOCK_DISPLAY);
            BlockDisplay display1 = (BlockDisplay) p.getWorld().spawnEntity(loc, EntityType.BLOCK_DISPLAY);

            display.setBlock(Material.GRAY_STAINED_GLASS.createBlockData());
            display1.setBlock(Material.BLACK_STAINED_GLASS.createBlockData());
            display.setInterpolationDuration(20);
            display.setViewRange(256);
            display.setBillboard(Display.Billboard.FIXED);
            display1.setBillboard(Display.Billboard.FIXED);
            display.setBrightness(new Display.Brightness(15, 15));
            display1.setBrightness(new Display.Brightness(15, 15));

            //display.setTransformationMatrix(new Matrix4f().scale(0.1F, 0.1F, 0.1F).translate(-.5F, -.5F, 0).translate(0, 0, 0.1F));

            display.setInterpolationDuration(2); // 20 ticks = 1 second
            display1.setInterpolationDuration(2); // 20 ticks = 1 second
            //display.setInterpolationDelay(1);  // 40

            float randomValue = 0.05f + (0.1f - 0.05f) * new Random().nextFloat();

            AxisAngle4f angle = new AxisAngle4f(new Random().nextInt(361), 0, 0 ,1);

            for (var e : getLookingAtBlockSpot(loc).entrySet()) {
                display.setTransformationMatrix(new Matrix4f().scale(randomValue, randomValue, e.getKey().floatValue()).transpose().translate(-0.5F+(randomValue/4), -0.5F+(randomValue/4), 0));
                display1.setTransformationMatrix(new Matrix4f().scale(randomValue/2, randomValue/2, e.getKey().floatValue()).transpose().translate(-0.5F+(randomValue/4), -0.5F+(randomValue/4), 0));
            if (e.getValue() != null)
                p.getWorld().createExplosion(e.getValue(), Double.valueOf(args[0]).floatValue());
            }

            Utils.scheduler.runTaskLater(Utils.plugin, () -> {
                display.setBlock(Material.LIGHT_GRAY_STAINED_GLASS.createBlockData());
            }, 8);

            Utils.scheduler.runTaskLater(Utils.plugin, () -> {
                display.setBlock(Material.WHITE_STAINED_GLASS.createBlockData());
            }, 11);

            Utils.scheduler.runTaskLater(Utils.plugin, () -> {
                display.remove();
                display1.remove();
            }, 14);

        }

        return false;
    }

    public static HashMap<Double, Location> getLookingAtBlockSpot(Location loc) {
        // Step 1: Get player's eye location
        Location eyeLocation = loc;

        // Step 2: Get the direction the player is looking
        Vector direction = eyeLocation.getDirection();

        // Step 3: Find the block the player is looking at
        int maxDistance = 1000; // The maximum distance to trace, you can change this as necessary
        Location hitLocation = null;

        int distance = 0;

        for (int i = 0; i < maxDistance; i++) {
            // Step 4: Trace in the direction the player is looking, one block at a time
            Location currentLocation = eyeLocation.clone().add(direction.clone().multiply(i / 10));

            // If a block is found (a solid block, not air or water)
            if (currentLocation.getBlock().getType() != Material.AIR) {
                hitLocation = currentLocation;
                distance = i;
                break;
            }
        }

        HashMap<Double, Location> meow = new HashMap<>();
        meow.put(Double.valueOf((distance / 10)), hitLocation);

        return meow;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String cmd, @NotNull String[] args) {
        List<String> arguments = new ArrayList<>();

        return arguments;
    }
}
