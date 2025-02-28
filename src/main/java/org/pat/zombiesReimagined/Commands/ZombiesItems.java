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
import org.pat.zombiesReimagined.Utility.CustomItem.Item;
import org.pat.zombiesReimagined.Utility.CustomItem.UseType;

import java.util.ArrayList;
import java.util.List;

public class ZombiesItems implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String cmd, @NotNull String[] args) {

        if (sender instanceof Player p) {
            List<ItemStack> devItems = new ArrayList<>(List.of(new Item[] {
                    new Item(UseType.DEV, "&fSelection Wand", Material.STICK, 1),
                    new Item(UseType.DEV, "&fExecute Task", Material.BLAZE_ROD, 1)
            }));

            List<ItemStack> gunItems = new ArrayList<>(List.of(new Item[] {
                    new Item(UseType.GUN, "&FAssault Rifle", Material.IRON_HOE, 1, 10, 10, 10, 10),
                    new Item(UseType.GUN, "&FSniper", Material.NETHERITE_HOE, 1, 10, 10, 10, 10),
            }));

            Inventory inv = Bukkit.createInventory(p, 27, "Custom items");

            for (int i = 0; i <= 8 && i < devItems.size(); i++) {
                inv.setItem(i, devItems.get(i));
            }

            for (int i = 0; i <= 8 && i < gunItems.size(); i++) {
                inv.setItem(18 + i, gunItems.get(i));
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
