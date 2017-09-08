package fr.crokis.HalfGuard;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.jnbt.*;

public class claimsManager {
	protected Hashtable<Integer, Hashtable<String, Integer>> claims = new Hashtable<Integer, Hashtable<String, Integer>>();
	protected Hashtable<String, Hashtable<Integer, Integer>> unclaimRequests = new Hashtable<String, Hashtable<Integer, Integer>>();
	protected ArrayList<BlockState> blocksToDelete = new ArrayList<BlockState>();
	
	public int loadClaims() { //Charge les claims à partir des fichiers .NBT
		File zonesFolder = new File(Bukkit.getPluginManager().getPlugin("HalfGuard").getDataFolder() + File.separator + "claims");
		for(String fileNBT : zonesFolder.list()) {
			try {
	            int claimID = Integer.parseInt(fileNBT.replaceAll(".nbt", ""));
	            // S'il n'y a pas eu d'exception jusqu'ici, c'est qu'il semble bien que ce soit un claim
	            
	            //On vérifie que c'est la bonne version, sinon on le mets à jour
	            String version = getVersion(claimID);
	            if(!version.equals(Main.pluginVersion))
	            		updateNBT(claimID, version);
	            
	            Map<String, Tag> tNBT = getFromID(claimID);
	            
	            // On crée un nouveau claim
	            Hashtable<String, Integer> claim = new Hashtable<String, Integer>();
	            // On récupère les coordonnées du NBT ...
	            ListTag tloc1 = (ListTag) tNBT.get("loc1");
	            ListTag tloc2 = (ListTag) tNBT.get("loc2");
	            
	            claim.put("X1", (Integer) tloc1.getValue().get(0).getValue());
				claim.put("Y1", (Integer) tloc1.getValue().get(1).getValue());
				claim.put("Z1", (Integer) tloc1.getValue().get(2).getValue());
				claim.put("X2", (Integer) tloc2.getValue().get(0).getValue());
				claim.put("Y2", (Integer) tloc2.getValue().get(1).getValue());
				claim.put("Z2", (Integer) tloc2.getValue().get(2).getValue());
				
	            // Et on l'enregistre
	            claims.put(claimID, claim);
	        } catch(Exception e) { }
		}
		return claims.size();
	}
	
	public void updateNBT (int claimID, String version) {
		switch(version) {
			case "1.0.1":
			case "1.1.0":
				setSharedMode(claimID, "true");
				break;
				
			default:
				return;
		}
		setVersion(claimID, Main.pluginVersion); //On met a jour le no. de version
	}
	
	public Set<String> promotelist(int claimID) { return Main.Utils.copy((CompoundTag) getFromID(claimID).get("promoted")).keySet(); }
	
	public Map<String, Tag> getFromID(int claimID) {
		try {
			File zonesFolder = new File(Bukkit.getPluginManager().getPlugin("HalfGuard").getDataFolder() + File.separator + "claims");
            File claimFile = new File(zonesFolder + File.separator + claimID + ".nbt");
            
            if(!claimFile.exists()) //Si le fichier n'existe pas ...
            		return null;
            
            NBTInputStream iNBT = new NBTInputStream(new FileInputStream(claimFile));
            CompoundTag tNBT = (CompoundTag) iNBT.readTag();
            iNBT.close();
            return Main.Utils.copy(tNBT);
        } catch(Exception e) { return null; }
	}
	
	public int getIDFromPos(Location loc) {
		Set<Integer> keys = this.claims.keySet();
	    Iterator<Integer> itr = keys.iterator();
	    
	    int X = (int) Math.floor(loc.getX());
	    int Y = (int) Math.floor(loc.getY());
	    int Z = (int) Math.floor(loc.getZ());

	    while (itr.hasNext()) { 
	       int claimID = itr.next(); //Key
	       Hashtable<String, Integer> tmpZ = this.claims.get(claimID); //Value
	       
	       int Xm = Math.min(tmpZ.get("X1"), tmpZ.get("X2"));
	       int XM = Math.max(tmpZ.get("X1"), tmpZ.get("X2"));
	       int Ym = Math.min(tmpZ.get("Y1"), tmpZ.get("Y2"));
	       int YM = Math.max(tmpZ.get("Y1"), tmpZ.get("Y2"));
	       int Zm = Math.min(tmpZ.get("Z1"), tmpZ.get("Z2"));
	       int ZM = Math.max(tmpZ.get("Z1"), tmpZ.get("Z2"));

		   if(X >= Xm && X <= XM && Y >= Ym && Y <= YM && Z >= Zm && Z <= ZM && getWorld(claimID).equals(loc.getWorld().getName()))
			   return claimID;
	    } 
		return -1;
	}
	
	public boolean isInClaim(Location loc, int claimID) {
		int supposedClaim = getIDFromPos(loc);
		return (supposedClaim == claimID);
	}
	
	public boolean isPromoted(int claimID, String player) {
		Set<String> promoted = promotelist(claimID);
		if(promoted.contains(player))
			return true;
		return false;
	}
	
	public boolean allowedPos(Player player, String event, Location loc) {
		int claimID = Main.cM.getIDFromPos(loc);
		if(claimID == -1)
			return true;
		
		int ifOwner = 1;
		int ifPromoted = 1;
		int ifPublic = 0;
		switch(event) {
			case "BlockBreakEvent":
			case "BlockPlaceEvent":
			case "CropJump":
				break;
				
			case "ChestOpen":
				ifPromoted = 1;
				break;
				
			case "DoorOpen":
				ifPromoted = 1;
				ifPublic = 2; //If isPublic
				break;
				
			default:
				return false;
		}
		
		boolean isOwner = player.getName().equals(Main.cM.getOwner(claimID));
		boolean isPromoted = isPromoted(claimID, player.getName());
		boolean isPublic = Main.cM.getSecureMode(claimID).equals("public");
		
		if(isOwner) {
			if(!Main.Utils.bool((int) ifOwner))
				return false;
			
			destroyBlock(player, claimID, loc); //On vérifie s'il ne détruit pas le bloc d'un locataire
			return true;
		}
		
		if(isPromoted) {
			if(event.equals("BlockPlaceEvent") && Main.Utils.bool((int) ifPromoted))
				saveBlock(player, claimID, loc); //Si c'est un placement de bloc, on l'enregistre
			if( (event.equals("BlockBreakEvent") || event.equals("CropJump")) && Main.Utils.bool((int) ifPromoted))
				return destroyBlock(player, claimID, loc); //Si c'est une destruction de bloc, on vérifie qu'il lui appartient
			return Main.Utils.bool((int) ifPromoted);
		}
		
		if(ifPublic == 2)
			return isPublic;
		
		return Main.Utils.bool((int) ifPublic);
	}
	
	public boolean allowedPosVerbose(Player player, String event, Location loc) {
		boolean allowed = allowedPos(player, event, loc);
		if(!allowed)
			Main.print(ChatColor.RED + "Vous n'êtes pas autorisé à effectuer cette action.", player);
		return allowed;
	}
	
	public void saveBlock(Player player, int claimID, Location loc) { //Enregistre un bloc posé par un joueur promu
		Map<String, Tag> tMAP = getFromID(claimID);
		if(tMAP == null)
			return;
		
		IntTag tX = new IntTag("X", (int) Math.floor(loc.getX()));
		IntTag tY = new IntTag("Y", (int) Math.floor(loc.getY()));
		IntTag tZ = new IntTag("Z", (int) Math.floor(loc.getZ()));

		Map<String, Tag> promoted = Main.Utils.copy((CompoundTag) tMAP.get("promoted"));
		List<Tag> blocks = Main.Utils.copy((ListTag) promoted.get(player.getName()));
		blocks.add(new ListTag("loc", IntTag.class, Arrays.asList(tX, tY, tZ)));
		promoted.put(player.getName(), new ListTag(player.getName(), ListTag.class, blocks));
		tMAP.put("promoted", new CompoundTag("promoted", promoted));
		save(claimID, tMAP);
	}
	
	public boolean destroyBlock(Player player, int claimID, Location loc) { //Vérifie et effectue la suppression d'un bloc par un joueur promu
		Map<String, Tag> tMAP = getFromID(claimID);
		if(tMAP == null)
			return false;
		
        String owner = (String) tMAP.get("owner").getValue();
		
		int X = (int) Math.floor(loc.getX());
		int Y = (int) Math.floor(loc.getY());
		int Z = (int) Math.floor(loc.getZ());

		Map<String, Tag> promoted = Main.Utils.copy((CompoundTag) tMAP.get("promoted"));

		if(getSharedMode(claimID).equals("true") || player.getName().equals(owner)) { //Si les autres joueurs promus peuvent casser les blocs...
			Set<String> keys = promoted.keySet();
			Iterator<String> itr = keys.iterator();

			while (itr.hasNext()) { 
				String pseudo = itr.next(); //Key
				List<Tag> blocks = Main.Utils.copy((ListTag) promoted.get(pseudo));
				blocks = checkBlocks(blocks, X, Y, Z);
				if( blocks != null ) {
					promoted.put(pseudo, new ListTag(pseudo, ListTag.class, blocks));
					tMAP.put("promoted", new CompoundTag("promoted", promoted));
					save(claimID, tMAP);
					return true;
				}
			}
		} else { //Si chacun doit pouvoir casser ses blocs
			List<Tag> blocks = Main.Utils.copy((ListTag) promoted.get(player.getName()));
			blocks = checkBlocks(blocks, X, Y, Z);
			if( blocks != null ) {
				promoted.put(player.getName(), new ListTag(player.getName(), ListTag.class, blocks));
				tMAP.put("promoted", new CompoundTag("promoted", promoted));
				save(claimID, tMAP);
				return true;
			}
		}
		
		return false;
	}
	
	private List<Tag> checkBlocks(List<Tag> blocks, int X, int Y, int Z) {
		int i = 0;
		for(Tag b : blocks) {
			ListTag thisLoc = (ListTag) b;
			IntTag tX = (IntTag) thisLoc.getValue().get(0);
			IntTag tY = (IntTag) thisLoc.getValue().get(1);
			IntTag tZ = (IntTag) thisLoc.getValue().get(2);
			
			if(tX.getValue() == X && tY.getValue() == Y && tZ.getValue() == Z) { //Le bloc a bien été posé par lui
				blocks.remove(i);
				return blocks;
			}
			i++;
		}
		return null;
	}
	
	public String getOwner(int claimID) {
		Map<String, Tag> tNBT = getFromID(claimID);
		
		if(tNBT == null)
			return "";
		
        return (String) tNBT.get("owner").getValue();
	}
	
	public String getWorld(int claimID) {
		Map<String, Tag> tNBT = getFromID(claimID);
		
		if(tNBT == null)
			return "";
		
        return (String) tNBT.get("world").getValue();
	}
	
	public String getSecureMode(int claimID) {
		Map<String, Tag> tNBT = getFromID(claimID);
		
		if(tNBT == null)
			return "";
		
        return (String) tNBT.get("secureMode").getValue();
	}
	
	public String getVersion(int claimID) {
		Map<String, Tag> tNBT = getFromID(claimID);
		
		if(tNBT == null)
			return "";
		
        return (String) tNBT.get("version").getValue();
	}
	
	public String getSharedMode(int claimID) {
		Map<String, Tag> tNBT = getFromID(claimID);
		
		if(tNBT == null)
			return "";
		
        return (String) tNBT.get("sharedMode").getValue();
	}
	
	public void remove(int claimID) {
		if(!claims.containsKey(claimID))
			return;
		
		claims.remove(claimID);
		try {
			File zonesFolder = new File(Bukkit.getPluginManager().getPlugin("HalfGuard").getDataFolder() + File.separator + "claims");
			File claimFile = new File(zonesFolder + File.separator + claimID + ".nbt");
			claimFile.delete();
		} catch(Exception e) { return; }
	}
	
	public boolean alreadyClaimed(Hashtable<String, Integer> zone) {
		Set<Integer> keys = this.claims.keySet();
	    Iterator<Integer> itr = keys.iterator();
	    
	    int xm = Math.min(zone.get("X1"), zone.get("X2"));
	    int xM = Math.max(zone.get("X1"), zone.get("X2"));
	    int ym = Math.min(zone.get("Y1"), zone.get("Y2"));
	    int yM = Math.max(zone.get("Y1"), zone.get("Y2"));
	    int zm = Math.min(zone.get("Z1"), zone.get("Z2"));
	    int zM = Math.max(zone.get("Z1"), zone.get("Z2"));

	    while (itr.hasNext()) { 
	       int claimID = itr.next(); //Key
	       Hashtable<String, Integer> tmpZ = this.claims.get(claimID); //Value
	       
	       int Xm = Math.min(tmpZ.get("X1"), tmpZ.get("X2"));
	       int XM = Math.max(tmpZ.get("X1"), tmpZ.get("X2"));
	       int Ym = Math.min(tmpZ.get("Y1"), tmpZ.get("Y2"));
	       int YM = Math.max(tmpZ.get("Y1"), tmpZ.get("Y2"));
	       int Zm = Math.min(tmpZ.get("Z1"), tmpZ.get("Z2"));
	       int ZM = Math.max(tmpZ.get("Z1"), tmpZ.get("Z2"));
	       
		   boolean errorForX = false;
		   boolean errorForY = false;
		   boolean errorForZ = false;
	       if( (xm >= Xm || xM >= Xm) && (xm <= XM || xM <= XM))
	    	   	errorForX = true;
	       if( (ym >= Ym || yM >= Ym) && (ym <= YM || yM <= YM))
	    	   	errorForY = true;
	       if( (zm >= Zm || zM >= Zm) && (zm <= ZM || zM <= ZM))
	    	   	errorForZ = true;
	       
	       if(errorForX && errorForY && errorForZ)
	    	   	return true;
	    } 
		return false;
	}
	
	public int currentClaim(Player player) { return getIDFromPos(player.getLocation()); }
	
	public int nextID() { //Donne l'ID du prochain claim
		File zonesFolder = new File(Bukkit.getPluginManager().getPlugin("HalfGuard").getDataFolder() + File.separator + "claims");
		if(zonesFolder.list().length == 0) //Si aucun claim n'a été défini, on commence à 1 :
			return 1;
		
		// Sinon, on regarde le dernier
		try {
            int next = Integer.parseInt(zonesFolder.list()[zonesFolder.list().length-1].replaceAll(".nbt", ""))+1;
            return next;
        } catch(Exception e) {
        		return 1;
        }
	}
	
	public boolean exist(int claimID) { //Indique si le claim existe
		File zonesFolder = new File(Bukkit.getPluginManager().getPlugin("HalfGuard").getDataFolder() + File.separator + "zones");
		
		for(String file : zonesFolder.list()) {
			if(file.equals(claimID + ".nbt"))
				return true;
		}
		return false;
	}
	
	public void setSecureMode(int claimID, String secureMode) {
		Map<String, Tag> tMAP = getFromID(claimID);
		if(tMAP == null)
			return;

		tMAP.put("secureMode", new StringTag("secureMode", secureMode));
		save(claimID, tMAP);
	}
	
	public void setVersion(int claimID, String version) {
		Map<String, Tag> tMAP = getFromID(claimID);
		if(tMAP == null)
			return;

		tMAP.put("version", new StringTag("version", version));
		save(claimID, tMAP);
	}
	
	public void setSharedMode(int claimID, String sharedMode) {
		Map<String, Tag> tMAP = getFromID(claimID);
		if(tMAP == null)
			return;

		tMAP.put("sharedMode", new StringTag("sharedMode", sharedMode));
		save(claimID, tMAP);
	}
	
	public void save(int claimID, Map<String, Tag> tMAP) {
		CompoundTag tNBT = new CompoundTag("" + claimID, tMAP);
		try {
			File claimsFolder = new File(Bukkit.getPluginManager().getPlugin("HalfGuard").getDataFolder() + File.separator + "claims");
	        File claimFile = new File(claimsFolder + File.separator + claimID + ".nbt");
	        NBTOutputStream oNBT = new NBTOutputStream(new FileOutputStream(claimFile));
	        oNBT.writeTag(tNBT);
	        oNBT.close();
		} catch(Exception e) {
    			Main.printConsole(ChatColor.RED + "Impossible to create claim #" + claimID + "!");
    			Main.printConsole(ChatColor.RED + e.getMessage());
    			return;
		}
	}
	
	public void promote(int claimID, String player) {
		Map<String, Tag> tMAP = getFromID(claimID);
		if(tMAP == null)
			return;

		Map<String, Tag> promoted = Main.Utils.copy((CompoundTag) tMAP.get("promoted"));
		promoted.put(player, new ListTag(player, ListTag.class, Arrays.asList()));
		tMAP.put("promoted", new CompoundTag("promoted", promoted));
		save(claimID, tMAP);
	}
	
	public void dismiss(int claimID, String player, String blocksOption) {
		Map<String, Tag> tMAP = getFromID(claimID);
		if(tMAP == null)
			return;

		Map<String, Tag> promoted = Main.Utils.copy((CompoundTag) tMAP.get("promoted"));
		
		if(blocksOption.equals("remove")) { //Si on souhaite supprimer tous les blocs ...
			List<Tag> blocks = Main.Utils.copy((ListTag) promoted.get(player));

			for(Tag b : blocks) {
				ListTag thisLoc = (ListTag) b;
				IntTag tX = (IntTag) thisLoc.getValue().get(0);
				IntTag tY = (IntTag) thisLoc.getValue().get(1);
				IntTag tZ = (IntTag) thisLoc.getValue().get(2);
				
				Block block = Bukkit.getServer().getWorlds().get(0).getBlockAt(tX.getValue(), tY.getValue(), tZ.getValue());
				block.setType(Material.AIR);
			}
		}
		
		promoted.remove(player);
		tMAP.put("promoted", new CompoundTag("promoted", promoted));
		save(claimID, tMAP);
	}
	
	@SuppressWarnings("unchecked")
	public int create(Player owner, Hashtable<String, Integer> zone) {
		String DEFAULT_SECURE_MODE = "private"; //Mode de sécurisation par défaut
		String DEFAULT_SHARED_MODE = "true"; //Mode de partage par défaut
		
		int claimID = nextID();
		try {
			StringTag towner = new StringTag("owner", owner.getName());
			StringTag tversion = new StringTag("version", Main.pluginVersion);
			StringTag tworld = new StringTag("world", owner.getWorld().getName());
			StringTag tsecure = new StringTag("secureMode", DEFAULT_SECURE_MODE);
			StringTag tshared = new StringTag("sharedMode", DEFAULT_SHARED_MODE);
			
			IntTag tX1 = new IntTag("X", zone.get("X1"));
			IntTag tX2 = new IntTag("X", zone.get("X2"));
			IntTag tY1 = new IntTag("Y", zone.get("Y1"));
			IntTag tY2 = new IntTag("Y", zone.get("Y2"));
			IntTag tZ1 = new IntTag("Z", zone.get("Z1"));
			IntTag tZ2 = new IntTag("Z", zone.get("Z2"));
			
			ListTag tloc1 = new ListTag("loc1", IntTag.class, Arrays.asList(tX1, tY1, tZ1));
			ListTag tloc2 = new ListTag("loc2", IntTag.class, Arrays.asList(tX2, tY2, tZ2));
			
			Map <String, Tag> tMAP = new HashMap<String, Tag>();
			tMAP.put("owner", towner);
			tMAP.put("version", tversion);
			tMAP.put("secureMode", tsecure);
			tMAP.put("sharedMode", tshared);
			tMAP.put("world", tworld);
			tMAP.put("loc1", tloc1);
			tMAP.put("loc2", tloc2);
			
			Map<String, Tag> promoted = new HashMap<String, Tag>();
			tMAP.put("promoted", new CompoundTag("promoted", promoted));//Par défaut: Aucun joueur promu
			
			save(claimID, tMAP);
            this.claims.put(claimID, (Hashtable<String, Integer>) zone.clone()); //On enregistre la zone que l'on vient de claim
            return claimID;
        } catch(Exception e) {
        		Main.printConsole(ChatColor.RED + "Impossible to create claim #" + claimID + "!");
        		Main.printConsole(ChatColor.RED + e.getMessage());
            return -1;
        }
	}
	
}
