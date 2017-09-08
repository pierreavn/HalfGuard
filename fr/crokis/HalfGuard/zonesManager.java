package fr.crokis.HalfGuard;
//import java.util.Enumeration;
import java.util.Hashtable;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;

public class zonesManager {
	protected Hashtable<String, Hashtable<String, Integer>> zones = new Hashtable<String, Hashtable<String, Integer>>();
	
	// Indique si le joueur a commencé la selection d'une zone
	public boolean zoneExist(Player player) { return zones.containsKey(player.getName()); }
	
	// Indique si le joueur a terminé la selection d'une zone
	public boolean zoneDefined(Player player) {
		if(!zoneExist(player))
			return false;

		Hashtable<String, Integer> zone = zones.get(player.getName());
		if(!zone.containsKey("X1") || !zone.containsKey("X2"))
			return false;

		return true;
	}
	
	//Obtention de la zone d'un joueur
	public Hashtable<String, Integer> zoneGet(Player player) {
		if(!zoneExist(player))
			return null;
		return zones.get(player.getName());
	}
	
	// Gestion des zones
	public void zoneSet(Player player, int X, int Y, int Z, Action action) {
		Hashtable<String, Integer> zone = new Hashtable<String, Integer>();
		
		if(this.zoneExist(player)) { //Si le joueur a déjà une zone, on la récupère pour la modifier
			zone = zones.get(player.getName());
		}

		if(action == Action.LEFT_CLICK_BLOCK) {
			zone.put("X1", X);
			zone.put("Y1", Y);
			zone.put("Z1", Z);
		} else if(action == Action.RIGHT_CLICK_BLOCK) {
			zone.put("X2", X);
			zone.put("Y2", Y);
			zone.put("Z2", Z);
		}
		
		zones.put(player.getName(), zone);
	}
}
