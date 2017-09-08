package fr.crokis.HalfGuard;

import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandPromote implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(label.equalsIgnoreCase("promote")) {
			if(sender instanceof Player) {
				Player player = (Player) sender;
				if(player.hasPermission("HalfGuard.promote")) {
					if(args.length != 1) { //On vérifie le nombre d'arguments
						Main.usage("promote", sender);
						return true;
					}
					
					int claimID = Main.cM.currentClaim(player);
					if(claimID == -1) {
						Main.print(ChatColor.RED + "Cette zone n'a pas encore été claim.", sender);
						return true;
					}
					if(!player.getName().equals(Main.cM.getOwner(claimID))) {
						Main.print(ChatColor.RED + "Vous ne pouvez promouvoir uniquement sur vos propres zones.", sender);
						return true;
					}
					
					if(player.getName().equals(args[0])) {
						Main.print(ChatColor.RED + "Vous ne pouvez vous promouvoir vous-même.", sender);
						return true;
					}
					
					Set<String> promoted = Main.cM.promotelist(claimID);
					if(promoted.contains(args[0])) {
						Main.print(ChatColor.RED + "Le joueur '" + args[0] + "' a déjà été promu sur ce claim (#"+claimID+").", sender);
						return true;
					}
					
					Main.cM.promote(claimID, args[0]);
					Main.print(ChatColor.GREEN + args[0] + " promu avec succès.", sender);
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