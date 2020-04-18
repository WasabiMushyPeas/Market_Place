package events;

import com.mcdomainname.marketplace.MarketPlace;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class MarketPlaceEvent implements Listener {

    static int slotToPurchase;
    MarketPlace plugin;
    HashMap<String, Integer> buyersInventoryMap;

    public MarketPlaceEvent(MarketPlace plugin) {
        this.plugin = plugin;
        buyersInventoryMap = new HashMap<>();
    }



    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        //Material itemWanted = null;


        if (e.getView().getTitle().equalsIgnoreCase(ChatColor.BLACK + "Market Place")) {
            if (e.getCurrentItem() != null) {
                slotToPurchase = e.getSlot();
                plugin.openConfirmMenu(player, e);
            } else {
                player.sendMessage(ChatColor.RED + "That is not an item");
            }

            e.setCancelled(true);
        } else if (e.getView().getTitle().equalsIgnoreCase(ChatColor.DARK_RED + "Confirmation")) {
            //Player whoToTradeWith = player.getServer().getPlayer(plugin.getMarket().getPosting(e.getSlot()).getOwner());
            if (e.getCurrentItem() != null) {
                switch (e.getCurrentItem().getType()) {
                    case BARRIER:
                        player.closeInventory();
                        plugin.showMarket(player);
                        break;
                    case EMERALD:
                        //Search the player's inventory for the desired item
                        for (int i = 0; i < player.getInventory().getSize(); i++) {
                            //loop through the entire inventory of the player making the purchase DONE!!!
                            //Check if the itemstack is enchanted
                            if (player.getInventory().getItem(i) != null) {
//
                                if (player.getInventory().getItem(i).getEnchantments().isEmpty()) {
                                    //If it is NOT enchanted merge it into the hashmap.  This will add all non enchanted items of the same name.
                                    buyersInventoryMap.merge(player.getInventory().getItem(i).getType().name(), player.getInventory().getItem(i).getAmount(),
                                            (v1, v2) -> v1 + v2);
                                    //System.out.println(ChatColor.GREEN + "Putting:" + player.getInventory().getItem(i).getAmount() + " " + player.getInventory().getItem(i).getType().name() + " into hashmap.");
                                    //System.out.println(ChatColor.RED + "The HashMap now has:" + buyersInventoryMap.get(player.getInventory().getItem(i).getType().name()) + " items for this key.");
                                }
                            }
                        }
                            //Now that you have a hashmap of all nonenchanted items condensed.  See if they have the item and they have enough of the item

                            if (buyersInventoryMap.get(plugin.getMarket().getPosting(slotToPurchase).getMaterialDesired()) != null) {
                                if (buyersInventoryMap.get(plugin.getMarket().getPosting(slotToPurchase).getMaterialDesired()) >= plugin.getMarket().getPosting(slotToPurchase).getQuantityOfDesiredItem()) {
                                    //If they do remove all of the items from their inventory with that name.
                                    //System.out.println(ChatColor.LIGHT_PURPLE + "Preparing to clear inventory.");
                                    for (int r = 0; r < player.getInventory().getSize(); r++) {
                                        if (player.getInventory().getItem(r) != null) {
                                            if (player.getInventory().getItem(r).getType().name().equalsIgnoreCase(plugin.getMarket().getPosting(slotToPurchase).getMaterialDesired())) {
                                                //System.out.println(ChatColor.LIGHT_PURPLE + "Looping though inventory and removing all instances of the item needed to make the purchase: "+ r + " times.");
                                                player.getInventory().setItem(r, new ItemStack(Material.AIR));
                                            }
                                        }
                                    }

                                    int howMuchThePlayerGetsBack = buyersInventoryMap.get(plugin.getMarket().getPosting(slotToPurchase).getMaterialDesired()) - plugin.getMarket().getPosting(slotToPurchase).getQuantityOfDesiredItem();
                                    //System.out.println(ChatColor.GOLD + "The player get this in change: " + howMuchThePlayerGetsBack);
                                    if (howMuchThePlayerGetsBack > 64){
                                        for(int x = 0; x < howMuchThePlayerGetsBack; x++){
                                            Material theMaterialThePLayerHad = Material.matchMaterial(plugin.getMarket().getPosting(slotToPurchase).getMaterialDesired());
                                            ItemStack itemToGiveBackMore = new ItemStack(theMaterialThePLayerHad, 1);
                                            player.getInventory().addItem(itemToGiveBackMore);
                                            //System.out.println(ChatColor.GOLD + "Gave the player:" + x + " items.");
                                        }

                                    }else{
                                        Material theMaterialThePLayerHad = Material.matchMaterial(plugin.getMarket().getPosting(slotToPurchase).getMaterialDesired());
                                        ItemStack itemToGiveBack = new ItemStack(theMaterialThePLayerHad, howMuchThePlayerGetsBack);
                                        player.getInventory().addItem(itemToGiveBack);
                                    }
                                    //give the other player their asking price
                                    Player whoToTradeWith = player.getServer().getPlayer(plugin.getMarket().getPosting(slotToPurchase).getOwner());
                                    Material theCost = Material.matchMaterial(plugin.getMarket().getPosting(slotToPurchase).getMaterialDesired());

                                    ItemStack cost = new ItemStack(theCost, plugin.getMarket().getPosting(slotToPurchase).getQuantityOfDesiredItem());
                                    whoToTradeWith.getInventory().addItem(cost);

                                    //give the player the item they just paid for
                                    player.getInventory().addItem(plugin.getMarket().getAndRemovePosting(slotToPurchase));
                                    plugin.getMarket().saveMarket();



                                }else{
                                    player.sendMessage("You do not have enough non-enchanted items to purchase the item(s)");
                                }

                            }else{
                                player.sendMessage("You do not have the non-enchanted items in your inventory necessary to purchase the item(s).");
                            }



                        player.closeInventory();
                        buyersInventoryMap.clear();
                        break;
                }
                e.setCancelled(true);
            } else {
                player.sendMessage(ChatColor.RED + "Invalid Selection!");
                e.setCancelled(true);
            }


        }


    }


}



