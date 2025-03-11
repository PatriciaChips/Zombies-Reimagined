package org.pat.zombiesReimagined.Utility.MapUtils.StructUtils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;
import org.joml.Matrix4f;
import org.pat.zombiesReimagined.Utility.ZUtils;

import java.util.HashMap;

public class Selection {

    public static HashMap<Player, Location[]> selection = new HashMap<>();
    public static HashMap<Player, BlockDisplay> selectionDisplay = new HashMap<>();

    public static void createSelection(Player p, Block block) {
        if (Selection.selection.containsKey(p) && Selection.selection.get(p).length == 2) {
            Selection.selectionDisplay.get(p).remove();
            Selection.selection.remove(p);
            Selection.selectionDisplay.remove(p);
            return;
        } else {
            if (Selection.selection.containsKey(p) && Selection.selection.get(p).length == 1) {
                if (!Selection.selection.get(p)[0].equals(block.getLocation())) {
                    Selection.selection.put(p, new Location[]{Selection.selection.get(p)[0], block.getLocation()});
                    for (Entity entity : Selection.selection.get(p)[0].getNearbyEntitiesByType(BlockDisplay.class, 0.1, 0.1, 0.1)) {
                        if (!entity.hasMetadata(p.getUniqueId().toString()))
                            continue;

                        BlockDisplay selectionDisplay = (BlockDisplay) entity;

                        Location topCorner = Selection.selection.get(p)[1].clone();
                        Location bottomCorner = Selection.selection.get(p)[0].clone();

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
                Selection.selection.put(p, new Location[]{block.getLocation()});
            }
        }
        for (Location loc : Selection.selection.get(p)) {
            BlockDisplay blockDisplay = (BlockDisplay) p.getWorld().spawn(loc.clone().add(0.005, 0.005, 0.005), BlockDisplay.class);
            blockDisplay.setBlock(Material.WHITE_STAINED_GLASS.createBlockData());
            blockDisplay.setMetadata(p.getUniqueId().toString(), new FixedMetadataValue(ZUtils.plugin, p.getUniqueId().toString()));
            blockDisplay.setGlowing(true);
            blockDisplay.setTransformationMatrix(new Matrix4f().scaleLocal(0.99F));
            Selection.selectionDisplay.put(p, blockDisplay);
        }
    }

}
