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
import org.pat.zombiesReimagined.Utility.ItemUtils.Guns;
import org.pat.zombiesReimagined.Utility.ItemUtils.Item;
import org.pat.zombiesReimagined.Utility.ItemUtils.UseType;

import java.util.ArrayList;
import java.util.List;

public class ZombiesItems implements TabExecutor {

    public static List<Item> devItems = new ArrayList<>(List.of(new Item[] {
            new Item(UseType.DEV, "&fSelection Wand", Material.STICK, "selection_wand"),
            new Item(UseType.DEV, "&fExecute Task", Material.BLAZE_ROD, "execution_wand"),
            new Item(UseType.DEV, "&fReset Task", Material.BREEZE_ROD, "reset_wand")
    }));

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String cmd, @NotNull String[] args) {

        if (sender instanceof Player p) {

            Inventory inv = Bukkit.createInventory(p, 27, "Custom items");

            for (int i = 0; i <= 8 && i < devItems.size(); i++) {
                inv.setItem(i, devItems.get(i).getItemStack());
            }

            for (int i = 0; i <= 17 && i < Guns.gunArray.length; i++) {
                ItemStack gunItem = new Item(UseType.GUN, Guns.gunArray[i].getItemStack().getItemMeta().getDisplayName(),
                        Guns.gunArray[i].getItemStack().getType(),
                        Guns.gunArray[i].getKey(),
                        Test.useModels).getItemStack();
                inv.setItem(9 + i, gunItem);
            }

            p.openInventory(inv);

        }

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String cmd, @NotNull String[] args) {
        List<String> arguments = new ArrayList<>();

        return arguments;
    }
}
