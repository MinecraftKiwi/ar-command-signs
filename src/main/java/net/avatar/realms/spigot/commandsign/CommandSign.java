package net.avatar.realms.spigot.commandsign;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.java.JavaPlugin;

import net.avatar.realms.spigot.commandsign.data.IBlockSaver;
import net.avatar.realms.spigot.commandsign.data.JsonBlockSaver;
import net.avatar.realms.spigot.commandsign.model.CommandBlock;
import net.avatar.realms.spigot.commandsign.model.EditingConfiguration;
import net.avatar.realms.spigot.commandsign.tasks.SaverTask;

public class CommandSign extends JavaPlugin{
	
	private static CommandSign plugin;
	
	private Map<Player, PermissionAttachment> 	playerPerms;
	private Map<Block, CommandBlock> 			commandBlocks;
	private Map<Player, EditingConfiguration> 	creatingConfigurations;
	private Map<Player, EditingConfiguration> 	editingConfigurations;
	private Map<Player, CommandBlock>			copyingConfigurations;
	private Map<Player, Block> 					deletingBlocks;
	public List<Player> 						infoPlayers;
	
	private IBlockSaver							blockSaver;
	private SaverTask 							saver;
	
	public static final List<Material> VALID_MATERIALS = new LinkedList<Material>() {

		private static final long serialVersionUID = 5828774578373884657L;

		{
			add(Material.WALL_SIGN);
			add(Material.SIGN_POST);
			add(Material.STONE_BUTTON);
			add(Material.WOOD_BUTTON);
			add(Material.STONE_PLATE);
			add(Material.WOOD_PLATE);
			add(Material.GOLD_PLATE);
			add(Material.IRON_PLATE);
		}
	};
	
	public static final List<Material> PLATES = new LinkedList<Material>() {

		private static final long serialVersionUID = -7382953117406089790L;

		{
			add(Material.STONE_PLATE);
			add(Material.WOOD_PLATE);
			add(Material.GOLD_PLATE);
			add(Material.IRON_PLATE);
		}
	};
	
	@Override
	public void onEnable() {
		plugin = this;
		
		playerPerms = new HashMap<Player, PermissionAttachment>();
		commandBlocks = new HashMap<Block , CommandBlock>();
		creatingConfigurations = new HashMap<Player, EditingConfiguration>();
		editingConfigurations = new HashMap<Player, EditingConfiguration>();
		copyingConfigurations = new HashMap<Player, CommandBlock>();
		deletingBlocks = new HashMap<Player, Block>();
		infoPlayers = new LinkedList<Player>();
		
		this.getCommand("commandsign").setExecutor(new CommandSignCommands(this));
		this.getServer().getPluginManager().registerEvents(new CommandSignListener(this), this);
		
		try {
			this.blockSaver = new JsonBlockSaver(this.getDataFolder());
			loadData();
			saver = new SaverTask(this);
			long delay = 20 * 60 * 10; //Server ticks
			long period = 20 * 60 * 5; // Server ticks
			Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, saver, delay, period);
		} catch (Exception e) {
			getLogger().severe("Was not able to create the save file for command sign plugin");
			e.printStackTrace();
		}
		getLogger().info("CommandSigns properly enabled !");
	}
	
	@Override
	public void onDisable() {
		plugin = null;
		if (blockSaver != null) {
			saveData();
		}
	}
	
	public static CommandSign getPlugin() {
		return plugin;
	}
	
	public PermissionAttachment getPlayerPermissions(Player player) {
		
		if (playerPerms.containsKey(player)) {
			return playerPerms.get(player);
		}
		
		PermissionAttachment perms = player.addAttachment(this);
		playerPerms.put(player, perms);
		
		return perms;
	}
	
	public Map<Block, CommandBlock> getCommandBlocks() {
		return commandBlocks;
	}
	
	public Map<Player, EditingConfiguration> getCreatingConfigurations() {
		return creatingConfigurations;
	}
	
	public Map<Player, EditingConfiguration> getEditingConfigurations() {
		return editingConfigurations;
	}
	
	public Map<Player, CommandBlock> getCopyingConfigurations() {
		return copyingConfigurations;
	}
	
	public Map<Player, Block> getDeletingBlocks() {
		return deletingBlocks;
	}
	
	public List<Player> getInfoPlayers() {
		return infoPlayers;
	}
	
	private void loadData() {
		Collection<CommandBlock> data = blockSaver.load();
		if (data == null) {
			return;
		}
		for (CommandBlock block : data) {
			commandBlocks.put(block.getBlock(), block);
		}
	}
	
	public void saveData() {
		blockSaver.save(commandBlocks.values());
	}
}
