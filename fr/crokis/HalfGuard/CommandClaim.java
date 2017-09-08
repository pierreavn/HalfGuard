package fr.crokis.HalfGuard;
import java.util.Hashtable;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandClaim implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(label.equalsIgnoreCase("claim")) {
			if(sender instanceof Player) {
				Player player = (Player) sender;
				if(player.hasPermission("HalfGuard.claim")) {
					if(args.length != 0) { //On vérifie le nombre d'arguments
						Main.usage("claim", sender);
						return true;
					}
					
					//Si le joueur n'a pas de zone définie ...
					if(!Main.zM.zoneDefined(player)) {
						Main.print(ChatColor.RED + "Vous devez d'abord définir une zone (WOOD_PICKAXE)", sender);
						return true;
					}
					
					Hashtable<String, Integer> zone = Main.zM.zoneGet(player);
					
					if(Main.cM.alreadyClaimed(zone)) {
						Main.print(ChatColor.RED + "Au moins un bloc de cette zone appartient déjà à un autre claim.", sender);
						return true;
					}
					
					int zoneID = Main.cM.create(player, zone);
					
					if(zoneID == -1) //Si le claim n'a pas été créé ...
						return true;
					
					Main.printConsole(player.getName() + ": New zone claim #" + zoneID);
					Main.print(ChatColor.GREEN + "Claim créé avec succès !", sender);
				} else {
					Main.print(ChatColor.RED + "Vous ne disposez pas des permissions requises pour exécuter cette commande.", sender);
				}
			} else {
				Main.print(ChatColor.RED + "Vous ne pouvez executer cette commande à partir de la console!", sender);
			}
			return true;
		}
		return false;
	}
}