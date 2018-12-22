package be.nokorbis.spigot.commandsigns.controller;

import be.nokorbis.spigot.commandsigns.api.menu.EditionMenu;
import be.nokorbis.spigot.commandsigns.api.menu.MenuNavigationContext;
import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class NCommandSignsConfigurationManager {
	private final Player editor;
	private CommandBlock commandBlock;
	private boolean isCreating;
	private MenuNavigationContext navigationContext;

	public NCommandSignsConfigurationManager(final Player player) {
		this.editor = player;
		this.navigationContext = new MenuNavigationContext();
	}

	public Player getEditor() {
		return this.editor;
	}

	public boolean isCreating() {
		return this.isCreating;
	}

	public boolean isEditing() {
		return !this.isCreating();
	}

	public void setCreating(boolean creating) {
		this.isCreating = creating;
	}

	public void setEditing(boolean editing) {
		this.setCreating(!editing);
	}

	public CommandBlock getCommandBlock() {
		return commandBlock;
	}

	public void setCommandBlock(CommandBlock commandBlock) {
		this.commandBlock = commandBlock;
	}

	public void setCurrentMenu(EditionMenu<CommandBlock> menu) {
		this.navigationContext.setCoreMenu(menu);
	}

	public void display() {
		EditionMenu<CommandBlock> coreMenu = this.navigationContext.getCoreMenu();
		if (coreMenu != null) {
			coreMenu.display(editor, commandBlock, this.navigationContext);
		}
		else {
			if (this.isCreating) {
				this.editor.sendMessage(ChatColor.GREEN + "Creation complete!");
			}
			else {
				this.editor.sendMessage(ChatColor.GREEN + "Edition complete!");
			}
		}
	}

	public boolean handleCommandInput(String command) {
		return false;
	}

	public boolean handleChatInput(String message) {
		return false;
	}
}
