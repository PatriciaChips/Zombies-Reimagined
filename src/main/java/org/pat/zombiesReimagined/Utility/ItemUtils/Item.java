package org.pat.zombiesReimagined.Utility.ItemUtils;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.pat.pattyEssentialsV3.ColoredText;
import org.pat.zombiesReimagined.Utility.ZUtils;

import java.util.List;
import java.util.UUID;

public class Item implements Cloneable {

    private ItemStack item;
    private float range;
    private float bloom;
    private int magSize;
    private int ammo;
    private int extraAmmo;
    private int fireRate; //tick cooldown
    private int reloadSpeed;
    private int reloadAmount;
    private boolean useModel;
    private String key;

    // Constructor for DEV item (only name, material, and amount)
    public Item(UseType type, String name, Material material, String key) {
        this.item = createItemStack(material, 1);
        this.key = key;
        setItemMetaData(type, item, name); // DEV items don't use the extra properties
    }

    // Constructor for GUN item
    public Item(UseType type, String name, Material material, String key, boolean useModel, float range, float bloom, int magSize, int extraAmmo, int fireRate, int reloadSpeed, int reloadAmount) {
        this.range = range;
        this.bloom = bloom;
        this.magSize = magSize;
        this.ammo = magSize;
        this.extraAmmo = extraAmmo;
        this.fireRate = fireRate;
        this.reloadSpeed = reloadSpeed;
        this.reloadAmount = reloadAmount;
        this.useModel = useModel;
        this.key = key;
        this.item = createItemStack(material, 1);
        //setDurability(item);
        setItemMetaData(type, item, name); // GUN items use all properties
    }

    // Default Constructor for GUN item
    public Item(UseType type, String name, Material material, String key, boolean useModel) {
        this.range = 100;
        this.bloom = 0;
        this.magSize = 30;
        this.fireRate = 1;
        this.ammo = magSize;
        this.reloadSpeed = 20;
        this.reloadAmount = 0;
        this.useModel = useModel;
        this.key = key;
        this.item = createItemStack(material, 1);
        //setDurability(item);
        setItemMetaData(type, item, name); // GUN items use all properties
    }

    /** GET */
    public ItemStack getItemStack() {
        return item;
    }

    public float getBloom() {
        return bloom;
    }

    public String getKey() {
        return key;
    }

    public float getRange() {
        return range;
    }

    public int getAmmo() {
        return ammo;
    }

    public int getMagSize() {
        return magSize;
    }

    public int getFireRate() {
        return fireRate;
    }

    public int getReloadSpeed() {
        return reloadSpeed;
    }

    public int getReloadAmount() {
        return reloadAmount;
    }

    public int getExtraAmmo() {
        return extraAmmo;
    }

    /** SET */
    public void setAmmo(int ammo) {
        this.ammo = ammo;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public void setExtraAmmo(int extraAmmo) {
        this.extraAmmo = extraAmmo;
    }

    public void setDurability(ItemStack item) {
        int dura = 0;
        if (ammo > 0 && ammo != magSize)
            dura = ((item.getType().getMaxDurability()/(magSize)) * (magSize-ammo));
        if (ammo == magSize)
            dura = 1;
        if (ammo == 0)
            dura = item.getType().getMaxDurability();
        item.setDurability((short) dura);
        this.setItem(item);
        Guns.registeredGuns.put(item, this);
    }

    /** UTIL */
    private ItemStack createItemStack(Material material, int amount) {
        return new ItemStack(material, amount);
    }

    public void setItemMetaData(UseType type, ItemStack item, String name) {
        ItemMeta meta = item.getItemMeta(); // Get the current ItemMeta (used for properties like name, lore)
        if (meta != null) {
            meta.setDisplayName(ColoredText.t(name)); // Set the custom display name
            meta.getPersistentDataContainer().set(ZUtils.key, PersistentDataType.STRING, key);
            item.setItemMeta(meta); // Apply the ItemMeta to the ItemStack

            if (type == UseType.GUN) {
                if (useModel)
                    meta.setItemModel(new NamespacedKey("guns", key));
                meta.setLore(List.of("Range: " + range, "Bloom: " + bloom, "Mag size: " + magSize, "Firerate: " + fireRate));
                Multimap<Attribute, AttributeModifier> modifiers = ArrayListMultimap.create();
                modifiers.put(Attribute.ATTACK_SPEED, new AttributeModifier(new NamespacedKey("e", "a"), 0, AttributeModifier.Operation.MULTIPLY_SCALAR_1));
                meta.setAttributeModifiers(modifiers);
                item.setItemMeta(meta); // Apply changes
            }
        }
        this.item = item;
    }

    public static Item getItem(ItemStack item) {
        if (Guns.registeredGuns.containsKey(item)) {
            return Guns.registeredGuns.get(item); // Return THIS for itemstack
        } else {
            String key = item.getItemMeta().getPersistentDataContainer().get(ZUtils.key, PersistentDataType.STRING);
            Guns.registeredGuns.put(item, Guns.getGunFromKey(key).clone());
            return Guns.registeredGuns.get(item); // THIS not found, clone from gunArray
        }
    }

    @Override
    protected Item clone() {
        try {
            return (Item) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
