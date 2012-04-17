package fr.harmo.Employeurs;

import fr.harmo.Employeurs.Config.Config;
import fr.harmo.Employeurs.Economie.VaultLink;
import fr.harmo.Employeurs.Listeners.BlocksListener;
import fr.harmo.Employeurs.Listeners.CommandesListener;
import fr.harmo.Employeurs.Listeners.PlayerListener;
import java.util.List;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author
 * HarmO
 */
public class Employeurs extends JavaPlugin {
	
	public Permission perms;
	public BlocksListener blocksL = new BlocksListener(this);
	public PlayerListener playerL = new PlayerListener(this);
	public Manager manager = new Manager(this);
	
	@Override
	public void onEnable() {
		
		Config config = new Config(this);
		boolean setDefaultConfig = config.setDefaultConfig();
		config.setConfig();
		setupPermissions();
		if (!loadVault()) {
			getServer().getLogger().severe("==========- Employeurs -==========");
			getServer().getLogger().severe("Plugin Vault requis, téléchargement : ");
			getServer().getLogger().severe("http://dev.bukkit.org/server-mods/vault/");
			getServer().getLogger().severe("==============================");
			setEnabled(false);
		}
		
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(blocksL, this);
		pm.registerEvents(playerL, this);
		
		CommandesListener cmds = new CommandesListener(this);
		getCommand("emp").setExecutor(cmds);
		getCommand("empadm").setExecutor(cmds);
		
		getLogger().info("Actif !");
	}
	
	@Override
	public void onDisable() {
		getLogger().info("Inactif !");
	}
	
	private boolean loadVault() {
		Plugin test = getServer().getPluginManager().getPlugin("Vault");
			if (test == null)
				return false;

			VaultLink vault = new VaultLink(this);

			getLogger().info("Vault link ok.");
			return true;
	}
	
	private boolean setupPermissions() {
		RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
		perms = rsp.getProvider();
		return perms != null;
	}
	
	public void disablePlugin(){
		setEnabled(false);
	}

	public void sendMessageList(Player player, List<String> defaultMessages) {
		for (int i = 0; i < defaultMessages.size(); i++) {
			player.sendMessage(defaultMessages.get(i));
		}
	}
}
