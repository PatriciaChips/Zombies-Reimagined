package org.pat.zombiesReimagined.Utility.ItemUtils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.pat.pattyEssentialsV3.Utils;
import org.pat.zombiesReimagined.Commands.Test;
import org.pat.zombiesReimagined.Utility.ZUtils;

public class Guns {

    public static ItemStack[] gunArray = new ItemStack[]{
            new Item(UseType.GUN, "Minigun", Material.NETHERITE_HOE, 1, "minigun", false),
            new Item(UseType.GUN, "Shotgun", Material.IRON_AXE, 1, "shotgun", false),
            new Item(UseType.GUN, "Deagle", Material.IRON_HOE, 1, "deagle", false),
            new Item(UseType.GUN, "Double Shotgun", Material.DIAMOND_AXE, 1, "double_shotgun", false),
            new Item(UseType.GUN, "Grenade Launcher", Material.NETHERITE_SHOVEL, 1, "grenade_launcher", false),
            new Item(UseType.GUN, "LMG", Material.IRON_PICKAXE, 1, "lmg", false),
            new Item(UseType.GUN, "MK-47", Material.NETHERITE_PICKAXE, 1, "mk47", false),
            new Item(UseType.GUN, "Pistol", Material.WOODEN_HOE, 1, "pistol", false),
            new Item(UseType.GUN, "Rocket Launcher", Material.WOODEN_SHOVEL, 1, "rpg", false),
            new Item(UseType.GUN, "SMG", Material.STONE_HOE, 1, "smg", false),
            new Item(UseType.GUN, "Tommy Gun", Material.DIAMOND_HOE, 1, "tommy_gun", false)
    };

    public static ItemStack getGunFromKey(String key) {
        for (ItemStack item : gunArray) {
            if (item.getItemMeta().getPersistentDataContainer().get(ZUtils.key, PersistentDataType.STRING).equalsIgnoreCase(key)) {
                return item;
            }
        }
        return null;
    }

    public static boolean gunInteract(PlayerInteractEvent e, Player p, String key) {

        switch (key) {
            /** GUNS */
            case "minigun":
                Test.shootGunTest1(p, 5, true);
                Utils.scheduler.runTaskLater(ZUtils.plugin, () -> {
                    Test.shootGunTest1(p, 5, false);
                    Utils.scheduler.runTaskLater(ZUtils.plugin, () -> {
                        Test.shootGunTest1(p, 5, false);
                        Utils.scheduler.runTaskLater(ZUtils.plugin, () -> {
                            Test.shootGunTest1(p, 5, false);
                        }, 1);
                    }, 1);
                }, 1);
                break;
            case "shotgun":
                for (int i = 0; i <= 15; i++) {
                    if (i == 0)
                        Test.shootGunTest1(p, 40, true);
                    Test.shootGunTest1(p, 40, false);
                }
                break;
            case "deagle":
                Test.shootGunTest1(p, 0, true);
                break;
            case "double_shotgun":
                Test.shootGunTest1(p, 0, true);
                break;
            case "grenade_launcher":
                Test.shootGunTest1(p, 0, true);
                break;
            case "lmg":
                Test.shootGunTest1(p, 0, true);
                break;
            case "mk47":
                Test.shootGunTest1(p, 0, true);
                break;
            case "pistol":
                Test.shootGunTest1(p, 0, true);
                break;
            case "revolver":
                Test.shootGunTest1(p, 0, true);
                break;
            case "rpg":
                Test.shootGunTest1(p, 0, true);
                break;
            case "smg":
                Test.shootGunTest1(p, 0, true);
                break;
            case "tommy_gun":
                Test.shootGunTest1(p, 0, true);
                break;
            default:
                return false;
        }

        return true;

    }

}
