package org.pat.zombiesReimagined.Utility.MapUtils;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.pat.pattyEssentialsV3.ColoredText;
import org.pat.zombiesReimagined.Commands.Test;
import org.pat.zombiesReimagined.Utility.ItemUtils.Guns;
import org.pat.zombiesReimagined.Utility.ZUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class MapFeature implements Cloneable {

    public static HashMap<MapFeature, Location> storedStructures = new HashMap<>();
    public static HashSet<Player> iteratedStructures = new HashSet<>();

    private FeatureType type;
    private int cost;
    private int cost1;
    private String name;
    private String name1;
    private Material material;
    private boolean isBottomHalf;
    private String key;
    private List<Block> extraBlocks;
    private List<Entity> structureEntities;
    private List<Entity> preHiddenEntities;
    private List<Entity> toBEHiddenEntities;
    private List<Entity> packEnabledEntities;
    private List<Player> containedPlayers;
    private HashMap<Player, Long> containedTimedPlayers;
    private HashMap<Player, ItemStack> containedPlayersGun;
    private Location loc;

    // Constructor for GUN shop
    public MapFeature(FeatureType gun, int gunCost, int ammoCost, String key) {
        ItemStack item = Guns.getGunFromKey(key).getItemStack();
        this.type = gun;
        this.cost = gunCost;
        this.cost1 = ammoCost;
        this.name = item.getItemMeta().getDisplayName();
        this.material = item.getType();
        this.key = key;
        this.extraBlocks = new ArrayList<>();
        this.structureEntities = new ArrayList<>();
        this.preHiddenEntities = new ArrayList<>();
        this.toBEHiddenEntities = new ArrayList<>();
        this.packEnabledEntities = new ArrayList<>();
        this.containedPlayers = new ArrayList<>();
    }

    // Constructor for POWERUP shop
    public MapFeature(FeatureType powerup, int cost, String powerupName) {
        this.type = powerup;
        this.cost = cost;
        this.name = powerupName;
        this.structureEntities = new ArrayList<>();
        this.preHiddenEntities = new ArrayList<>();
        this.toBEHiddenEntities = new ArrayList<>();
        this.containedPlayers = new ArrayList<>();
    }

    // Constructor for ARMOUR shop
    public MapFeature(FeatureType armour, int cost, String armourName, Material armorBaseMat, boolean isBottomHalf) {
        this.type = armour;
        this.cost = cost;
        this.name = armourName;
        this.material = armorBaseMat;
        this.isBottomHalf = isBottomHalf;
        this.extraBlocks = new ArrayList<>();
        this.structureEntities = new ArrayList<>();
        this.preHiddenEntities = new ArrayList<>();
        this.toBEHiddenEntities = new ArrayList<>();
        this.containedPlayers = new ArrayList<>();
    }

    // Constructor for LUCKY CHEST
    public MapFeature(FeatureType lucky, int cost) {
        this.type = lucky;
        this.cost = cost;
        this.name = "Lucky Chest";
        this.structureEntities = new ArrayList<>();
        this.containedTimedPlayers = new HashMap<>();
        this.preHiddenEntities = new ArrayList<>();
        this.toBEHiddenEntities = new ArrayList<>();
        this.extraBlocks = new ArrayList<>();
        this.packEnabledEntities = new ArrayList<>();
        this.containedPlayersGun = new HashMap<>();
    }

    // Constructor for WINDOW
    public MapFeature(FeatureType window) {
        this.type = window;
        this.structureEntities = new ArrayList<>();
    }

    // Constructor for UNLOCK_DOOR
    public MapFeature(FeatureType door, int cost, String doorName1, String doorName2, int cost1) {
        this.type = door;
        this.cost = cost;
        this.cost1 = cost1;
        this.name = doorName1;
        this.name1 = doorName2;
        this.extraBlocks = new ArrayList<>();
        this.structureEntities = new ArrayList<>();
    }

    public MapFeature clone() {
        try {
            return (MapFeature) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * GET METHODS
     */
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

    public String getKey() {
        return key;
    }

    public List<Block> getExtraBlocks() {
        return extraBlocks;
    }

    public List<Entity> getStructureEntities() {
        return structureEntities;
    }

    public List<Entity> getPreHiddenEntities() {
        return preHiddenEntities;
    }

    public List<Entity> getToBEHiddenEntities() {
        return toBEHiddenEntities;
    }

    public List<Entity> getPackEnabledEntities() {
        return packEnabledEntities;
    }

    public boolean containsPlayer(Player p) {
        return containedPlayers.contains(p);
    }

    public long containsTimedPlayer(Player p) {
        if (containedTimedPlayers.containsKey(p))
            return containedTimedPlayers.get(p);
        return 0;
    }

    public boolean playerHasContainedGun(Player p) {
        return containedPlayersGun.containsKey(p);
    }

    public HashMap<Player, Long> getContainedTimedPlayers() {
        return containedTimedPlayers;
    }

    public ItemStack getContainedPlayersGun(Player p) {
        return containedPlayersGun.get(p);
    }

    public Location getLoc() {
        return loc;
    }

    /**
     * SET METHODS
     */
    public void setExtraBlocks(List<Block> extraBlocks) {
        this.extraBlocks = extraBlocks;
    }

    public void setStructureEntities(List<Entity> structureEntities) {
        this.structureEntities.addAll(structureEntities);
    }

    public void setStructureEntities(Entity entity) {
        this.structureEntities.add(entity);
    }

    public void setPreHiddenEntities(List<Entity> preHiddenEntities) {
        this.preHiddenEntities.addAll(preHiddenEntities);
    }

    public void setPreHiddenEntities(Entity entity) {
        this.preHiddenEntities.add(entity);
    }

    public void setToBEHiddenEntities(List<Entity> toBEHiddenEntities) {
        this.toBEHiddenEntities.addAll(toBEHiddenEntities);
    }

    public void setToBEHiddenEntities(Entity entity) {
        this.toBEHiddenEntities.add(entity);
    }

    public void setPackEnabledEntities(List<Entity> packEnabledEntities) {
        this.packEnabledEntities = packEnabledEntities;
    }

    public void addContainedPlayers(Player p) {
        if (p != null && containedPlayers != null)
            containedPlayers.add(p);
    }

    public void clearContainedPlayers() {
        if (containedPlayers != null)
            containedPlayers.clear();
    }

    public void addContainedTimedPlayers(Player p, long value) {
        if (p != null && containedTimedPlayers != null)
            containedTimedPlayers.put(p, value);
    }

    public void clearContainedTimedPlayers() {
        if (containedTimedPlayers != null)
            containedTimedPlayers.clear();
    }

    public void addGunContainedPlayer(Player p, ItemStack value) {
        if (p != null && containedPlayersGun != null)
            containedPlayersGun.put(p, value);
    }

    public void clearGunContainedPlayers() {
        if (containedPlayersGun != null)
            containedPlayersGun.clear();
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public void setLoc(Location loc) {
        this.loc = loc;
    }

    /**
     * UTILITY METHODS
     */
    public void statusVisibilitySwap(boolean isGlobal) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (containedPlayers != null && containedPlayers.contains(player) || isGlobal) {
                for (Entity entity : toBEHiddenEntities) {
                    player.hideEntity(ZUtils.plugin, entity);
                }
                for (Entity entity : preHiddenEntities) {
                    player.showEntity(ZUtils.plugin, entity);
                    if (entity instanceof ItemDisplay) {
                        if (Test.useModels) {
                            if (packEnabledEntities != null && !packEnabledEntities.contains(entity)) {
                                player.hideEntity(ZUtils.plugin, entity);
                            }
                        } else {
                            if (packEnabledEntities != null && packEnabledEntities.contains(entity)) {
                                player.hideEntity(ZUtils.plugin, entity);
                            }
                        }
                    }
                }
            } else {
                for (Entity entity : toBEHiddenEntities) {
                    player.showEntity(ZUtils.plugin, entity);
                    if (entity instanceof ItemDisplay) {
                        if (Test.useModels) {
                            if (packEnabledEntities != null && !packEnabledEntities.contains(entity)) {
                                player.hideEntity(ZUtils.plugin, entity);
                            }
                        } else {
                            if (packEnabledEntities != null && packEnabledEntities.contains(entity)) {
                                player.hideEntity(ZUtils.plugin, entity);
                            }
                        }
                    }
                }
                for (Entity entity : preHiddenEntities) {
                    player.hideEntity(ZUtils.plugin, entity);
                }
            }
        }
    }

    public boolean fillBlankWindowBlocks() {
        boolean changedBlock = false;
        if (type == FeatureType.WINDOW) {
            for (Block block1 : extraBlocks) {
                Material checkMat = block1.getLocation().getBlock().getType();
                if (!Tag.SLABS.isTagged(material))
                    this.material = Material.OAK_SLAB;
                if (checkMat != material) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.sendBlockChange(block1.getLocation(), Material.BARRIER.createBlockData());
                    }
                    changedBlock = true;
                }
            }
        }
        return changedBlock;
    }

    public static boolean isMaterialSafe(Material mat) {
        switch (mat) {
            case AIR, REPEATER, COMPARATOR, TORCH, SOUL_TORCH, CHAIN, END_ROD, LIGHTNING_ROD, LADDER, FLOWER_POT,
                 BREWING_STAND, LIGHT, MOSS_CARPET, PALE_MOSS_CARPET:
                return true;
        }

        if (Tag.FLOWERS.isTagged(mat)
                || Tag.SMALL_FLOWERS.isTagged(mat)
                || Tag.REPLACEABLE.isTagged(mat)
                || Tag.BUTTONS.isTagged(mat)
                || Tag.ALL_SIGNS.isTagged(mat)
                || Tag.SLABS.isTagged(mat)
                || Tag.STAIRS.isTagged(mat)
                || Tag.WOOL_CARPETS.isTagged(mat))
            return true;

        return false;
    }
}
