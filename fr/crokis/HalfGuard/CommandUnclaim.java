package fr.crokis.HalfGuard;

import java.sql.Timestamp;
import java.util.Hashtable;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandUnclaim implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(label.equalsIgnoreCase("unclaim")) {
			if(sender instanceof Player) {
				Player player = (Player) sender;
				if(player.hasPermission("HalfGuard.unclaim")) {
					if(args.length != 0) { //On vérifie le nombre d'arguments
						Main.usage("unclaim", sender);
						return true;
					}
					
					int claimID = Main.cM.currentClaim(player);
					
					if(claimID == -1) {
						Main.print(ChatColor.RED + "Cette zone n'a pas encore été claim.", sender);
						return true;
					}
					if(!player.getName().equals(Main.cM.getOwner(claimID))) {
						Main.print(ChatColor.RED + "Vous ne pouvez unclaim uniquement vos propres zones.", sender);
						return true;
					}
					
					
					int ts = (int) new Timestamp(System.currentTimeMillis()).getTime();

					if(Main.cM.unclaimRequests.containsKey(player.getName()) && Main.cM.unclaimRequests.get(player.getName()).get(0) == claimID && ( (ts - Main.cM.unclaimRequests.get(player.getName()).get(1)) <= 20_000)) { // Si la requete pour unclaim a ete faite dans les 20 secondes...
						//Suppression du fichier et de la liste des claims
						Main.cM.unclaimRequests.remove(player.getName());
						Main.cM.remove(claimID);
						Main.print(ChatColor.GREEN + "Zone unclaim avec succès.", sender);
						Main.printConsole(player.getName() + ": Zone #" + claimID + " unclaim");
						return true;
					}
					
					Hashtable<Integer, Integer> datas = new Hashtable<Integer, Integer>();
					datas.put(0, claimID);
					datas.put(1, ts);
					Main.cM.unclaimRequests.put(player.getName(), datas);
					
					Main.print(ChatColor.GOLD + "Saisissez à nouveau /unclaim pour confirmer l'unclaim de cette zone (#" + claimID + ").", sender);
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