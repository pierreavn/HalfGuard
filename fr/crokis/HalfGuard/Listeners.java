package fr.crokis.HalfGuard;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockMultiPlaceEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jnbt.CompoundTag;
import org.jnbt.IntTag;
import org.jnbt.ListTag;
import org.jnbt.Tag;

public class Listeners implements Listener {
	protected Plugin main;
	Listeners(Plugin main) { this.main = main; } //On enregistre l'instance du plugin
	
	/*
	 * POUR AJOUTER UNE NOUVELLE CLASSE DE LISTENERS
	 * MODIFIER startGlobalListeners() DANS Main.java
	 */
	
	// Evenements
	@EventHandler
	public void onPlayerSelectPoint(PlayerInteractEvent event) {
		if(event.getPlayer().hasPermission("HalfGuard.claim")) {
			ItemStack item = event.getItem();
			if(item != null) {
				if(item.getType() == Material.WOOD_PICKAXE) {
					int X = event.getClickedBlock().getX();
					int Y = event.getClickedBlock().getY();
					int Z = event.getClickedBlock().getZ();
					Main.zM.zoneSet(event.getPlayer(), X, Y, Z, event.getAction());

					if(event.getAction() == Action.LEFT_CLICK_BLOCK) {
						Main.print(ChatColor.GREEN + "First selection x:" + X + "/y:" + Y + "/z:" + Z + " (" + event.getClickedBlock().getWorld().getName() + ")", event.getPlayer());
					} else {
						Main.print(ChatColor.GREEN + "Second selection x:" + X + "/y:" + Y + "/z:" + Z + " (" + event.getClickedBlock().getWorld().getName() + ")", event.getPlayer());
					}

					event.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) { event.setCancelled(!Main.cM.allowedPosVerbose(event.getPlayer(), event.getEventName(), event.getBlock().getLocation())); }
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		if(event.getEventName().equals("BlockMultiPlaceEvent"))
			return;
		
		event.setCancelled(!Main.cM.allowedPosVerbose(event.getPlayer(), event.getEventName(), event.getBlock().getLocation()));
	}
	@EventHandler
	public void onBlockMultiPlace(BlockMultiPlaceEvent event) {
		boolean result = false;
		List<BlockState> blocks = event.getReplacedBlockStates();
		for(BlockState block : blocks) {
			if(!Main.cM.allowedPosVerbose(event.getPlayer(), "BlockPlaceEvent", block.getLocation()))
				result = true;
		}
		event.setCancelled(result);
	}
	@EventHandler
	public void onDoorOpen(PlayerInteractEvent event) {
		if(event.getPlayer() == null || event.getClickedBlock() == null || event.getClickedBlock().getType() == null)
			return;

		switch(event.getClickedBlock().getType()) {
			case WOODEN_DOOR:
			case IRON_DOOR_BLOCK:
			case SPRUCE_DOOR:
			case BIRCH_DOOR:
			case JUNGLE_DOOR:
			case ACACIA_DOOR:
			case DARK_OAK_DOOR:
				if(event.getAction() != Action.RIGHT_CLICK_BLOCK)
					return;
				break;
			
			default:
				return;
		}
		event.setCancelled(!Main.cM.allowedPosVerbose(event.getPlayer(), "DoorOpen", event.getClickedBlock().getLocation()));
	}
	@EventHandler
	public void onChestOpen(PlayerInteractEvent event) {
		if(event.getPlayer() == null || event.getClickedBlock() == null || event.getClickedBlock().getType() == null)
			return;

		switch(event.getClickedBlock().getType()) {
			case CHEST:
			case ENDER_CHEST:
			case TRAPPED_CHEST:
				if(event.getAction() != Action.RIGHT_CLICK_BLOCK)
					return;
				break;
			
			default:
				return;
		}
		event.setCancelled(!Main.cM.allowedPosVerbose(event.getPlayer(), "ChestOpen", event.getClickedBlock().getLocation()));
	}
	@EventHandler
	public void onCropTrampling(PlayerInteractEvent event) {
        if (event.getAction() == Action.PHYSICAL) { // Physical = Jump
            Block block = event.getClickedBlock();
            Main.printConsole(block.getLocation().toString());
            if (block.getType() == Material.SOIL && !Main.cM.allowedPosVerbose(event.getPlayer(), "CropJump", block.getLocation())) {
                // Annuler l'event
                event.setUseInteractedBlock(org.bukkit.event.Event.Result.DENY);
                event.setCancelled(true);
            }
        }
	}
	/*
	@EventHandler
	public void onTreeBlockChange(BlockPhysicsEvent event) {
		Main.printConsole(event.getBlock().getType().toString());
		switch(event.getBlock().getType().toString()) {
			case "LOG":
			case "WOOD":
			case "LEAVES": //Si des nouveaux blocs de feuilles ou de bois sont ajoutés...
				break;
			
			default:
				return;
		}
		Main.printConsole("DELETE_REQ");
		
		//On vérifie qu'ils ne sont pas à supprimer
		int i = 0;
		for(BlockState b : Main.cM.blocksToDelete) {
			if(b.getX() == event.getBlock().getX() && b.getY() == event.getBlock().getY() && b.getZ() == event.getBlock().getZ()) {
				event.setCancelled(true);
				Main.cM.blocksToDelete.remove(i);
				Main.printConsole("DELETED");
				return;
			}
			i++;
		}
	}
	*/
	@EventHandler
	public void onTreeGrowth(StructureGrowEvent event) {
		//Si un arbre pousse, il faut enregistrer chaque nouveau bloc généré
		
		//On cherche d'abord le claim
		int claimID = Main.cM.getIDFromPos(event.getLocation());
		if(claimID == -1)
			return;
		
		//On vérifie ensuite si le joueur est toujours promu
		//Sinon, l'arbre appartient au propriétaire
		
		Set<String> promotedList = Main.cM.promotelist(claimID);
		if(!promotedList.contains(event.getPlayer().getName()))
			return;
		
		Map<String, Tag> tMAP = Main.cM.getFromID(claimID);
		if(tMAP == null)
			return;
		Map<String, Tag> promoted = Main.Utils.copy((CompoundTag) tMAP.get("promoted"));

		//On ajoute chaque nouvel élément de la structure aux blocs du joueur
		List<BlockState> blocks = event.getBlocks();
		for(BlockState b : blocks) { //Pour chaque block...
			if(Main.cM.isInClaim(b.getLocation(), claimID)) {
				IntTag tX = new IntTag("X", (int) Math.floor(b.getX()));
				IntTag tY = new IntTag("Y", (int) Math.floor(b.getY()));
				IntTag tZ = new IntTag("Z", (int) Math.floor(b.getZ()));

				List<Tag> playerBlocks = Main.Utils.copy((ListTag) promoted.get(event.getPlayer().getName()));
				playerBlocks.add(new ListTag("loc", IntTag.class, Arrays.asList(tX, tY, tZ)));
				promoted.put(event.getPlayer().getName(), new ListTag(event.getPlayer().getName(), ListTag.class, playerBlocks));
			//} else {
				//Main.cM.blocksToDelete.add(b); //Si le bloc dépasse du claim, on demande à le supprimer
			}
		}
		
		tMAP.put("promoted", new CompoundTag("promoted", promoted));
		Main.cM.save(claimID, tMAP);
		return;
	}
}