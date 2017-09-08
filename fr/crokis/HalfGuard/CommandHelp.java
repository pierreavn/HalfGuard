package fr.crokis.HalfGuard;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHelp implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(label.equalsIgnoreCase("halfguard") || label.equalsIgnoreCase("i")) {
			if(sender instanceof Player && !sender.hasPermission("HalfGuard.help")) { //On vérifie que, si c'est un joueur, il dispose des permissions
				Main.print(ChatColor.RED + "Vous ne disposez pas des permissions requises pour exécuter cette commande.", sender);
				return true;
			}
			
			//On vérifie le nombre d'arguments et on récupère le joueur
			if(args.length != 0) {
				Main.usage("halfguard", sender);
				return true;
			}
			
			String text = ChatColor.AQUA + Main.pluginName + " - v." + Main.pluginVersion + " by " + Main.pluginAuthor + "\n" + ChatColor.RESET
					+ ChatColor.RED + "/claim" + ChatColor.GOLD + "\n" + ChatColor.RESET
					+ "Permet de définir une nouvelle zone à protéger. Sélectionnez au préalable deux points avec une pioche en bois.\n" + ChatColor.RESET
					
					+ ChatColor.RED + "/unclaim" + ChatColor.GOLD + "\n" + ChatColor.RESET
					+ "Permet de déprotéger une zone. Positionnez-vous dans la zone pour la sélectionner.\n" + ChatColor.RESET
					
					+ ChatColor.RED + "/where" + ChatColor.GOLD + "\n" + ChatColor.RESET
					+ "Permet d'obtenir des informations sur la zone courante. Positionnez-vous dans la zone pour la sélectionner.\n" + ChatColor.RESET
					
					+ ChatColor.RED + "/secure" + ChatColor.GOLD + " <private|public>\n" + ChatColor.RESET
					+ "Permet de modifier le mode de sécurisation d'une zone. Positionnez-vous dans la zone pour la sélectionner.\n" + ChatColor.RESET
					+ " - private: Par défaut. Les portes ne peuvent-être ouvertes que par vous ou un joueur promu.\n" + ChatColor.RESET
					+ " - public: Les portes peuvent-être ouvertes par tout le monde.\n" + ChatColor.RESET
					
					+ ChatColor.RED + "/shared" + ChatColor.GOLD + " <true|false>\n" + ChatColor.RESET
					+ "Permet de modifier le mode de partage d'une zone. Positionnez-vous dans la zone pour la sélectionner.\n" + ChatColor.RESET
					+ " - true: Par défaut. Un joueur promu peut casser les blocs des autres joueurs promus.\n" + ChatColor.RESET
					+ " - false: Un joueur promu ne peut casser uniquement ses blocs.\n" + ChatColor.RESET
					
					+ ChatColor.RED + "/promote" + ChatColor.GOLD + " <joueur>\n" + ChatColor.RESET
					+ "Permet de promouvoir un joueur, qui pourra par la suite ajouter ses propres blocs. Positionnez-vous dans la zone pour la sélectionner.\n" + ChatColor.RESET
			
					+ ChatColor.RED + "/dismiss" + ChatColor.GOLD + " <joueur> <keep|remove>\n" + ChatColor.RESET
					+ "Permet de destituer un joueur de votre zone. Positionnez-vous dans la zone pour la sélectionner.\n" + ChatColor.RESET
					+ "Le second argument permet de choisir que faire des blocs du joueur concerné :\n" + ChatColor.RESET
					+ " - keep: Tous les blocs posés par ce joueur seront conservés, et vous en devenez propriétaire.\n" + ChatColor.RESET
					+ " - remove: Tous les blocs posés par ce joueur seront supprimés.\n" + ChatColor.RESET
					
					+ ChatColor.RED + "/promotelist" + ChatColor.GOLD + "\n" + ChatColor.RESET
					+ "Permet d'obtenir la liste des joueurs promus dans cette zone. Positionnez-vous dans la zone pour la sélectionner.";
			
			Main.print(text, sender);
			return true;
		}
		return false;
	}
}