package org.pat.zombiesReimagined.Utility.MapUtils;

import org.bukkit.Material;

import java.util.HashMap;

public class IndentifiedStructures {

    public static HashMap<Material, MapFeature> gunIdentifiers = new HashMap<>();
    public static HashMap<Material, MapFeature> powerupIndentifiers = new HashMap<>();
    public static HashMap<Material, MapFeature> armourIndentifiers = new HashMap<>();
    public static HashMap<Material, MapFeature> unlockDoorIndentifiers = new HashMap<>();

    static {
        /** GUNS */ //CONCRETE
        gunIdentifiers.put(Material.GRAY_CONCRETE, new MapFeature(FeatureType.GUN, 100, "Assault Rifle", Material.IRON_HOE));
        gunIdentifiers.put(Material.LIGHT_GRAY_CONCRETE, new MapFeature(FeatureType.GUN, 100, "Sniper", Material.NETHERITE_HOE));

        /** POWERUP */ //WOOL
        powerupIndentifiers.put(Material.LIGHT_GRAY_WOOL, new MapFeature(FeatureType.POWERUP, 250, "Fast Revive"));

        /** ARMOUR */ //GLASS
        armourIndentifiers.put(Material.GREEN_STAINED_GLASS, new MapFeature(FeatureType.ARMOR, 500, "Leather Armour", Material.LEATHER, true));

        /** UNLOCK DOOR */ //WOOD
        unlockDoorIndentifiers.put(Material.OAK_LOG, new MapFeature(FeatureType.UNLOCK_DOOR, 500, "Motel", "Garage", 250));
    }

}
