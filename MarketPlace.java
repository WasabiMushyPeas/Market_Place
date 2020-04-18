package com.mcdomainname.marketplace;

import commands.BuyCommand;
import commands.SellCommand;
import events.MarketPlaceEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

public final class MarketPlace extends JavaPlugin {

    private Market marketPlace;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new MarketPlaceEvent(this), this);

        System.out.println("Registering sell command.");
        marketPlace = new Market();

        getCommand("sell_item").setExecutor(new SellCommand(this));
        getCommand("buy_item").setExecutor(new BuyCommand(this));


    }






    public void openConfirmMenu(Player player, InventoryClickEvent e) {

        Inventory ConfirmMenu = Bukkit.createInventory(player, 9, ChatColor.DARK_RED + "Confirmation");

        // yes I want to trade
        ItemStack Yes = new ItemStack(Material.EMERALD, 1);
        ItemMeta meta = Yes.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Yes");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.DARK_GREEN + "You want to trade");
        meta.setLore(lore);
        Yes.setItemMeta(meta);
        ConfirmMenu.setItem(0, Yes);

        // no I don't want to trade
        ItemStack No = new ItemStack(Material.BARRIER, 1);
        meta.setDisplayName(ChatColor.RED + "No");
        ArrayList<String> lore1 = new ArrayList<>();
        lore1.add(ChatColor.DARK_RED + "You don't want to trade");
        meta.setLore(lore1);
        No.setItemMeta(meta);
        ConfirmMenu.setItem(8, No);

        // Item that they clicked on
        ItemStack itemTheyClicked = marketPlace.getPosting(e.getSlot()).getItemStack();;
        meta.setDisplayName(ChatColor.RED + "You are purchasing "+ itemTheyClicked.getType().name().toLowerCase());
        ArrayList<String> lore2 = new ArrayList<>();
        lore2.add(ChatColor.GOLD + marketPlace.getPosting(e.getSlot()).getOwner());
        lore2.add(ChatColor.GOLD + String.valueOf(marketPlace.getPosting(e.getSlot()).getQuantityOfDesiredItem()) + " " + marketPlace.getPosting(e.getSlot()).getMaterialDesired());
        meta.setLore(lore2);
        itemTheyClicked.setItemMeta(meta);
        ConfirmMenu.setItem(4, itemTheyClicked);

        player.openInventory(ConfirmMenu);

    }


    public void showMarket(Player player) {

        Inventory marketPostings = Bukkit.createInventory(player, 54, ChatColor.BLACK + "Market Place");


        for (int i = 0; i < marketPlace.getSize(); i++) {
            ArrayList<String> lore = new ArrayList<>();

            ItemStack itemStackPosting = marketPlace.getPosting(i).getItemStack();

            ItemMeta itemToDisplayMeta = itemStackPosting.getItemMeta();
            itemToDisplayMeta.setDisplayName(ChatColor.GREEN + itemStackPosting.getType().name());

            Set<Enchantment> enchantments = itemToDisplayMeta.getEnchants().keySet();
            List<Enchantment> listEnchantments = new ArrayList<>();
            listEnchantments.addAll(enchantments);

            ListIterator<Enchantment> iterator = listEnchantments.listIterator();


            while (iterator.hasNext()) {
                lore.add(ChatColor.AQUA + iterator.next().getKey().getKey());
            }

            lore.add(ChatColor.GOLD + marketPlace.getPosting(i).getOwner());
            lore.add(ChatColor.GOLD + String.valueOf(marketPlace.getPosting(i).getQuantityOfDesiredItem()) + " " + marketPlace.getPosting(i).getMaterialDesired());

            itemToDisplayMeta.setLore(lore);
            itemStackPosting.setItemMeta(itemToDisplayMeta);
            marketPostings.addItem(itemStackPosting);

        }

        player.openInventory(marketPostings);



    }

        public Market getMarket () {
            return marketPlace;
        }


        @Override
        public void onDisable () {
            // Plugin shutdown logic
            System.out.println(ChatColor.LIGHT_PURPLE + "Shutting down the MarketPlace");
        }


}
