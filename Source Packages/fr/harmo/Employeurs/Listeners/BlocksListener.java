package fr.harmo.Employeurs.Listeners;

import fr.harmo.Employeurs.Config.Config;
import fr.harmo.Employeurs.Employeurs;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;

/**
 *
 * @author HarmO
 */
public class BlocksListener implements Listener {
	
	private Employeurs plugin;
	private static final BlockFace[] faces = new BlockFace[]{BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};

	public BlocksListener(Employeurs plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onBlockDestroy(BlockBreakEvent event) throws Exception {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		Material material = block.getType();
	}
	
	@EventHandler
	public void onSignPlace(SignChangeEvent event) {
		Player player = event.getPlayer();
		String ligne1 = event.getLine(0);
		String ligne2 = event.getLine(1);
		String ligne3 = event.getLine(2);
		String ligne4 = event.getLine(3);
		Integer posX = event.getBlock().getX();
		Integer posY = event.getBlock().getY();
		Integer posZ = event.getBlock().getZ();
		if (ligne1.equalsIgnoreCase(Config.prefix)) {
			if (plugin.perms.has(player, "emp.create")) {
				event.setLine(0, Config.signColor + Config.prefix);
				
			}
			else {
				player.sendMessage(Config.noCreatePermMessage);
				event.setLine(0,"");
				event.setLine(1,"");
				event.setLine(2,"");
				event.setLine(3,"");
			}
		}
	}
	

}
