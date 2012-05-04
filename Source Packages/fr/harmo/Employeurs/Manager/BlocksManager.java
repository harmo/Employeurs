package fr.harmo.Employeurs.Manager;

import fr.harmo.Employeurs.Config.Config;
import fr.harmo.Employeurs.Employeurs;
import java.util.ArrayList;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.material.Sign;

/**
 *
 * @author HarmO
 */
public class BlocksManager {

	private Employeurs plugin;
	private ArrayList aBreakIdsList;
	private ArrayList aPlaceIdsList;
	private ArrayList aCraftIdsList;
	private ArrayList aDropIdsList;
	private ArrayList aAcceptedIds;
	private ArrayList aRefusedIds;

	public BlocksManager(Employeurs plugin) {
		this.plugin = plugin;
		this.aBreakIdsList = Config.getAuthorizedIds("break");
		this.aPlaceIdsList = Config.getAuthorizedIds("place");
		this.aCraftIdsList = Config.getAuthorizedIds("craft");
		this.aDropIdsList = Config.getAuthorizedIds("drop");
		this.aAcceptedIds = new ArrayList();
		this.aRefusedIds = new ArrayList();
	}

	public ArrayList getRefusedIds() {
		return this.aRefusedIds;
	}
	public ArrayList getAcceptedIds() {
		return this.aAcceptedIds;
	}

	public void clearRefusedIds() {
		this.aRefusedIds.clear();
	}
	public void clearAcceptedIds() {
		this.aAcceptedIds.clear();
	}

	public void addRefusedId (String id) {
		this.aRefusedIds.add(id);
	}
	public void addAcceptedId (int id) {
		this.aAcceptedIds.add(id);
	}
	public void addAcceptedIdString (String id) {
		this.aAcceptedIds.add(id);
	}

	public ArrayList getBreakIdsList() {
		return this.aBreakIdsList;
	}
	public ArrayList getPlaceIdsList() {
		return this.aPlaceIdsList;
	}
	public ArrayList getCraftIdsList() {
		return this.aCraftIdsList;
	}
	public ArrayList getDropIdsList() {
		return this.aDropIdsList;
	}

	public String getOrientation(Location loc) {
		loc.setX(loc.getX()-1);
		Location locPrevX = loc;
		loc.setX(loc.getX()+2);
		Location locNextX = loc;
		loc.setX(loc.getX()-1);
		loc.setZ(loc.getZ()-1);
		Location locPrevZ = loc;
		loc.setZ(loc.getZ()+2);
		Location locNextZ = loc;

		if (!locPrevX.getBlock().getType().toString().equals("AIR") || !locNextX.getBlock().getType().toString().equals("AIR")) {
			return "Z";
		}
		if (!locPrevZ.getBlock().getType().toString().equals("AIR") || !locNextZ.getBlock().getType().toString().equals("AIR")) {
			return "X";
		}
		return null;
	}
	public boolean isSignPlacedCorrectly(Location loc, Block block) {
		String northBlock = block.getRelative(BlockFace.NORTH).getType().toString();
		String southBlock = block.getRelative(BlockFace.SOUTH).getType().toString();
		String westBlock = block.getRelative(BlockFace.WEST).getType().toString();
		String eastBlock = block.getRelative(BlockFace.EAST).getType().toString();
		String bottomBlock = block.getRelative(BlockFace.DOWN).getType().toString();
		if ((!northBlock.equals("AIR") && southBlock.equals("AIR")) || (northBlock.equals("AIR") && !southBlock.equals("AIR"))) {
			if (eastBlock.equals("AIR") && westBlock.equals("AIR") && bottomBlock.equals("AIR")) {
				return true;
			}
		}
		if ((!eastBlock.equals("AIR") && westBlock.equals("AIR")) || (eastBlock.equals("AIR") && !westBlock.equals("AIR"))) {
			if (northBlock.equals("AIR") && southBlock.equals("AIR") && bottomBlock.equals("AIR")) {
				return true;
			}
		}
		return false;
	}
	public void destroyChest(Location loc) {
		loc.setY(loc.getY()-1);
		Block block = loc.getBlock();
		Block nextBlock;
		String northBlock = block.getRelative(BlockFace.NORTH).getType().toString();
		String southBlock = block.getRelative(BlockFace.SOUTH).getType().toString();
		String westBlock = block.getRelative(BlockFace.WEST).getType().toString();
		String eastBlock = block.getRelative(BlockFace.EAST).getType().toString();

		if (block.getType().toString().equals("CHEST")) {
			block.setTypeId(0);
			if (!northBlock.equals("AIR") && eastBlock.equals("CHEST")) {
				loc.setZ(loc.getZ()-1);
				nextBlock = loc.getBlock();
				nextBlock.setTypeId(0);
			}
			if (!southBlock.equals("AIR") && westBlock.equals("CHEST")) {
				loc.setZ(loc.getZ()+1);
				nextBlock = loc.getBlock();
				nextBlock.setTypeId(0);
			}
			if (!eastBlock.equals("AIR") && southBlock.equals("CHEST")) {
				loc.setX(loc.getX()+1);
				nextBlock = loc.getBlock();
				nextBlock.setTypeId(0);
			}
			if (!westBlock.equals("AIR") && northBlock.equals("CHEST")) {
				loc.setX(loc.getX()-1);
				nextBlock = loc.getBlock();
				nextBlock.setTypeId(0);
			}
		}
	}
	public String createChest(Location loc) {
		loc.setY(loc.getY()-1);
		Block block = loc.getBlock();
		Block nextBlock;
		String northBlock = block.getRelative(BlockFace.NORTH).getType().toString();
		String southBlock = block.getRelative(BlockFace.SOUTH).getType().toString();
		String westBlock = block.getRelative(BlockFace.WEST).getType().toString();
		String eastBlock = block.getRelative(BlockFace.EAST).getType().toString();
		if (block.getType().toString().equals("AIR")) {
			if (!northBlock.equals("AIR")) {
				if (eastBlock.equals("AIR")) {
					loc.setZ(loc.getZ()-1);
					nextBlock = loc.getBlock();
					block.setTypeId(54);
					nextBlock.setTypeId(54);
					return "ok";
				}
				else
					return "rightBlockError";
			}
			if (!southBlock.equals("AIR")) {
				if (westBlock.equals("AIR")) {
					loc.setZ(loc.getZ()+1);
					nextBlock = loc.getBlock();
					block.setTypeId(54);
					nextBlock.setTypeId(54);
					return "ok";
				}
				else
					return "rightBlockError";
			}
			if (!eastBlock.equals("AIR")) {
				if (southBlock.equals("AIR")) {
					loc.setX(loc.getX()+1);
					nextBlock = loc.getBlock();
					block.setTypeId(54);
					nextBlock.setTypeId(54);
					return "ok";
				}
				else
					return "rightBlockError";
			}
			if (!westBlock.equals("AIR")) {
				if (northBlock.equals("AIR")) {
					loc.setX(loc.getX()-1);
					nextBlock = loc.getBlock();
					block.setTypeId(54);
					nextBlock.setTypeId(54);
					return "ok";
				}
				else
					return "rightBlockError";
			}
			else
				return "error";
		}
		else
			return "bottomBlockError";
	}

}
