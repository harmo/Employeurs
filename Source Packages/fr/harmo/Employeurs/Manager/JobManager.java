package fr.harmo.Employeurs.Manager;

import fr.harmo.Employeurs.Config.Config;
import fr.harmo.Employeurs.Employeurs;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author HarmO
 */
public class JobManager {

	private Employeurs plugin;
	private HashMap<String, String[]> aJobs = new HashMap();
	private HashMap<String, String[]> aChests = new HashMap();
	private static final BlockFace[] faces = new BlockFace[]{BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};

	public JobManager(Employeurs plugin) {
		this.plugin = plugin;
	}

	public ArrayList getJobList() {
		ArrayList jobList = new ArrayList();
		if (!Config.mysql) {
			// File
			try {
				jobList = Config.getDbFile().getJobList();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		else {
			// Database
		}
		return jobList;
	}
	public String[] getJobInfos(Location loc) {
		String[] aInfos = null;
		if (!Config.mysql) {
			aInfos = Config.getDbFile().getOffer(loc.getX(), loc.getY(), loc.getZ());
		}
		return aInfos;
	}
	public boolean takeJob(Player player, Location loc) {
		if (!Config.mysql) {
			// File
			try {
				String[] aJob = Config.getDbFile().getOffer(loc.getX(), loc.getY(), loc.getZ());
				if (aJobs.containsValue(aJob)) {
					player.sendMessage("Cette offre est déjà prise !!!");
					return false;
				}
				else if (aJobs.containsKey(player.getName())) {
					if (haveThisPost(player.getName(), loc)) {
						player.sendMessage("Vous avez déjà cet emploi !!!");
					}
					else {
						player.sendMessage("Vous avez déjà un emploi !!!");
					}
					return false;
				}
				else {
					aJobs.put(player.getName(), aJob);
					aChests.put(player.getName(), aJob);
					Config.getDbFile().addPost(player.getName(), aJob, aJob, player.getWorld().getName());
					return true;
				}
			}
			catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		else {
			// Database
			return true;
		}
	}
	public String getJobSalary(Location loc) {
		String[] aJob = getJobInfos(loc);
		return aJob[5];
	}
	public boolean quitJob(Player player) {
		if (!Config.mysql) {
			if (Config.getDbFile().deletePost(player.getName(), player.getWorld().getName())) {
				aJobs.remove(player.getName());
				return true;
			}
		}
		return false;
	}

	public ArrayList getOfferList() {
		ArrayList offerList = new ArrayList();
		if (!Config.mysql) {
			// File
			try {
				offerList = Config.getDbFile().getOfferList();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		else {
			// Database
		}
		return offerList;
	}
	public void setPosts(String playername, String[] aJob) {
		if (!aJobs.containsKey(playername)) {
			aJobs.put(playername, aJob);
		}
	}
	public void setChest(String playername, String[] aChest) {
		if (aChests.containsKey(playername)) {
			aChests.put(playername, aChest);
		}
	}


	public ArrayList getPostList() {
		ArrayList postList = new ArrayList();
		if (!Config.mysql) {
			// File
			try {
				postList = Config.getDbFile().getPostList();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		else {
			// Database
		}
		return postList;
	}
	public String[] getPostInfos(String playername) {
		String[] aInfos = null;
		if (!Config.mysql) {
			aInfos = Config.getDbFile().getPost(playername);
		}
		return aInfos;
	}
	public boolean havePost(String playername) {
		if (aJobs.containsKey(playername)) {
			return true;
		}
		return false;
	}
	public boolean haveThisPost(String playername, Location loc) {
		if (!Config.mysql) {
			String[] aJob = Config.getDbFile().getOffer(loc.getX(), loc.getY(), loc.getZ());
			if (aJobs.containsKey(playername)) {
				String[] playerJob = aJobs.get(playername);
				if (Arrays.equals(playerJob, aJob))
					return true;
			}
		}

		return false;
	}

	public boolean isPlayerAuthorized(Player player, Block block) {
		String playername = player.getName();
		Location loc = block.getLocation();
		loc.setY(loc.getY()+1);
		Block topBlock = loc.getBlock();
		if (topBlock.getType().toString().equals("WALL_SIGN")) {
			if (haveThisPost(playername, loc)) {
				return true;
			}
			else {
				String[] aJobInfos = getJobInfos(loc);
				if (aJobInfos[1].equals(playername))
					return true;
			}
		}
		else {
			Location newLoc = getNearLoc(loc, "WALL_SIGN");
			if (newLoc.getBlock().getType().toString().equals("WALL_SIGN")) {
				if (haveThisPost(playername, newLoc)) {
					return true;
				}
				else {
					String[] aJobInfos = getJobInfos(newLoc);
					if (aJobInfos[1].equals(playername))
						return true;
				}
			}
		}
		return false;
	}
	public Location getNearLoc(Location loc, String type) {
		if (loc.getBlock().getRelative(BlockFace.NORTH, 1).getType().toString().equals(type)) {
			loc.setX(loc.getX()-1);
		}
		if (loc.getBlock().getRelative(BlockFace.SOUTH, 1).getType().toString().equals(type)) {
			loc.setX(loc.getX()+1);
		}
		if (loc.getBlock().getRelative(BlockFace.EAST, 1).getType().toString().equals(type)) {
			loc.setZ(loc.getZ()-1);
		}
		if (loc.getBlock().getRelative(BlockFace.WEST, 1).getType().toString().equals(type)) {
			loc.setZ(loc.getZ()+1);
		}
		return loc;
	}
	public boolean isJobChest(Block block) {
		Location loc = block.getLocation();
		loc.setY(loc.getY()+1);
		Block topBlock = loc.getBlock();
		if (topBlock.getType().toString().equals("WALL_SIGN")) {
			Sign sign = (Sign) topBlock.getState();
			if (sign.getLine(0).substring(2, sign.getLine(0).length()).equals(Config.prefix))
				return true;
		}
		else {
			Location newLoc = getNearLoc(loc, "WALL_SIGN");
			if (newLoc.getBlock().getType().toString().equals("WALL_SIGN")) {
				Sign sign = (Sign) newLoc.getBlock().getState();
				if (sign.getLine(0).substring(2, sign.getLine(0).length()).equals(Config.prefix))
					return true;
			}
		}
		return false;
	}
	// get the chest content with the sign location
	public ItemStack[] getChestContent(Location loc) {
		ItemStack[] chestContent = null;
		Chest chest = getChestLocWithSign(loc);
		chestContent = chest.getInventory().getContents();
		return chestContent;
	}
	public Chest getChestLocWithSign(Location loc) {
		loc.setY(loc.getY()-1);
		Chest chest = (Chest) loc.getBlock().getState();
		return chest;
	}
	public Integer howManyItemsMore(int itemAmount, int itemId, Location loc) {
		int rest = 0;
		Location signLoc = getSignLocWithChest(loc);
		String[] aInfos = getJobInfos(signLoc);
		if (aInfos != null) {
			String[] aItems = aInfos[6].substring(1, aInfos[6].length()-1).split(",");
			for (int i = 0; i < aItems.length; i++) {
				String[] aSplit = aItems[i].split(":");
				Integer id = new Integer(aSplit[0]);
				Integer amount = new Integer(aSplit[1]);
				if (id == itemId) {
					rest = amount - itemAmount;
				}
			}
		}

		return rest;
	}
	public Location getSignLocWithChest(Location loc) {
		Location signLoc = loc;
		loc.setY(loc.getY()+1);
		if (loc.getBlock().getState().getType().toString().equals("WALL_SIGN")) {
			signLoc = loc;
		}
		else {
			Material block = loc.getBlock().getType();
			for (BlockFace face : faces) {
				block = loc.getBlock().getRelative(face).getType();
				if (block.toString().equals("WALL_SIGN")) {
					signLoc = loc.getBlock().getRelative(face).getLocation();
				}
			}
		}
		return signLoc;
	}
	public boolean isItemInPost(Integer itemID, Player player) {
		String[] aInfos = getPostInfos(player.getName());
		if (aInfos != null) {
			String[] aItems = aInfos[6].substring(1, aInfos[6].length()-1).split(",");
			for (int i = 0; i < aItems.length; i++) {
				String[] aSplit = aItems[i].split(":");
				String id = aSplit[0];
				if (itemID.toString().equals(id)) {
					return true;
				}
			}
		}
		return false;
	}

	public Integer getRecolted(Integer itemID, String playername, int x, int y, int z) {
		Integer recolted = 0;
		if (!Config.mysql) {
			try {
				String[] chestContent = Config.getDbFile().getchestContent(playername);
				if (chestContent != null) {
					for (int i = 0; i < chestContent.length; i++) {
						String[] split = chestContent[i].split(":");
						String id = split[0];
						String nb = split[1];
						if (itemID.toString().equals(id)) {
							recolted = new Integer(nb);
						}
					}
				}

			} catch(IOException io) {
				io.printStackTrace();
			}
		}
		return recolted;
	}
	public void registerPlayerChest(Player player, String worldname) {
		if (!Config.mysql) {
			try {
				ArrayList chestContent = new ArrayList();
				String[] aPost = Config.getDbFile().getPost(player.getName());
				double x = new Integer(aPost[2]);
				double y = new Integer(aPost[3]);
				double z = new Integer(aPost[4]);
				World world = plugin.getServer().getWorld(worldname);
				Location loc = new Location(world, x, y, z);
				ItemStack[] chest = getChestContent(loc);

				for (int i = 0; i < chest.length; i++) {
					if (chest[i] != null) {
						if (!chestContent.contains(chest[i].getTypeId())) {
							chestContent.add(chest[i].getTypeId() + ":" + chest[i].getAmount());
						}
					}
				}
				if (chestContent.size() > 0) {
					Config.getDbFile().setChestContent(player.getName(), worldname, chestContent.toArray());
				}
			} catch (IOException io) {
				io.printStackTrace();
			}
		}
	}

}
