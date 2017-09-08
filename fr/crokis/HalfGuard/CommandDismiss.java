package fr.crokis.HalfGuard;

import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandDismiss implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(label.equalsIgnoreCase("dismiss")) {
			if(sender instanceof Player) {
				Player player = (Player) sender;
				if(player.hasPermission("HalfGuard.dismiss")) {
					if(args.length != 2) { //On vérifie le nombre d'arguments
						Main.usage("dismiss", sender);
						return true;
					}
					
					int claimID = Main.cM.currentClaim(player);
					if(claimID == -1) {
						Main.print(ChatColor.RED + "Cette zone n'a pas encore été claim.", sender);
						return true;
					}
					if(!player.getName().equals(Main.cM.getOwner(claimID))) {
						Main.print(ChatColor.RED + "Vous ne pouvez renvoyer uniquement sur vos propres zones.", sender);
						return true;
					}
					
					if(player.getName().equals(args[0])) {
						Main.print(ChatColor.RED + "Vous ne pouvez vous renvoyer vous-même."
								+ "\n Utilisez /unclaim pour supprimer la protection de la zone.", sender);
						return true;
					}
					
					Set<String> promoted = Main.cM.promotelist(claimID);
					if(!promoted.contains(args[0])) {
						Main.print(ChatColor.RED + "Le joueur '" + args[0] + "' n'a pas été promu sur ce claim (#"+claimID+").", sender);
						return true;
					}
					
					if(!args[1].equals("keep") && !args[1].equals("remove")) {
						Main.print(ChatColor.RED + "Option de blocs '" + args[1] + "' invalide.", sender);
						Main.usage("dismiss", sender);
						return true;
					}
					
					Main.cM.dismiss(claimID, args[0], args[1]);
					Main.print(ChatColor.GREEN + args[0] + " renvoyé avec succès.", sender);
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