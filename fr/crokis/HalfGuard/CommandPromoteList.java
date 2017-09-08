package fr.crokis.HalfGuard;

import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandPromoteList implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(label.equalsIgnoreCase("promotelist")) {
			if(sender instanceof Player) {
				Player player = (Player) sender;
				if(player.hasPermission("HalfGuard.promote")) {
					if(args.length != 0) { //On vérifie le nombre d'arguments
						Main.usage("promotelist", sender);
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
					
					Set<String> promoted = Main.cM.promotelist(claimID);
					if(promoted.size() == 0) {
						Main.print("Aucun joueur promu sur ce claim (#" + claimID + ").", sender);
						return true;
					}
					
					String text = "Joueur(s) promu(s) sur ce claim (#" + claimID + ") :";
					for(String pseudo : promoted) {
						text = text + "\n - " + pseudo;
					}
					
					Main.print(text, sender);
					return true;
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