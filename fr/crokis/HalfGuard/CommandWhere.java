package fr.crokis.HalfGuard;

import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandWhere implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(label.equalsIgnoreCase("where")) {
			if(sender instanceof Player) {
				Player player = (Player) sender;
				if(player.hasPermission("HalfGuard.where")) {
					if(args.length != 0) { //On vérifie le nombre d'arguments
						Main.usage("where", sender);
						return true;
					}
					
					int claimID = Main.cM.currentClaim(player);
					
					if(claimID == -1) {
						Main.print("Cette zone n'a pas encore été claim.", sender);
						return true;
					}
					
					String promotedOrNot = "";
					Set<String> promoted = Main.cM.promotelist(claimID);
					if(promoted.contains(player.getName())) { //Si le joueur est promu
						promotedOrNot = ChatColor.GREEN + "Vous êtes promu sur cette zone";
					} else {
						promotedOrNot = ChatColor.GOLD + "Vous n'êtes pas promu sur cette zone";
					}
					
					Main.print("Claim #" + claimID + " par " + ChatColor.GOLD + Main.cM.getOwner(claimID) + ChatColor.RESET + "\nSécurisation: " + ChatColor.GOLD + Main.cM.getSecureMode(claimID) + ChatColor.RESET + "\nPartage activé: " + ChatColor.GOLD + Main.cM.getSharedMode(claimID) + "\n" + ChatColor.RESET + promotedOrNot, sender);
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