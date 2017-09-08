package fr.crokis.HalfGuard;

import java.io.File;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Main extends JavaPlugin {
	static int i = 0;
	static String pluginName = "HalfGuard";
	static String pluginVersion = "1.1.1";
	static String pluginAuthor = "Pierre AVINAIN (crokis)";
	
	static zonesManager zM = new zonesManager(); // Instanciation de nouveaux managers
	static claimsManager cM = new claimsManager();
	static HGUtils Utils = new HGUtils();
	
	public static void print(String texte, CommandSender sender) {
		sender.sendMessage(ChatColor.AQUA + "[" + pluginName + "] " + ChatColor.RESET + texte);
	}
	
	public static void printConsole(String texte) {
		Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[" + pluginName + "] " + ChatColor.RESET + texte);
	}
	
	static <T> T[] append(T[] arr, T element) {
	    final int N = arr.length;
	    arr = Arrays.copyOf(arr, N + 1);
	    arr[N] = element;
	    return arr;
	}
	
	public static void usage(String command, CommandSender sender) {
		String texte = "";
		switch(command) {
			case "claim":
				texte = "/claim";
				break;
			
			case "secure":
				texte = "/secure <public|private>";
				break;
				
			case "shared":
				texte = "/shared <true|false>";
				break;
				
			case "unclaim":
				texte = "/unclaim";
				break;
				
			case "where":
				texte = "/where";
				break;
				
			case "halfguard":
				texte = "/halfguard";
				break;
				
			case "promote":
				texte = "/promote <player>";
				break;
				
			case "promotelist":
				texte = "/promotelist";
				break;
				
			case "dismiss":
				texte = "/dismiss <player> <blocksOptions: keep|remove>";
				break;
		}
		print(ChatColor.RED + "Utilisation incorrecte de la commande /" + command + "\nSyntaxe: " + texte, sender);
	}
	
	public void onEnable() {
		printConsole(ChatColor.AQUA + pluginName + " - v." + pluginVersion + " by " + pluginAuthor);
		printConsole("Initializing commands ...");
		
		//Initialisation du main
		//Main.chaptersManager.initMain(this);
		
		//Initialisation des commandes
		this.getCommand("claim").setExecutor(new CommandClaim()); //Claim une nouvelle zone
		this.getCommand("where").setExecutor(new CommandWhere()); //Infos sur le claim courant
		this.getCommand("unclaim").setExecutor(new CommandUnclaim()); //Unclaim une zone
		this.getCommand("secure").setExecutor(new CommandSecure()); //Change le mode de sécurisation d'une zone
		this.getCommand("shared").setExecutor(new CommandShared()); //Change le mode de partage d'une zone
		this.getCommand("promote").setExecutor(new CommandPromote()); //Ajoute un utilisateur conjoint
		this.getCommand("promotelist").setExecutor(new CommandPromoteList()); //Liste les utilisateurs conjoints
		this.getCommand("dismiss").setExecutor(new CommandDismiss()); //Supprime un utilisation conjoint
		this.getCommand("halfguard").setExecutor(new CommandHelp());
		
		printConsole("Starting listeners ...");
		//Démarrage des listeners globaux
		this.startGlobalListeners();
		
		printConsole("Loading configuration ...");
		//Chargement config
        try {
            File claimsFolder = new File(getDataFolder() + File.separator + "claims");
            if(!claimsFolder.exists()){
            	claimsFolder.mkdirs();
            }
        } catch(SecurityException e) {
        		printConsole(ChatColor.RED + "Impossible to create 'claims' folder!");
            return;
        }
        
        printConsole("Loading claims ...");
		int countClaims = cM.loadClaims();
		printConsole(countClaims + " claim(s) found.");
        
        printConsole("Successfully enabled.");
	}
	
	public void startGlobalListeners() {
		getServer().getPluginManager().registerEvents(new Listeners(this), this);
	}
}