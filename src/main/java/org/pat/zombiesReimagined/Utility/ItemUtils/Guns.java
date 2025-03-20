package org.pat.zombiesReimagined.Utility.ItemUtils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.pat.pattyEssentialsV3.ColoredText;
import org.pat.pattyEssentialsV3.Utils;
import org.pat.zombiesReimagined.Commands.Test;
import org.pat.zombiesReimagined.Utility.ZUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Guns {

    public static HashMap<ItemStack, Item> registeredGuns = new HashMap<>();
    public static Set<ItemStack> shootCooldown = new HashSet<>();
    public static HashMap<ItemStack, Player> reloadCooldown = new HashMap<>();

    public static Item[] gunArray = new Item[]{
            new Item(UseType.GUN, "Minigun", Material.NETHERITE_HOE, "minigun", false, false, 50, 4, 250, 250, 4, 25, 0),
            new Item(UseType.GUN, "Shotgun", Material.IRON_AXE, "shotgun", false, false, 10, 18, 6, 50, 11, 5, 1),
            new Item(UseType.GUN, "Deagle", Material.IRON_HOE, "deagle", false, false, 100, 0, 12, 100, 7, 20, 0),
            new Item(UseType.GUN, "Double Shotgun", Material.DIAMOND_AXE, "double_shotgun", false, true, 100, 0, 30, 100, 4, 10, 0),
            new Item(UseType.GUN, "Grenade Launcher", Material.NETHERITE_SHOVEL, "grenade_launcher", false, true, 20, 4, 30, 450, 0, 1, 0),
            new Item(UseType.GUN, "LMG", Material.IRON_PICKAXE, "lmg", false, true, 100, 0, 30, 100, 4, 10, 0),
            new Item(UseType.GUN, "MK-47", Material.NETHERITE_PICKAXE, "mk47", false, true, 100, 0, 30, 100, 4, 10, 0),
            new Item(UseType.GUN, "Pistol", Material.WOODEN_HOE, "pistol", false, false, 100, 0, 30, 100, 4, 10, 0),
            new Item(UseType.GUN, "Rocket Launcher", Material.WOODEN_SHOVEL, "rpg", false, true, 30, 0, 1, 5, 0, 20, 0),
            new Item(UseType.GUN, "SMG", Material.STONE_HOE, "smg", false, true, 100, 0, 30, 100, 4, 10, 0),
            new Item(UseType.GUN, "Tommy Gun", Material.DIAMOND_HOE, "tommy_gun", false, true, 100, 0, 30, 100, 4, 10, 0)
    };

    public static Item getGunFromKey(String key) {
        for (Item cItem : gunArray) {
            if (cItem.getItemStack().getItemMeta().getPersistentDataContainer().get(ZUtils.key, PersistentDataType.STRING).equalsIgnoreCase(key)) {
                cItem.setUseModel(Test.useModels);
                return cItem;
            }
        }
        return null;
    }

    public static boolean gunInteract(PlayerInteractEvent e, Player p, ItemStack item, String key, boolean isReload) {

        if (p.getCooldown(item) == 0) {

            Item cItem = Guns.registeredGuns.get(item);

            if (!isReload)
                reloadCooldown.remove(item);

            if (isReload) {
                if (cItem != null) { // ITEM isn't a gun || isnt registered
                    if (cItem.getExtraAmmo() > 0) {
                        boolean isPump = key.equalsIgnoreCase("shotgun");

                        final int iterationDelay = 2;

                        if (cItem.getAmmo() >= cItem.getMagSize())
                            return true;

                        if (isPump && reloadCooldown.containsKey(item))
                            return true;

                        if (isPump)
                            reloadCooldown.put(item, p);

                        new BukkitRunnable() {
                            public void run() {
                                if (!isPump)
                                    cancel();
                                if (!reloadCooldown.containsKey(item) && isPump) {
                                    cancel();
                                    return;
                                }

                                p.setCooldown(item, cItem.getReloadSpeed());

                                Utils.scheduler.runTaskLater(ZUtils.plugin, () -> {
                                    cItem.swapModel(p, item, false);
                                }, cItem.getReloadSpeed());

                                int ammo = cItem.getAmmo();
                                int preReloadAmount = cItem.getReloadAmount() > 0 ? cItem.getReloadAmount() : cItem.getMagSize() - ammo;
                                if (preReloadAmount > cItem.getExtraAmmo())
                                    preReloadAmount = cItem.getExtraAmmo();
                                int reloadAmount = preReloadAmount;

                                // SOUNDS

                                new BukkitRunnable() {
                                    int i = 0;

                                    public void run() {
                                        double iterationAmount = (double) reloadAmount / (double) cItem.getReloadSpeed();
                                        if ((int) iterationAmount < 1) {
                                            iterationAmount++;
                                            iterationAmount = iterationAmount * 1.5;
                                        }
                                        //p.sendMessage(iterationAmount + "");
                                        cItem.setAmmo(((cItem.getAmmo() + ((int) iterationAmount) > cItem.getMagSize()) ? cItem.getMagSize() : cItem.getAmmo() + (int) iterationAmount));
                                        i++;

                                        if (cItem.getAmmo() == cItem.getMagSize() || i == reloadAmount)
                                            cancel();
                                        if (cItem.getAmmo() == cItem.getMagSize() && !isPump)
                                            cancel();
                                        if (cItem.getAmmo() == cItem.getMagSize())
                                            reloadCooldown.remove(item);
                                    }
                                }.runTaskTimer(ZUtils.plugin, 0L, 0);
                                cItem.setExtraAmmo(cItem.getExtraAmmo() - preReloadAmount <= 0 ? 0 : cItem.getExtraAmmo() - preReloadAmount);
                            }
                        }.runTaskTimer(ZUtils.plugin, 0L, cItem.getReloadSpeed() + iterationDelay);
                    } else {
                        p.sendMessage(ColoredText.t("&c&oOUT OF AMMO"));
                        // SOUNDS
                    }
                }
                cItem.swapModel(p, item, true);
                return true;
            }

            if (cItem != null && cItem.getAmmo() == 0) {
                p.playSound(p, Sound.BLOCK_NOTE_BLOCK_HAT, 0.4F, 0.6F);
                p.playSound(p, Sound.BLOCK_NOTE_BLOCK_HAT, 0.3F, 0.9F);
                return true;
            }

            Location soundLoc = p.getEyeLocation().add(p.getEyeLocation().getDirection().normalize().multiply(0.3));

            switch (key) {
                /** GUNS */
                case "minigun": {
                    boolean isCooldowned = Test.shootGunTest1(p, item, true, true, true);
                    if (!isCooldowned) {
                        playGenericGunSound(soundLoc);
                        Utils.scheduler.runTaskLater(ZUtils.plugin, () -> {
                            playGenericGunSound(soundLoc);
                            Test.shootGunTest1(p, item, true, false, false);
                            Utils.scheduler.runTaskLater(ZUtils.plugin, () -> {
                                playGenericGunSound(soundLoc);
                                Test.shootGunTest1(p, item, true, false, false);
                                Utils.scheduler.runTaskLater(ZUtils.plugin, () -> {
                                    playGenericGunSound(soundLoc);
                                    Test.shootGunTest1(p, item, true, false, false);
                                }, 1);
                            }, 1);
                        }, 1);
                    }
                    break;
                }
                case "shotgun": {
                    boolean isCooldowned = false;
                    for (int i = 0; i <= 10; i++) {
                        if (i == 0)
                            isCooldowned = Test.shootGunTest1(p, item, true, true, true);
                        if (!isCooldowned)
                            playGenericGunSound(soundLoc);
                        if (!isCooldowned && i != 0)
                            Test.shootGunTest1(p, item, false, false, false);
                    }
                    break;
                }
                case "deagle": {
                    boolean playSound = !(Test.shootGunTest1(p, item, true, true, true));
                    if (playSound)
                        playGenericGunSound(soundLoc);
                }
                case "double_shotgun": {
                    boolean playSound = !(Test.shootGunTest1(p, item, true, true, true));
                    if (playSound)
                        playGenericGunSound(soundLoc);
                    break;
                }
                case "grenade_launcher": {
                    boolean playSound = !(Test.shootExplosive1(p, item, true, true, true, false, false, true, true, Material.OCHRE_FROGLIGHT, 4, 12, true));
                    if (playSound)
                        playGenericExplosiveGunSound(soundLoc);
                    break;
                }
                case "lmg": {
                    boolean playSound = !(Test.shootGunTest1(p, item, true, true, true));
                    if (playSound)
                        playGenericGunSound(soundLoc);
                    break;
                }
                case "mk47": {
                    boolean playSound = !(Test.shootGunTest1(p, item, true, true, true));
                    if (playSound)
                        playGenericGunSound(soundLoc);
                    break;
                }
                case "pistol": {
                    boolean playSound = !(Test.shootGunTest1(p, item, true, true, true));
                    if (playSound)
                        playGenericGunSound(soundLoc);
                    break;
                }
                case "revolver": {
                    boolean playSound = !(Test.shootGunTest1(p, item, true, true, true));
                    if (playSound)
                        playGenericGunSound(soundLoc);
                    break;
                }
                case "rpg": {
                    boolean playSound = !(Test.shootExplosive1(p, item, false, false, true, true, true, false, false, null, 3, 10, true));
                    if (playSound)
                        playGenericExplosiveGunSound(soundLoc);
                    break;
                }
                case "smg": {
                    boolean playSound = !(Test.shootGunTest1(p, item, true, true, true));
                    Test.shootGunTest1(p, item, true, false, false);
                    Test.shootGunTest1(p, item, true, false, false);
                    Test.shootGunTest1(p, item, true, false, false);
                    Test.shootGunTest1(p, item, true, false, false);
                    if (playSound)
                        playGenericGunSound(soundLoc);
                    break;
                }
                case "tommy_gun": {
                    boolean playSound = !(Test.shootGunTest1(p, item, true, true, true));
                    if (playSound)
                        playGenericGunSound(soundLoc);
                    break;
                }
                default:
                    return false; // Gun didnt fire
            }
            if (cItem != null)
                cItem.swapModel(p, item, false);
            return true; // Reached end of switch, Gun fired or reloaded
        }

        return false; // Gun didnt fire

    }

    public static void playGenericGunSound(Location loc) {
        Object[] sounds = new Object[]{
                new Object[]{Sound.BLOCK_BAMBOO_WOOD_PLACE, 0.5F, 1F},
                new Object[]{Sound.BLOCK_STONE_PLACE, 0.5F, 2F}
        };
        for (Object var : sounds) {
            Object[] sound = (Object[]) var;
            loc.getWorld().playSound(loc, (Sound) sound[0], (float) sound[1], (float) sound[2]);
        }
    }

    public static void playGenericExplosiveGunSound(Location loc) {
        Object[] sounds = new Object[]{
                new Object[]{Sound.ENTITY_LLAMA_SPIT, 0.1F, 0.5F},
                new Object[]{Sound.ENTITY_LLAMA_SPIT, 0.1F, 1F},
                new Object[]{Sound.ENTITY_LLAMA_EAT, 0.3F, 0.5F},
                new Object[]{Sound.ENTITY_SHULKER_BULLET_HIT, 0.3F, 1.5F},
                new Object[]{Sound.ENTITY_GENERIC_EXPLODE, 0.08F, 2F}
        };
        for (Object var : sounds) {
            Object[] sound = (Object[]) var;
            loc.getWorld().playSound(loc, (Sound) sound[0], (float) sound[1], (float) sound[2]);
        }
    }

}
