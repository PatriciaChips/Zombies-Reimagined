package org.pat.zombiesReimagined.Utility.MapUtils.StructUtils;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import org.joml.Matrix4f;
import org.pat.pattyEssentialsV3.ColoredText;
import org.pat.pattyEssentialsV3.Utils;
import org.pat.zombiesReimagined.Commands.Test;
import org.pat.zombiesReimagined.Utility.ItemUtils.Guns;
import org.pat.zombiesReimagined.Utility.ItemUtils.Item;
import org.pat.zombiesReimagined.Utility.ItemUtils.UseType;
import org.pat.zombiesReimagined.Utility.MapUtils.IdentifiedStructures;
import org.pat.zombiesReimagined.Utility.MapUtils.MapFeature;
import org.pat.zombiesReimagined.Utility.ZUtils;

import java.util.*;

public class InteractStructure {

    public static void interact(PlayerInteractEvent e, Block block, Player p) {
        for (var v : MapFeature.storedStructures.entrySet()) {
            MapFeature feature = v.getKey();
            Location loc = v.getValue();
            switch (feature.getType()) {
                case UNLOCK_DOOR:
                    if (feature.getExtraBlocks().contains(block)) {
                        for (Block strucBlock : feature.getExtraBlocks()) {
                            if (block.getX() == strucBlock.getX() && block.getY() == strucBlock.getY() && block.getZ() == strucBlock.getZ()) {
                                String sideName = "";
                                int sideCost = 0;
                                if (e.getBlockFace().equals(getBlockFacesFromYaw(loc.getYaw())[0])) {
                                    sideName = feature.getName();
                                    sideCost = feature.getCost();
                                } else if (e.getBlockFace().equals(getBlockFacesFromYaw(loc.getYaw())[1])) {
                                    sideName = feature.getName1();
                                    sideCost = feature.getCost1();
                                }
                                if (sideCost > 0) {
                                    p.sendMessage(sideName + ": $" + sideCost);
                                    // CHECK IF THEY HAVE ENOUYGH MONEY
                                    p.getWorld().playSound(loc, Sound.BLOCK_DISPENSER_FAIL, 0.2F, 0.7F);
                                    Utils.scheduler.runTaskLater(ZUtils.plugin, () -> {
                                        p.getWorld().playSound(loc, Sound.BLOCK_DISPENSER_FAIL, 0.5F, 1.5F);
                                        p.getWorld().playSound(loc, Sound.BLOCK_PISTON_CONTRACT, 1.4F, 0.8F);
                                        p.getWorld().playSound(loc, Sound.BLOCK_PISTON_CONTRACT, 1.4F, 1F);
                                        p.getWorld().playSound(loc, Sound.ENTITY_MINECART_RIDING, 0.1F, 1.6F);
                                    }, 1);
                                    for (Entity entity : feature.getStructureEntities()) {
                                        entity.remove();
                                    }
                                    int i = 23;
                                    for (Block[] group : sortBlocksByY(feature.getExtraBlocks())) {
                                        for (Block structureBlocks : group) {
                                            Location bLoc = structureBlocks.getLocation();
                                            BlockData data = structureBlocks.getBlockData();
                                            Material blockType = structureBlocks.getType();
                                            BlockDisplay blockDisplay = bLoc.getWorld().spawn(bLoc, BlockDisplay.class);
                                            blockDisplay.setBrightness(new Display.Brightness(15, 15));
                                            blockDisplay.setInterpolationDuration(20);
                                            blockDisplay.setInterpolationDelay(2);
                                            blockDisplay.setBlock(data);
                                            Utils.scheduler.runTaskLater(ZUtils.plugin, () -> {
                                                structureBlocks.setType(Material.AIR);
                                            }, 2);
                                            Utils.scheduler.runTaskLater(ZUtils.plugin, () -> {
                                                blockDisplay.setTransformationMatrix(new Matrix4f().translate(0, 4.01F, 0));
                                            }, 3);
                                            Utils.scheduler.runTaskLater(ZUtils.plugin, () -> {
                                                blockDisplay.remove();
                                            }, i);
                                            if (bLoc.getY() == loc.getY() + 1) {
                                                bLoc.getWorld().spawnParticle(Particle.BLOCK, bLoc.clone().add(0.5, 0, 0.5), 10, 0.7, 0.1, 0.7, data);
                                                bLoc.getWorld().spawnParticle(Particle.DUST, bLoc.clone().add(0.5, 2, 0.5), 10, 0.7, 1.5, 0.7, new Particle.DustOptions(Color.fromRGB(135, 135, 135), 0.75F));
                                            }
                                            //** DEBUG
                                            Utils.scheduler.runTaskLater(ZUtils.plugin, () -> {
                                                bLoc.getBlock().setType(blockType);
                                                bLoc.getBlock().setBlockData(data);
                                            }, 60);
                                            //*/
                                        }
                                        i -= 4;
                                    }
                                }
                                break;
                            }
                        }
                    }
                    break;
                case GUN:
                    if (feature.getExtraBlocks().contains(block)) {
                        e.setCancelled(true);
                        for (Block strucBlock : feature.getExtraBlocks()) {
                            if (block.equals(strucBlock)) {
                                if (feature.containsPlayer(p)) {
                                    p.sendMessage("Ammo" + " | $" + feature.getCost1());
                                    dispenseItems(p, loc, new ItemStack[]{new ItemStack(Material.PRISMARINE_CRYSTALS), new ItemStack(Material.PRISMARINE_CRYSTALS), new ItemStack(Material.PRISMARINE_CRYSTALS)});
                                } else {
                                    p.sendMessage(feature.getName() + " | $" + feature.getCost());
                                    feature.addContainedPlayers(p);
                                    feature.statusVisibilitySwap(false);
                                    p.sendMessage(feature.getKey());
                                    ItemStack gunItem = new Item(UseType.GUN, feature.getName(), feature.getMaterial(), 1, feature.getKey(), Test.useModels);
                                    p.getEquipment().setItemInMainHand(gunItem);

                                    dispenseItems(p, loc, new ItemStack[]{gunItem});
                                }
                            }
                        }
                    }
                    break;
                case ARMOR:
                    if (feature.getExtraBlocks().contains(block)) {
                        for (Block strucBlock : feature.getExtraBlocks()) {
                            if (block.equals(strucBlock)) {
                                if (feature.containsPlayer(p)) {
                                    p.sendMessage("Already purchased!");
                                } else {
                                    p.sendMessage(feature.getName() + " | $" + feature.getCost());
                                    feature.addContainedPlayers(p);
                                    feature.statusVisibilitySwap(false);

                                    ItemStack boot = Utils.i.createItemstack("", IdentifiedStructures.convertMaterialToArmour(feature.getMaterial())[0].getType(), 1, null, null, true, false);
                                    ItemStack leggings = Utils.i.createItemstack("", IdentifiedStructures.convertMaterialToArmour(feature.getMaterial())[1].getType(), 1, null, null, true, false);
                                    ItemStack chestplate = Utils.i.createItemstack("", IdentifiedStructures.convertMaterialToArmour(feature.getMaterial())[2].getType(), 1, null, null, true, false);
                                    ItemStack helmet = Utils.i.createItemstack("", IdentifiedStructures.convertMaterialToArmour(feature.getMaterial())[3].getType(), 1, null, null, true, false);

                                    ItemStack[] spitOutItems = new ItemStack[]{};

                                    if (feature.isBottomHalf()) {
                                        p.getEquipment().setBoots(boot);
                                        p.getEquipment().setLeggings(leggings);
                                        spitOutItems = new ItemStack[]{boot, leggings};
                                    } else {
                                        p.getEquipment().setChestplate(chestplate);
                                        p.getEquipment().setHelmet(helmet);
                                        spitOutItems = new ItemStack[]{chestplate, helmet};
                                    }

                                    dispenseItems(p, loc, spitOutItems);
                                }
                                break;
                            }
                        }
                    }
                    break;
                case CHEST:
                    if (feature.getExtraBlocks().contains(block)) {
                        for (Block strucBlock : feature.getExtraBlocks()) {
                            if (block.equals(strucBlock)) {
                                e.setCancelled(true);

                                Chest chest = (Chest) strucBlock.getState();

                                if (feature.getContainedTimedPlayers().size() > 0) {
                                    for (var v1 : feature.getContainedTimedPlayers().entrySet()) {
                                        if (p == v1.getKey()) {
                                            if (feature.playerHasContainedGun(p)) {
                                                chest.close();
                                                p.getInventory().addItem(feature.getContainedPlayersGun(p));
                                                feature.clearGunContainedPlayers();
                                                feature.clearContainedTimedPlayers();
                                                feature.statusVisibilitySwap(false);
                                            } else {
                                                p.sendMessage(ColoredText.t("&7&oYou're already using this lucky chest!"));
                                            }
                                        } else {
                                            p.sendMessage(ColoredText.t("&7&o" + v1.getKey().getName() + " is using this lucky chest!"));
                                        }
                                    }
                                    return;
                                }

                                chest.open();

                                long useTime = System.currentTimeMillis();
                                feature.addContainedTimedPlayers(p, useTime);

                                feature.statusVisibilitySwap(true);
                                ItemDisplay itemDisplayPackless = null;
                                ItemDisplay itemDisplayPack = null;
                                TextDisplay underText = null;
                                for (Entity entity : feature.getPreHiddenEntities()) {
                                    if (entity instanceof ItemDisplay itemDisplay) {
                                        itemDisplay.setInterpolationDelay(2);
                                        itemDisplay.setInterpolationDuration(7);
                                        if (feature.getPackEnabledEntities().contains(entity)) {
                                            itemDisplayPack = itemDisplay;
                                            Utils.scheduler.runTaskLater(ZUtils.plugin, () -> {
                                                itemDisplay.setTransformationMatrix(new Matrix4f().scaleLocal(1F).translate(-0.13F, -0.35F, 0F).rotateLocalY((float) Math.toRadians(90)));
                                            }, 3);
                                        } else {
                                            itemDisplayPackless = itemDisplay;
                                            Utils.scheduler.runTaskLater(ZUtils.plugin, () -> {
                                                itemDisplay.setTransformationMatrix(new Matrix4f().scale(1.3F).translate(0.08F, -0.4F, 0.13F).rotateZ(79.318F));
                                            }, 3);
                                        }
                                    } else {
                                        underText = (TextDisplay) entity;
                                    }
                                }

                                final ItemDisplay pack = itemDisplayPack;
                                final ItemDisplay packless = itemDisplayPackless;
                                final TextDisplay text = underText;

                                text.setText(ColoredText.t("&7" + p.getName() + " is gambling..."));

                                int soundSpeed = 2;
                                BukkitTask task = new BukkitRunnable() {
                                    int i = 0;
                                    float[] sounds = new float[]{1.3F, 1.4F};
                                    ItemStack currentGun = null;

                                    public void run() {

                                        if (i < sounds.length) {
                                            p.getWorld().playSound(loc, Sound.BLOCK_NOTE_BLOCK_HARP, 0.1F, sounds[i]);
                                            p.getWorld().playSound(loc, Sound.BLOCK_NOTE_BLOCK_BELL, 0.05F, sounds[i]);
                                        }

                                        i++;
                                        if (i > sounds.length) {
                                            i = 0;
                                            ItemStack changeItem = null;
                                            do {
                                                changeItem = Guns.gunArray[new Random().nextInt(Guns.gunArray.length)];
                                            } while (changeItem.equals(currentGun));  // Keep looping until we get a different item
                                            pack.setItemStack(new Item(UseType.GUN, changeItem.getItemMeta().getDisplayName(), changeItem.getType(), 1, changeItem.getItemMeta().getPersistentDataContainer().get(ZUtils.key, PersistentDataType.STRING), true));
                                            packless.setItemStack(new Item(UseType.GUN, changeItem.getItemMeta().getDisplayName(), changeItem.getType(), 1, changeItem.getItemMeta().getPersistentDataContainer().get(ZUtils.key, PersistentDataType.STRING), false));
                                            currentGun = changeItem;
                                        }

                                    }
                                }.runTaskTimer(ZUtils.plugin, 0L, soundSpeed);

                                Utils.scheduler.runTaskLater(ZUtils.plugin, () -> { //end of roll
                                    task.cancel();
                                    if (Test.useModels) {
                                        feature.addGunContainedPlayer(p, pack.getItemStack());
                                    } else {
                                        feature.addGunContainedPlayer(p, packless.getItemStack());
                                    }
                                    for (int i = 0; i < 5; i++) {
                                        int fI = i;
                                        Utils.scheduler.runTaskLater(ZUtils.plugin, () -> {
                                            if (feature.containsTimedPlayer(p) == useTime) {
                                                text.setText(ColoredText.t("&7Accept the " + packless.getItemStack().getItemMeta().getDisplayName() + "? (" + (5 - fI) + "s)"));
                                            }
                                        }, (i) * 20);
                                    }
                                    Utils.scheduler.runTaskLater(ZUtils.plugin, () -> { //end of choice time
                                        if (feature.containsTimedPlayer(p) == useTime) {
                                            chest.close();
                                            feature.clearContainedTimedPlayers();
                                            feature.clearGunContainedPlayers();
                                            Utils.scheduler.runTaskLater(ZUtils.plugin, () -> {
                                                feature.statusVisibilitySwap(false);
                                            }, 4);
                                            for (Entity entity : feature.getPreHiddenEntities()) {
                                                if (entity instanceof ItemDisplay itemDisplay) {
                                                    if (feature.getPackEnabledEntities().contains(entity)) {
                                                        itemDisplay.setTransformationMatrix(new Matrix4f().scaleLocal(0.3F).translate(-0.13F, -4.3F, 0F).rotateLocalY((float) Math.toRadians(90)));
                                                    } else {
                                                        itemDisplay.setTransformationMatrix(new Matrix4f().scale(0.3F).translate(0.08F, -4.4F, 0.13F).rotateZ(79.318F));
                                                    }
                                                }
                                            }
                                        }
                                    }, 100); //10s + 5seconds to decide if they want the gun or not
                                }, 200);
                            }
                        }
                    }
                    break;
            }
        }
    }

    public static void dispenseItems(Player p, Location loc, ItemStack[] items) {
        ItemStack[] spitOutItems = items;

        Location adLoc = loc.clone().add(0, 1.5, 0);

        if (items.length > 0) {
            p.getWorld().playSound(adLoc.clone(), Sound.BLOCK_NOTE_BLOCK_HAT, 0.5F, 0.6F);
            Utils.scheduler.runTaskLater(ZUtils.plugin, () -> {
                p.getWorld().playSound(adLoc.clone(), Sound.BLOCK_NOTE_BLOCK_HAT, 0.6F, 0.3F);
            }, 3);
        }

        Utils.scheduler.runTaskLater(ZUtils.plugin, () -> {
            int i = 0;
            for (ItemStack spatItem : spitOutItems) {
                Utils.scheduler.runTaskLater(ZUtils.plugin, () -> {
                    float adYaw = adLoc.getYaw();
                    adYaw = adYaw - 25 + new Random().nextInt(51);
                    if (adYaw > 360) {
                        adYaw = adYaw - 360;
                    } else if (adYaw < 0) {
                        adYaw = 360 - adYaw;
                    }
                    adLoc.setYaw(adYaw);

                    org.bukkit.entity.Item itemEntity = p.getWorld().spawn(adLoc.clone().add(adLoc.getDirection().normalize().multiply(-0.25)), org.bukkit.entity.Item.class);
                    itemEntity.setItemStack(spatItem);
                    itemEntity.setVelocity(new Vector());
                    itemEntity.setCanPlayerPickup(false);
                    p.getWorld().spawnParticle(Particle.CLOUD, adLoc.clone().add(adLoc.getDirection().multiply(-0.1)), 3, 0, 0, 0, 0.03);
                    Utils.scheduler.runTaskLater(ZUtils.plugin, () -> {
                        p.getWorld().spawnParticle(Particle.SMOKE, itemEntity.getLocation(), 3, 0, 0, 0, 0);
                    }, 9);
                    itemEntity.setTicksLived(5990);
                    p.getWorld().playSound(adLoc.clone(), Sound.ENTITY_SNIFFER_DROP_SEED, 0.7F, 1F);
                    itemEntity.setVelocity(adLoc.clone().getDirection().multiply(-0.1).add(new Vector(0, 0.2, 0)));
                }, i);
                i += 5;
            }
        }, 3);
    }

    public static BlockFace[] getBlockFacesFromYaw(float yaw) {
        if (yaw == 90) {
            return new BlockFace[]{BlockFace.WEST, BlockFace.EAST};
        } else if (yaw == 270) {
            return new BlockFace[]{BlockFace.EAST, BlockFace.WEST};
        } else if (yaw == 180) {
            return new BlockFace[]{BlockFace.NORTH, BlockFace.SOUTH};
        } else if (yaw == 0) {
            return new BlockFace[]{BlockFace.SOUTH, BlockFace.NORTH};
        } else {
            return new BlockFace[]{BlockFace.NORTH, BlockFace.SOUTH}; // Default, shouldn't reach here
        }
    }

    public static List<Block[]> sortBlocksByY(List<Block> blockList) {
        // Sort the block list by the Y coordinate
        Collections.sort(blockList, new Comparator<Block>() {
            @Override
            public int compare(Block block1, Block block2) {
                // Compare blocks based on their Y coordinate
                return Integer.compare(block1.getLocation().getBlockY(), block2.getLocation().getBlockY());
            }
        });

        // Create a map to group blocks by their Y-coordinate
        Map<Integer, List<Block>> groupedBlocks = new HashMap<>();

        // Group blocks by their Y-coordinate
        for (Block block : blockList) {
            int y = block.getLocation().getBlockY();
            groupedBlocks.putIfAbsent(y, new ArrayList<>());
            groupedBlocks.get(y).add(block);
        }

        // Create a list to hold the arrays of blocks
        List<Block[]> blockArrays = new ArrayList<>();

        // Convert each group of blocks (same Y-coordinate) to a Block[] array
        for (Map.Entry<Integer, List<Block>> entry : groupedBlocks.entrySet()) {
            blockArrays.add(entry.getValue().toArray(new Block[0]));
        }

        // Return the list of Block[] arrays
        return blockArrays;
    }

}
