package org.pat.zombiesReimagined.Utility.MapUtils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.pat.zombiesReimagined.Utility.ItemUtils.Item;
import org.pat.zombiesReimagined.Utility.ItemUtils.UseType;
import org.pat.zombiesReimagined.Utility.ZUtils;

import java.util.HashMap;

public class IdentifiedStructures {

    public static HashMap<Material, MapFeature> gunIdentifiers = new HashMap<>();
    public static HashMap<Material, MapFeature> powerupIndentifiers = new HashMap<>();
    public static HashMap<Material, MapFeature> armourIndentifiers = new HashMap<>();
    public static HashMap<Material, MapFeature> unlockDoorIndentifiers = new HashMap<>();

    static {
        /** GUNS */ //CONCRETE //gunIdentifiers.put(Material., new MapFeature(100, 50, ""));
        gunIdentifiers.put(Material.WHITE_CONCRETE, new MapFeature(FeatureType.GUN, 100, 50, "minigun"));
        gunIdentifiers.put(Material.LIGHT_GRAY_CONCRETE, new MapFeature(FeatureType.GUN, 100, 50, "shotgun"));
        gunIdentifiers.put(Material.GRAY_CONCRETE, new MapFeature(FeatureType.GUN, 100, 50, "deagle"));
        gunIdentifiers.put(Material.BLACK_CONCRETE, new MapFeature(FeatureType.GUN, 100, 50, "double_shotgun"));
        gunIdentifiers.put(Material.YELLOW_CONCRETE, new MapFeature(FeatureType.GUN, 100, 50, "grenade_launcher"));
        gunIdentifiers.put(Material.ORANGE_CONCRETE, new MapFeature(FeatureType.GUN, 100, 50, "lmg"));
        gunIdentifiers.put(Material.RED_CONCRETE, new MapFeature(FeatureType.GUN, 100, 50, "mk47"));
        gunIdentifiers.put(Material.BROWN_CONCRETE, new MapFeature(FeatureType.GUN, 100, 50, "pistol"));
        gunIdentifiers.put(Material.GREEN_CONCRETE, new MapFeature(FeatureType.GUN, 100, 50, "rpg"));
        gunIdentifiers.put(Material.LIME_CONCRETE, new MapFeature(FeatureType.GUN, 100, 50, "smg"));
        gunIdentifiers.put(Material.CYAN_CONCRETE, new MapFeature(FeatureType.GUN, 100, 50, "tommy_gun"));
        gunIdentifiers.put(Material.BLUE_CONCRETE, new MapFeature(FeatureType.GUN, 100, 50, "raygun"));

        /** POWERUP */ //WOOL
        powerupIndentifiers.put(Material.LIGHT_GRAY_WOOL, new MapFeature(FeatureType.POWERUP, 250, "Fast Revive"));
        powerupIndentifiers.put(Material.RED_WOOL, new MapFeature(FeatureType.POWERUP, 250, "Fire Bullets"));

        /** ARMOUR */ //GLASS
        armourIndentifiers.put(Material.GREEN_STAINED_GLASS, new MapFeature(FeatureType.ARMOR, 500, "Leather Armour", Material.LEATHER, true));
        armourIndentifiers.put(Material.LIME_STAINED_GLASS, new MapFeature(FeatureType.ARMOR, 500, "Leather Armour", Material.LEATHER, false));
        armourIndentifiers.put(Material.YELLOW_STAINED_GLASS, new MapFeature(FeatureType.ARMOR, 500, "Chain Armour", Material.CHAIN, true));
        armourIndentifiers.put(Material.ORANGE_STAINED_GLASS, new MapFeature(FeatureType.ARMOR, 500, "Chain Armour", Material.CHAIN, false));
        armourIndentifiers.put(Material.BLUE_STAINED_GLASS, new MapFeature(FeatureType.ARMOR, 500, "Gold Armour", Material.GOLD_INGOT, true));
        armourIndentifiers.put(Material.LIGHT_BLUE_STAINED_GLASS, new MapFeature(FeatureType.ARMOR, 500, "Gold Armour", Material.GOLD_INGOT, false));
        armourIndentifiers.put(Material.GRAY_STAINED_GLASS, new MapFeature(FeatureType.ARMOR, 500, "Iron Armour", Material.IRON_INGOT, true));
        armourIndentifiers.put(Material.LIGHT_GRAY_STAINED_GLASS, new MapFeature(FeatureType.ARMOR, 500, "Iron Armour", Material.IRON_INGOT, false));

        /** UNLOCK DOOR */ //WOOD
        unlockDoorIndentifiers.put(Material.OAK_LOG, new MapFeature(FeatureType.UNLOCK_DOOR, 500, "Motel", "Garage", 250));

    }

    public static ItemStack[] convertMaterialToArmour(Material mat) {
        switch (mat) {
            case LEATHER:
                return new ItemStack[]{new ItemStack(Material.LEATHER_BOOTS), new ItemStack(Material.LEATHER_LEGGINGS), new ItemStack(Material.LEATHER_CHESTPLATE), new ItemStack(Material.LEATHER_HELMET)};
            case CHAIN:
                return new ItemStack[]{new ItemStack(Material.CHAINMAIL_BOOTS), new ItemStack(Material.CHAINMAIL_LEGGINGS), new ItemStack(Material.CHAINMAIL_CHESTPLATE), new ItemStack(Material.CHAINMAIL_HELMET)};
            case GOLD_INGOT:
                return new ItemStack[]{new ItemStack(Material.GOLDEN_BOOTS), new ItemStack(Material.GOLDEN_LEGGINGS), new ItemStack(Material.GOLDEN_CHESTPLATE), new ItemStack(Material.GOLDEN_HELMET)};
            case IRON_INGOT:
                return new ItemStack[]{new ItemStack(Material.IRON_BOOTS), new ItemStack(Material.IRON_LEGGINGS), new ItemStack(Material.IRON_CHESTPLATE), new ItemStack(Material.IRON_HELMET)};
            default:
                return new ItemStack[]{};
        }
    }

}
