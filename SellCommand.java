package commands;

import com.mcdomainname.marketplace.Market;
import com.mcdomainname.marketplace.MarketPlace;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SellCommand implements TabExecutor {


    MarketPlace plugin;
    Market myMarketplace;

    public SellCommand(MarketPlace plugin) {
        this.plugin = plugin;
        this.myMarketplace = plugin.getMarket();
    }

    HashMap<String, Integer> mapItemNameToIndex = new HashMap<String, Integer>();

    private static SortedMap<String, List<String>> getByPrefix(
            NavigableMap<String, List<String>> myMap,
            String prefix) {
        return myMap.subMap(prefix, prefix + Character.MAX_VALUE);
    }

/*    private static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }*/

    private static boolean isInteger(String stingToCheck){
        try{
            Integer.parseInt(stingToCheck);
            return true;
        }
        catch(NumberFormatException e){

        }
        return false;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;

            ItemStack[] test = p.getInventory().getContents();

            //Check if the command had the necessary parameters
            if (args.length == 3) {
                System.out.println(ChatColor.GREEN + p.getDisplayName() + " entered /sell_item command with these parameters " +
                        args[0] + " " + args[1] + " " + args[2]);

                //Check the parameters to see if they are valid,
                //if valid remove item stack from player's inventory and
                // post the item stack in the market with the requested price
                if(mapItemNameToIndex.containsKey(args[0]) && (Material.matchMaterial(args[1]) != null) && isInteger(args[2])){
                    myMarketplace.addPosting(p.getInventory().getItem(mapItemNameToIndex.get(args[0])), p.getDisplayName(), args[1], Integer.parseInt(args[2]));
                    p.getInventory().setItem(mapItemNameToIndex.get(args[0]), new ItemStack(Material.AIR));

                    myMarketplace.printPostings();
                    myMarketplace.saveMarket();
                }


                //FUTURE if a sale price (Item and quantity, args[0] and args[1]) are not given post the item in the
                //marketplace and allow players to bid to the item.



            } else {
                p.sendMessage("Error: This command takes three arguments! ItemToSell Count WhatYouWant HowMany");
                return false;
            }

        } else {
            System.out.println(ChatColor.RED + "You have to be a player to run this command");
            return false;
        }
        return true;

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        //Offer tab completion for the four arguments for sell_items command.
        if (sender instanceof Player) {
            Player p = (Player) sender;

            //First argument is the item the user owns and wants to sell
            if (args.length == 1) {
                mapItemNameToIndex.clear();
                String uniqueItemName = new String();
                List<String> listPlayersItems = new ArrayList<>();
                for (int i = 0; i < p.getInventory().getSize(); i++) {
                    if (p.getInventory().getItem(i) != null) {
                        if(p.getInventory().getItem(i).getEnchantments().isEmpty()){
                            uniqueItemName = p.getInventory().getItem(i).getType().name() + "_QTY_" + p.getInventory().getItem(i).getAmount()+"_AT_"+i;
                            listPlayersItems.add(uniqueItemName);
                            mapItemNameToIndex.put(uniqueItemName,i);

                            //mapPlayerItems.put(p.getInventory().getItem(i).getType().name(), p.getInventory().getItem(i).getAmount());
                            //mapPlayerItems.merge(p.getInventory().getItem(i).getType().name(), p.getInventory().getItem(i).getAmount(),
                                //(v1, v2) -> v1 + v2);
                        }
                        else{
                            Set<Enchantment> enchantments = p.getInventory().getItem(i).getEnchantments().keySet();
                            List<Enchantment> listEnchantments = new ArrayList<>();
                            listEnchantments.addAll(enchantments);
                            ListIterator<Enchantment> iterator = listEnchantments.listIterator();

                            String echantmentString = new String();

                            int i_echantments = 0;
                            while(iterator.hasNext() && i_echantments < 4){
                                echantmentString = echantmentString + "_" + iterator.next().getKey().getKey().toUpperCase();
                                i_echantments++;
                            }
                            if(i_echantments==0){
                                echantmentString = new String("NO_ENCHANTMENT_FOUND");
                            }
/*                            if(!listEnchantments.isEmpty()){
                                System.out.println("This is the key of the namespace key -----------> " +listEnchantments.get(0).getKey().getKey());
                                echantmentString = listEnchantments.get(0).getKey().getKey() + listEnchantments.get(1).getKey().getKey();

                            }*/
                            uniqueItemName = p.getInventory().getItem(i).getType().name()+echantmentString+"_AT_"+i;
                            listPlayersItems.add(uniqueItemName);
                            mapItemNameToIndex.put(uniqueItemName,i);
                            //mapPlayerItems.put(p.getInventory().getItem(i).getType().name()+echantmentString, p.getInventory().getItem(i).getAmount());
                        }
                    }
                }

                TreeMap<String, List<String>> itemMap = listPlayersItems
                        .stream()
                        .collect(Collectors.groupingBy(String::toString, TreeMap::new, Collectors.toList()));

                if (!args[0].isEmpty()) {
                    List<String> filteredItems = new ArrayList(getByPrefix(itemMap, args[0].toUpperCase()).keySet());
                    return filteredItems;
                }

                return listPlayersItems;
            }


/*
            if (args.length == 2) {
                List<String> quantityOfSaleItem = new ArrayList<>();
                System.out.println("This is args[0]" + args[0]);

*/
/*               System.out.println("Print Map ******************************");
                mapPlayerItems.entrySet().forEach(entry->{
                    System.out.println(entry.getKey() + " " + entry.getValue());
                });
                System.out.println("******************************");*//*


*/
/*                System.out.println("Value of hashmap at key = " + args[0] + " " + mapPlayerItems.get(args[0]));
                if (!mapPlayerItems.isEmpty()) {
                    for (int i = 0; i < mapPlayerItems.get(args[0]); i++) {
                        quantityOfSaleItem.add(Integer.valueOf(i + 1).toString());
                    }

                }
                try {
                    if (isInteger(args[1])) {
                        if (Integer.valueOf(args[1]) > mapPlayerItems.get(args[0])) {
                            p.sendMessage(ChatColor.RED + "YOU DON'T HAVE THAT MUCH TO SELL!");
                            p.sendMessage(ChatColor.RED + "IF YOU PROCEED WITH THE COMMAND, " +
                                    "YOU WILL SELL ALL YOUR OF YOUR ITEM!.");
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Exception in SellCommand.java while trying to check " +
                            "number of goods you are selling.");
                }*//*


                return quantityOfSaleItem;
            }
*/

            //The second argument is what item the seller wants in return.
            else if (args.length == 2) {
                List<String> listOfAllMinecraftItems = Stream.of(Material.values())
                        .map(Enum::name)
                        .collect(Collectors.toList());

                TreeMap<String, List<String>> itemMap = listOfAllMinecraftItems
                        .stream()
                        .collect(Collectors.groupingBy(String::toString, TreeMap::new, Collectors.toList()));

                if (!args[1].isEmpty()) {
                    List<String> filteredItems = new ArrayList(getByPrefix(itemMap, args[1].toUpperCase()).keySet());
                    return filteredItems;
                }


                return listOfAllMinecraftItems;
            }
            //The fourth argument is the quantity of the item the seller wants in return.
            List<String> emptyList = new ArrayList<>();
            return emptyList;
        }
        return null;
    }
}
