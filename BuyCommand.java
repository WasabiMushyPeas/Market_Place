package commands;

import com.mcdomainname.marketplace.MarketPlace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BuyCommand implements CommandExecutor {

    MarketPlace plugin;
    public BuyCommand(MarketPlace plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player){
            Player player = (Player) sender;

            plugin.showMarket(player);


        }

        return true;
    }

}
