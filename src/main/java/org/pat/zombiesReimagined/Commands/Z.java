package org.pat.zombiesReimagined.Commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pat.pattyEssentialsV3.Utils;
import org.pat.zombiesReimagined.Utility.ItemUtils.Guns;
import org.pat.zombiesReimagined.Utility.ItemUtils.Item;
import org.pat.zombiesReimagined.Utility.ItemUtils.UseType;
import org.pat.zombiesReimagined.Utility.MapUtils.IdentifiedStructures;
import org.pat.zombiesReimagined.Utility.MapUtils.MapFeature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Z implements TabExecutor {

    public static List<Item> devItems = new ArrayList<>(List.of(new Item[]{
            new Item(UseType.DEV, "&fSelection Wand", Material.STICK, "selection_wand"),
            new Item(UseType.DEV, "&fExecute Task", Material.BLAZE_ROD, "execution_wand"),
            new Item(UseType.DEV, "&fReset Task", Material.BREEZE_ROD, "reset_wand")
    }));

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String cmd, @NotNull String[] args) {

        if (sender instanceof Player p) {

            if (args.length > 0) {
                switch (args[0].toLowerCase()) {
                    case "items": {
                        Inventory itemInv = Bukkit.createInventory(p, 27, "Custom items");

                        for (int i = 0; i <= 8 && i < devItems.size(); i++) {
                            itemInv.setItem(i, devItems.get(i).getItemStack());
                        }

                        for (int i = 0; i <= 17 && i < Guns.gunArray.length; i++) {
                            ItemStack gunItem = new Item(UseType.GUN, Guns.gunArray[i].getItemStack().getItemMeta().getDisplayName(),
                                    Guns.gunArray[i].getItemStack().getType(),
                                    Guns.gunArray[i].getKey(),
                                    Test.useModels).getItemStack();
                            itemInv.setItem(9 + i, gunItem);
                        }

                        p.openInventory(itemInv);
                        break;
                    }
                    case "i_struct": {
                        Inventory iInv = Bukkit.createInventory(p, 54, "Identified structures");

                        int i = 0;
                        for (var v : IdentifiedStructures.powerupIndentifiers.entrySet()) {
                            Material mat = v.getKey();
                            MapFeature feature = v.getValue();
                            List<String> lore = new ArrayList<>();
                            iInv.setItem(i, Utils.i.createItemstack(feature.getName(), mat, 1, lore, null, false, false));
                            i++;
                        }
                        i = 9;

                        for (var v : IdentifiedStructures.gunIdentifiers.entrySet()) {
                            Material mat = v.getKey();
                            MapFeature feature = v.getValue();
                            List<String> lore = new ArrayList<>();
                            iInv.setItem(i, Utils.i.createItemstack(feature.getName(), mat, 1, lore, null, false, false));
                            i++;
                        }
                        i = 27;

                        for (var v : IdentifiedStructures.armourIndentifiers.entrySet()) {
                            Material mat = v.getKey();
                            MapFeature feature = v.getValue();
                            List<String> lore = new ArrayList<>();
                            iInv.setItem(i, Utils.i.createItemstack(feature.getName(), mat, 1, lore, null, false, false));
                            i++;
                        }
                        i = 36;

                        for (var v : IdentifiedStructures.unlockDoorIndentifiers.entrySet()) {
                            Material mat = v.getKey();
                            MapFeature feature = v.getValue();
                            List<String> lore = new ArrayList<>();
                            iInv.setItem(i, Utils.i.createItemstack(feature.getName(), mat, 1, lore, null, false, false));
                            i++;
                        }

                        p.openInventory(iInv);
                        break;
                    }
                    case "r_struct": {
                        int size = 9;
                        if (MapFeature.storedStructures.size() > 9)
                            size = 18;
                        if (MapFeature.storedStructures.size() > 18)
                            size = 27;
                        if (MapFeature.storedStructures.size() > 27)
                            size = 36;
                        if (MapFeature.storedStructures.size() > 36)
                            size = 45;
                        if (MapFeature.storedStructures.size() > 45)
                            size = 54;
                        Inventory rInv = Bukkit.createInventory(p, size, "Registered structures");

                        MapFeature[] mFeature = MapFeature.storedStructures.keySet().toArray(new MapFeature[0]);

                        Arrays.sort(mFeature, Comparator.comparing(item -> item.getType().toString()));

                        int i = 0;
                        for (MapFeature feature : mFeature) {
                            List<String> lore = new ArrayList<>();
                            //lore.add("Location: x" + v.getValue().getX() + " y" + v.getValue().getY() + " z" + v.getValue().getZ());
                            //lore.add("Cost: $" + feature.getCost());
                            Material material = feature.getMaterial();
                            if (material == null) {
                                switch (feature.getType()) {
                                    case WINDOW:
                                        material = Material.GLASS_PANE;
                                        break;
                                    case POWERUP:
                                        material = Material.BEACON;
                                        break;
                                    case CHEST:
                                        material = Material.CHEST;
                                        break;
                                    case GUN:
                                        material = Material.IRON_HOE;
                                        break;
                                    case ARMOR:
                                        material = Material.NETHERITE_HELMET;
                                        break;
                                    case UNLOCK_DOOR:
                                        material = Material.OAK_PLANKS;
                                        break;
                                }
                            }
                            rInv.setItem(i, Utils.i.createItemstack(feature.getType().toString(), material, 1, lore, null, false, false));
                            i++;
                        }

                        p.openInventory(rInv);
                        break;
                    }
                }
            }

        }

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String cmd, @NotNull String[] args) {
        List<String> arguments = new ArrayList<>();

        if (args.length == 1) {
            arguments.add("items");
            arguments.add("i_struct");
            arguments.add("r_struct");
        }

        return arguments;
    }
}
