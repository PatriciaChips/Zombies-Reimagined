package org.pat.zombiesReimagined.Utility.MapUtils;

import org.bukkit.Material;
import org.pat.zombiesReimagined.Utility.CustomItem.UseType;

import java.awt.*;

public class MapFeature implements Cloneable {

    private FeatureType type;
    private int cost;
    private int cost1;
    private String name;
    private String name1;
    private Material material;
    private boolean isBottomHalf;

    // Constructor for GUN shop
    public MapFeature(FeatureType type, int cost, String gunName, Material gunMat) {
        this.type = type;
        this.cost = cost;
        this.name = gunName;
        this.material = gunMat;
    }

    // Constructor for POWERUP shop
    public MapFeature(FeatureType type, int cost, String powerupName) {
        this.type = type;
        this.cost = cost;
        this.name = powerupName;
    }

    // Constructor for ARMOUR shop
    public MapFeature(FeatureType type, int cost, String armourName, Material armorBaseMat, boolean isBottomHalf) {
        this.type = type;
        this.cost = cost;
        this.name = armourName;
        this.material = armorBaseMat;
        this.isBottomHalf = isBottomHalf;
    }

    // Constructor for LUCKY CHEST
    public MapFeature(FeatureType type, int cost) {
        this.type = type;
        this.cost = cost;
    }

    // Constructor for WINDOW
    public MapFeature(FeatureType type) {
        this.type = type;
    }

    // Constructor for UNLOCK_DOOR
    public MapFeature(FeatureType type, int cost, String doorName1, String doorName2, int cost1) {
        this.type = type;
        this.cost = cost;
        this.cost1 = cost1;
        this.name = doorName1;
        this.name1 = doorName2;
    }

    public MapFeature clone() {
        try {
            return (MapFeature) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public FeatureType getType() {
        return type;
    }

    public int getCost() {
        return cost;
    }

    public int getCost1() {
        return cost1;
    }

    public String getName1() {
        return name1;
    }

    public String getName() {
        return name;
    }

    public Material getMaterial() {
        return material;
    }

    public boolean isBottomHalf() {
        return isBottomHalf;
    }
}
