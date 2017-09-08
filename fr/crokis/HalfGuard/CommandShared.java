package fr.crokis.HalfGuard;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandShared implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(label.equalsIgnoreCase("shared")) {
			if(sender instanceof Player) {
				Player player = (Player) sender;
				if(player.hasPermission("HalfGuard.changeSharedMode")) {
					if(args.length != 1) { //On vérifie le nombre d'arguments
						Main.usage("shared", sender);
						return true;
					}
					
					int claimID = Main.cM.currentClaim(player);
					
					if(claimID == -1) {
						Main.print(ChatColor.RED + "Cette zone n'a pas encore été claim.", sender);
						return true;
					}
					if(!player.getName().equals(Main.cM.getOwner(claimID))) {
						Main.print(ChatColor.RED + "Vous ne pouvez modifier le partage uniquement sur vos propres zones.", sender);
						return true;
					}
					
					switch(args[0]) {
						case "true":
						case "false":
							break;
							
						default:
							Main.print(ChatColor.RED + "Type de partage '" + args[0] + "' inconnu.", sender);
							return true;
					}
					
					Main.cM.setSharedMode(claimID, args[0]);
					Main.print(ChatColor.GREEN + "Partage mise à jour avec succès vers '" + args[0] + "' pour la zone #" + claimID + ".", sender);
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