package org.pat.zombiesReimagined.Utility.MapUtils.StructUtils;

import org.bukkit.*;
import org.bukkit.Color;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Powerable;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.block.data.type.Switch;
import org.bukkit.entity.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.pat.pattyEssentialsV3.ColoredText;
import org.pat.pattyEssentialsV3.Utils;
import org.pat.zombiesReimagined.Commands.Test;
import org.pat.zombiesReimagined.Utility.ItemUtils.Guns;
import org.pat.zombiesReimagined.Utility.ItemUtils.Item;
import org.pat.zombiesReimagined.Utility.ItemUtils.UseType;
import org.pat.zombiesReimagined.Utility.MapUtils.DoorType;
import org.pat.zombiesReimagined.Utility.MapUtils.IdentifiedStructures;
import org.pat.zombiesReimagined.Utility.MapUtils.MapFeature;
import org.pat.zombiesReimagined.Utility.ZUtils;

import java.awt.*;
import java.util.*;
import java.util.List;

public class InteractStructure {

    public static void interact(PlayerInteractEvent e, Block block, Player p) {
        for (var v : MapFeature.storedStructures.entrySet()) {
            MapFeature feature = v.getKey();
            Location loc = v.getValue();
            if (feature.getExtraBlocks() == null || feature.getExtraBlocks().contains(block)) {
                switch (feature.getType()) {
                    case UNLOCK_DOOR:
                        for (Block strucBlock : feature.getExtraBlocks()) {
                            if (block.getX() == strucBlock.getX() && block.getY() == strucBlock.getY() && block.getZ() == strucBlock.getZ()) {
                                String sideName = "";
                                int sideCost = 0;
                                int tDirectionInt = 0;
                                float txAddon = 0;
                                float tzAddon = 0;
                                boolean isWestSouth = e.getBlockFace() == BlockFace.EAST || e.getBlockFace() == BlockFace.WEST;
                                switch (e.getBlockFace()) {
                                    case NORTH:
                                        tzAddon = -0.5F;
                                        break;
                                    case EAST:
                                        txAddon = 0.5F;
                                        break;
                                    case SOUTH:
                                        tzAddon = 0.5F;
                                        break;
                                    case WEST:
                                        txAddon = -0.5F;
                                        break;
                                }
                                float xAddon = txAddon;
                                float zAddon = tzAddon;
                                switch (e.getBlockFace()) {
                                    case EAST, NORTH:
                                        tDirectionInt = 1;
                                        break;
                                    case WEST, SOUTH:
                                        tDirectionInt = -1;
                                }
                                if (e.getBlockFace().equals(getBlockFacesFromYaw(loc.getYaw())[0])) {
                                    sideName = feature.getName();
                                    sideCost = feature.getCost();
                                } else if (e.getBlockFace().equals(getBlockFacesFromYaw(loc.getYaw())[1])) {
                                    sideName = feature.getName1();
                                    sideCost = feature.getCost1();
                                }
                                final int directionInt = tDirectionInt;
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
                                    float division = 4; //iteration points for rotating doors (MAX 20)
                                    float yAddon = -4;
                                    boolean isRotatingDoor = feature.getDoorType() == DoorType.doubleRotate3x3
                                            || feature.getDoorType() == DoorType.doubleRotate4x4
                                            || feature.getDoorType() == DoorType.upRotate5x4;
                                    boolean isUpRotation = feature.getDoorType() == DoorType.upRotate5x4;
                                    int groupIteration = 1;
                                    for (Block[] group : sortBlocksByY(feature.getExtraBlocks())) {
                                        int sideCheck = 1;
                                        for (Block structureBlocks : group) {
                                            if (feature.getDoorType() == DoorType.doubleRotate3x3) {
                                                if (sideCheck >= 4)
                                                    sideCheck = 1;
                                            } else {
                                                if (sideCheck >= 5)
                                                    sideCheck = 1;
                                            }

                                            if (groupIteration == 4) {
                                                if (sideCheck == 1)
                                                    sideCheck++;
                                                if (sideCheck == 4)
                                                    sideCheck--;
                                            }

                                            Location rightLoc = loc.clone();
                                            rightLoc.setYaw(rightLoc.getYaw() + 90);
                                            Location leftLoc = loc.clone();
                                            leftLoc.setYaw(leftLoc.getYaw() - 90);

                                            Location bLoc = structureBlocks.getLocation();
                                            BlockData data = structureBlocks.getBlockData();
                                            Material blockType = structureBlocks.getType();

                                            //** DEBUG
                                            Location dLoc = bLoc.clone();
                                            Utils.scheduler.runTaskLater(ZUtils.plugin, () -> {
                                                dLoc.getBlock().setType(blockType);
                                                dLoc.getBlock().setBlockData(data);
                                            }, 60);
                                            //*/

                                            if (isRotatingDoor) {
                                                if (feature.isTwoBlockCenter()) {
                                                    bLoc.setX(loc.getX());
                                                    bLoc.setZ(loc.getZ());
                                                    if (sideCheck == 1 || sideCheck == 2) {
                                                        bLoc.add(rightLoc.getDirection().normalize().multiply((loc.getYaw() == 270 || loc.getYaw() == 180) ? -2 : 2));
                                                    } else if (sideCheck == 3 || sideCheck == 4) {
                                                        bLoc.add(leftLoc.getDirection().normalize().multiply((loc.getYaw() == 270 || loc.getYaw() == 180) ? -2 : 2));
                                                    }
                                                    bLoc.add(xAddon, 0, zAddon);
                                                } else if (feature.getDoorType() == DoorType.doubleRotate3x3) {
                                                    bLoc.setX(loc.getX());
                                                    bLoc.setZ(loc.getZ());
                                                    if (sideCheck == 1 || sideCheck == 2) {
                                                        bLoc.add(rightLoc.getDirection().normalize().multiply((loc.getYaw() == 270 || loc.getYaw() == 180) ? -1.5 : 1.5));
                                                    } else if (sideCheck == 3) {
                                                        bLoc.add(leftLoc.getDirection().normalize().multiply((loc.getYaw() == 270 || loc.getYaw() == 180) ? -1.5 : 1.5));
                                                    }
                                                    bLoc.add(xAddon, 0, zAddon);
                                                } else {
                                                    bLoc.setY(loc.getY() + 5);
                                                    bLoc.add(0.5, 0, 0.5);
                                                }
                                            }

                                            if (feature.getDoorType() == DoorType.doubleRotate3x3)
                                                if (sideCheck == 3)
                                                    sideCheck = 4;

                                            BlockDisplay blockDisplay = bLoc.getWorld().spawn(bLoc, BlockDisplay.class);
                                            feature.setStructureEntities(blockDisplay);
                                            //blockDisplay.setBrightness(new Display.Brightness(9, 9));
                                            blockDisplay.setInterpolationDuration((isRotatingDoor) ? 20 / (int) division : 20); //20
                                            blockDisplay.setInterpolationDelay(-1);
                                            blockDisplay.setBlock(data);

                                            Location tbloc = bLoc.clone().add(loc.getYaw() == 180 || loc.getYaw() == 0 ? 3:0, 0, loc.getYaw() == 90 || loc.getYaw() == 270 ? 3:0); //.add(leftLoc.getDirection().normalize().multiply((loc.getYaw() == 270 || loc.getYaw() == 180) ? -3 : 3)

                                            BlockDisplay centerBlockDisplay = bLoc.getWorld().spawn(tbloc, BlockDisplay.class);
                                            centerBlockDisplay.setInterpolationDuration(blockDisplay.getInterpolationDuration()); //20
                                            centerBlockDisplay.setInterpolationDelay(blockDisplay.getInterpolationDelay());
                                            centerBlockDisplay.setBlock(blockDisplay.getBlock());

                                            if (feature.getDoorType() != DoorType.doubleRotate3x3) {
                                                centerBlockDisplay.remove();
                                            } else {
                                                if (sideCheck != 2) {
                                                    centerBlockDisplay.remove();
                                                } else {
                                                    feature.setStructureEntities(centerBlockDisplay);
                                                }
                                            }

                                            Matrix4f tinitialMatrix = new Matrix4f().translateLocal(-0.5F, yAddon, -0.5F);
                                            Matrix4f tinitialCMatrix = null;
                                            if (isRotatingDoor && !feature.isTwoBlockCenter())
                                                blockDisplay.setTransformationMatrix(tinitialMatrix);
                                            if (isRotatingDoor) {
                                                tinitialMatrix = new Matrix4f().translateLocal(0, 0, -0.5F).scaleLocal(0.5F, 1, 1);
                                                switch ((int) loc.getYaw()) {
                                                    case 90, 270:
                                                        tinitialMatrix = new Matrix4f().scaleLocal(0.5F, 1, (feature.getDoorType() == DoorType.doubleRotate3x3 && sideCheck == 2) ? 0.5F : 1).translateLocal(e.getBlockFace() == BlockFace.EAST ? txAddon : 0, 0, 0).translateLocal((e.getBlockFace() == BlockFace.EAST) ? -1 : 0, 0, ((sideCheck == 1) ? 0 : sideCheck == 2 ? 1 : sideCheck == 3 ? -2 : sideCheck == 4 ? -1 : 0));
                                                        break;
                                                    case 0, 180:
                                                        tinitialMatrix = new Matrix4f().scaleLocal((feature.getDoorType() == DoorType.doubleRotate3x3 && sideCheck == 2) ? 0.5F : 1, 1, 0.5F).translateLocal(0, 0, e.getBlockFace() == BlockFace.SOUTH ? tzAddon : 0).translateLocal(((sideCheck == 1) ? 0 : sideCheck == 2 ? 1 : sideCheck == 3 ? -2 : sideCheck == 4 ? -1 : 0), 0, (e.getBlockFace() == BlockFace.SOUTH) ? -1 : 0);
                                                        break;
                                                }
                                                blockDisplay.setTransformationMatrix(tinitialMatrix);
                                                if (feature.getDoorType() == DoorType.doubleRotate3x3 && sideCheck == 2) {
                                                    try {
                                                        float xCA = loc.getYaw() == 180 ? -3F:(loc.getYaw() == 0) ? -3F:0;
                                                        float zCa = loc.getYaw() == 270 ? -3F:(loc.getYaw() == 90) ? -3F:0;
                                                        tinitialCMatrix = ((Matrix4f) tinitialMatrix.clone()).translateLocal((loc.getYaw() == 90 || loc.getYaw() == 270 ? 0 : 0.5F) + xCA, 0, (loc.getYaw() == 0 || loc.getYaw() == 180 ? 0 : 0.5F) + zCa);
                                                        centerBlockDisplay.setTransformationMatrix(tinitialCMatrix);
                                                    } catch (CloneNotSupportedException ex) {
                                                        throw new RuntimeException(ex);
                                                    }
                                                }
                                            }

                                            final Matrix4f initialMatrix = tinitialMatrix;
                                            final Matrix4f initialCMatrix = tinitialCMatrix;

                                            Utils.scheduler.runTaskLater(ZUtils.plugin, () -> {
                                                structureBlocks.setType(Material.AIR);
                                            }, 2);

                                            int fSideCheck = sideCheck;
                                            Utils.scheduler.runTaskLater(ZUtils.plugin, () -> {
                                                if (isRotatingDoor) {
                                                    new BukkitRunnable() {
                                                        Matrix4f tMat = initialMatrix;
                                                        Matrix4f tCMat = initialCMatrix;
                                                        int iterations = 0;

                                                        public void run() {
                                                            blockDisplay.setInterpolationDelay(-1);
                                                            blockDisplay.setInterpolationDuration(20 / (int) division);
                                                            if (feature.isTwoBlockCenter() || feature.getDoorType() == DoorType.doubleRotate3x3) {
                                                                float rotation = -75 * directionInt / division * ((fSideCheck == 1 || fSideCheck == 2) ? 1 : -1);
                                                                Matrix4f matrix4f = new Matrix4f().rotateY((float) Math.toRadians(rotation)).mul(tMat);
                                                                tMat = matrix4f;
                                                                blockDisplay.setTransformationMatrix(matrix4f);
                                                                if (feature.getDoorType() == DoorType.doubleRotate3x3 && fSideCheck == 2) {
                                                                    centerBlockDisplay.setInterpolationDelay(-1);
                                                                    centerBlockDisplay.setInterpolationDuration(20 / (int) division);
                                                                    Matrix4f matrix4fC = new Matrix4f().rotateY((float) Math.toRadians(rotation * -1)).mul(tCMat);
                                                                    tCMat = matrix4fC;
                                                                    centerBlockDisplay.setTransformationMatrix(matrix4fC);
                                                                }
                                                            } else {
                                                                Matrix4f matrix4f = new Matrix4f().rotateZ((float) Math.toRadians((isWestSouth) ? -90 * directionInt / division : 0)).rotateX((float) Math.toRadians((!isWestSouth) ? -90 * directionInt / division : 0)).mul(tMat);
                                                                tMat = matrix4f;
                                                                blockDisplay.setTransformationMatrix(matrix4f);
                                                            }
                                                            iterations++;
                                                            if (iterations == division)
                                                                cancel();
                                                        }
                                                    }.runTaskTimer(ZUtils.plugin, 0L, (20 / (int) division));
                                                } else {
                                                    blockDisplay.setTransformationMatrix(new Matrix4f().translate(0, 4.01F, 0));
                                                }
                                            }, 3); //3

                                            Utils.scheduler.runTaskLater(ZUtils.plugin, () -> {
                                                if (!isRotatingDoor)
                                                    blockDisplay.remove();
                                            }, i);

                                            if (bLoc.getY() == loc.getY() + 1) {
                                                bLoc.getWorld().spawnParticle(Particle.BLOCK, bLoc.clone().add(0.5, 0, 0.5), 10, 0.7, 0.1, 0.7, data);
                                                bLoc.getWorld().spawnParticle(Particle.DUST, bLoc.clone().add(0.5, 2, 0.5), 10, 0.7, 1.5, 0.7, new Particle.DustOptions(Color.fromRGB(135, 135, 135), 0.75F));
                                            }
                                            sideCheck += groupIteration <= 3 ? 1 : 2;
                                        }
                                        i -= 4;
                                        yAddon += 1F;
                                        groupIteration++;
                                    }
                                }
                                break;
                            }
                        }
                        break;
                    case GUN:
                        e.setCancelled(true);
                        for (Block strucBlock : feature.getExtraBlocks()) {
                            if (block.equals(strucBlock)) {
                                if (feature.containsPlayer(p)) {
                                    p.sendMessage("Ammo" + " | $" + feature.getCost1());
                                    dispenseItems(p, loc.clone().add(0, 1.5, 0), new ItemStack[]{new ItemStack(Material.PRISMARINE_CRYSTALS), new ItemStack(Material.PRISMARINE_CRYSTALS), new ItemStack(Material.PRISMARINE_CRYSTALS)});
                                } else {
                                    p.sendMessage(feature.getName() + " | $" + feature.getCost());
                                    feature.addContainedPlayers(p);
                                    feature.statusVisibilitySwap(false);
                                    p.sendMessage(feature.getKey());
                                    ItemStack gunItem = new Item(UseType.GUN, feature.getName(), feature.getMaterial(), feature.getKey(), Test.useModels).getItemStack();
                                    p.getEquipment().setItemInMainHand(gunItem);

                                    dispenseItems(p, loc.clone().add(0, 1.5, 0), new ItemStack[]{gunItem});
                                }
                            }
                        }
                        break;
                    case ARMOR:
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

                                    dispenseItems(p, loc.clone().add(0, 1.5, 0), spitOutItems);
                                }
                                break;
                            }
                        }
                        break;
                    case CHEST:
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
                                                changeItem = Guns.gunArray[new Random().nextInt(Guns.gunArray.length)].getItemStack();
                                            } while (changeItem.equals(currentGun));
                                            pack.setItemStack(new Item(UseType.GUN, changeItem.getItemMeta().getDisplayName(), changeItem.getType(), changeItem.getItemMeta().getPersistentDataContainer().get(ZUtils.key, PersistentDataType.STRING), true).getItemStack());
                                            packless.setItemStack(new Item(UseType.GUN, changeItem.getItemMeta().getDisplayName(), changeItem.getType(), changeItem.getItemMeta().getPersistentDataContainer().get(ZUtils.key, PersistentDataType.STRING), false).getItemStack());
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
                        break;
                    case POWERUP:
                        if (!feature.containsPlayer(p)) {
                            if (Tag.BUTTONS.isTagged(block.getType())) {
                                Switch button = (Switch) block.getBlockData();
                                Vector vec = SpawnStructure.blockFaceToVector(button.getFacing(), button.getAttachedFace());
                                Test.createSpark(block.getLocation().add(0.5, 0.5, 0.5).add(vec.clone().multiply(-0.4)), vec, 100, 0.05F, 7, 2, 0.2F, 4, Material.PURPLE_STAINED_GLASS);
                                boolean isClear = MapFeature.isMaterialSafe(loc.clone().add(0, 1, 0).getBlock().getType());
                                Location lightSparkLoc = loc.clone().add(0, (isClear) ? 4.75 : 4, 0).add(loc.getDirection());
                                Test.createSpark(lightSparkLoc, new Vector(0, 1, 0), 360, 0.05F, 14, 2, 0.35F, 3, Material.YELLOW_STAINED_GLASS);
                                feature.addContainedPlayers(p);
                                feature.statusVisibilitySwap(false);
                                p.playSound(p, Sound.ENTITY_ILLUSIONER_PREPARE_BLINDNESS, 0.5F, 1);
                                e.setCancelled(true);
                                for (Block block1 : feature.getExtraBlocks()) {
                                    if (Tag.STAIRS.isTagged(block1.getType())) {
                                        boolean isTop = ((Stairs) block1.getBlockData()).getHalf() == Bisected.Half.TOP;
                                        Location dLoc = block1.getLocation().add(0.5, (isTop) ? 0 : 0.5F, 0.5);
                                        dLoc.setDirection(loc.getDirection());
                                        dispenseItems(p, dLoc, new ItemStack[]{new ItemStack(Material.valueOf(loc.clone().add(loc.getDirection()).getBlock().getType().toString().replace("WOOL", "DYE")))});
                                        break;
                                    }
                                }
                            }
                        } else {
                            e.setCancelled(true);
                        }
                        break;
                }
            }
        }
    }

    public static List<Location> generateCurvedPath(Location startLocation, Location targetLocation, int steps, double curveIntensity) {
        List<Location> path = new ArrayList<>();

        for (int i = 0; i < steps; i++) {
            double t = (double) i / (steps - 1); // Parametric value from 0 to 1

            // Use sine function for a curved path (you can change this for more complex curves)
            double x = startLocation.getX() + t * (targetLocation.getX() - startLocation.getX());
            double y = startLocation.getY() + Math.sin(t * Math.PI) * curveIntensity;  // Curved movement in the Y axis
            double z = startLocation.getZ() + t * (targetLocation.getZ() - startLocation.getZ());

            path.add(new Location(startLocation.getWorld(), x, y, z));
        }

        return path;
    }

    public static void dispenseItems(Player p, Location loc, ItemStack[] items) {
        ItemStack[] spitOutItems = items;

        Location adLoc = loc.clone();

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
