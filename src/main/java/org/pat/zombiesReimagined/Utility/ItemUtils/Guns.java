package org.pat.zombiesReimagined.Utility.ItemUtils;

import org.bukkit.Material;
import org.bukkit.Sound;
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
            new Item(UseType.GUN, "Minigun", Material.NETHERITE_HOE, "minigun", false, 50, 4, 250, 250, 4, 25, 0),
            new Item(UseType.GUN, "Shotgun", Material.IRON_AXE, "shotgun", false, 10, 18, 6, 50, 11, 5, 1),
            new Item(UseType.GUN, "Deagle", Material.IRON_HOE, "deagle", false, 100, 0, 12, 100, 7, 20, 0),
            new Item(UseType.GUN, "Double Shotgun", Material.DIAMOND_AXE, "double_shotgun", false, 100, 0, 30, 1, 10, 0, 100),
            new Item(UseType.GUN, "Grenade Launcher", Material.NETHERITE_SHOVEL, "grenade_launcher", false, 100, 0, 30, 1, 10, 0, 100),
            new Item(UseType.GUN, "LMG", Material.IRON_PICKAXE, "lmg", false, 100, 0, 30, 1, 10, 0, 100),
            new Item(UseType.GUN, "MK-47", Material.NETHERITE_PICKAXE, "mk47", false, 100, 0, 30, 1, 10, 0, 100),
            new Item(UseType.GUN, "Pistol", Material.WOODEN_HOE, "pistol", false, 100, 0, 30, 1, 10, 0, 100),
            new Item(UseType.GUN, "Rocket Launcher", Material.WOODEN_SHOVEL, "rpg", false, 100, 0, 30, 1, 10, 0, 100),
            new Item(UseType.GUN, "SMG", Material.STONE_HOE, "smg", false, 100, 0, 30, 1, 10, 0, 100),
            new Item(UseType.GUN, "Tommy Gun", Material.DIAMOND_HOE, "tommy_gun", false, 100, 0, 30, 1, 10, 0, 100)
    };

    public static Item getGunFromKey(String key) {
        for (Item cItem : gunArray) {
            if (cItem.getItemStack().getItemMeta().getPersistentDataContainer().get(ZUtils.key, PersistentDataType.STRING).equalsIgnoreCase(key)) {
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
                                int ammo = cItem.getAmmo();
                                int preReloadAmount = cItem.getReloadAmount() > 0 ? cItem.getReloadAmount() : cItem.getMagSize() - ammo;
                                if (preReloadAmount > cItem.getExtraAmmo())
                                    preReloadAmount = cItem.getExtraAmmo();
                                int reloadAmount = preReloadAmount;

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
                        // ADD SOUNDS
                    }
                }
                return true;
            }

            if (cItem != null && cItem.getAmmo() == 0)
                return true;

            switch (key) {
                /** GUNS */
                case "minigun": {
                    boolean isCooldowned = Test.shootGunTest1(p, item, true, true, true);
                    if (!isCooldowned) {
                        Utils.scheduler.runTaskLater(ZUtils.plugin, () -> {
                            Test.shootGunTest1(p, item, true, false, false);
                            Utils.scheduler.runTaskLater(ZUtils.plugin, () -> {
                                Test.shootGunTest1(p, item, true, false, false);
                                Utils.scheduler.runTaskLater(ZUtils.plugin, () -> {
                                    Test.shootGunTest1(p, item, true, false, false);
                                }, 1);
                            }, 1);
                        }, 1);
                    }
                    break;
                }
                case "shotgun": {
                    boolean isCooldowned = false;
                    for (int i = 0; i <= 15; i++) {
                        if (i == 0)
                            isCooldowned = Test.shootGunTest1(p, item, true, true, true);
                        if (!isCooldowned)
                            Test.shootGunTest1(p, item, false, false, false);
                    }
                    break;
                }
                case "deagle":
                    Test.shootGunTest1(p, item, true, true, true);
                case "double_shotgun":
                    Test.shootGunTest1(p, item, true, true, true);
                    break;
                case "grenade_launcher":
                    Test.shootGunTest1(p, item, true, true, true);
                    break;
                case "lmg":
                    Test.shootGunTest1(p, item, true, true, true);
                    break;
                case "mk47":
                    Test.shootGunTest1(p, item, true, true, true);
                    break;
                case "pistol":
                    Test.shootGunTest1(p, item, true, true, true);
                    break;
                case "revolver":
                    Test.shootGunTest1(p, item, true, true, true);
                    break;
                case "rpg":
                    Test.shootGunTest1(p, item, true, true, true);
                    break;
                case "smg":
                    Test.shootGunTest1(p, item, true, true, true);
                    break;
                case "tommy_gun":
                    Test.shootGunTest1(p, item, true, true, true);
                    break;
                default:
                    return false; // Gun didnt fire
            }
            return true; // Reached end of switch, Gun fired or reloaded
        }

        return false; // Gun didnt fire

    }

}
