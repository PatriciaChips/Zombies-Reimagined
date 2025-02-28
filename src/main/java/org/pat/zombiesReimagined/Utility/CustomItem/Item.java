package org.pat.zombiesReimagined.Utility.CustomItem;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.pat.pattyEssentialsV3.ColoredText;
import org.pat.zombiesReimagined.Utility.ZUtils;

import java.util.List;

public class Item extends ItemStack {

    // Constructor for DEV item (only name, material, and amount)
    public Item(UseType type, String name, Material material, int amount, String key) {
        super(material, amount);
        setItemMetaData(type, name, material, amount, key, -1, -1, -1, -1); // DEV items don't use the extra properties
    }

    // Constructor for GUN item (name, material, amount, range, bloom, maxMagSize, magSize)
    public Item(UseType type, String name, Material material, int amount, String key, double range, double bloom, int maxMagSize, int magSize) {
        super(material, amount);
        setItemMetaData(type, name, material, amount, key, range, bloom, maxMagSize, magSize); // GUN items use all properties
    }

    // Method to set item metadata based on UseType
    private void setItemMetaData(UseType type, String name, Material material, int amount, String key, double range, double bloom, int maxMagSize, int magSize) {
        ItemMeta meta = this.getItemMeta(); // Get the current ItemMeta (used for properties like name, lore)
        if (meta != null) {
            meta.setDisplayName(ColoredText.t(name)); // Set the custom display name
            meta.getPersistentDataContainer().set(ZUtils.key, PersistentDataType.STRING, key);
            this.setItemMeta(meta); // Apply the ItemMeta to the ItemStack

            // Handle additional metadata based on UseType
            if (type == UseType.GUN) {
                // You can store these properties in ItemMeta or some custom properties
                meta.setLore(List.of("Range: " + range, "Bloom: " + bloom, "Max Magazine Size: " + maxMagSize, "Magazine Size: " + magSize));
                this.setItemMeta(meta); // Apply changes
            }
        }
    }

}
