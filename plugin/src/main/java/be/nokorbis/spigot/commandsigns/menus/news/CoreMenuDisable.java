package be.nokorbis.spigot.commandsigns.menus.news;

import be.nokorbis.spigot.commandsigns.api.menu.EditionLeaf;
import be.nokorbis.spigot.commandsigns.api.menu.EditionMenu;
import be.nokorbis.spigot.commandsigns.api.menu.MenuEditable;
import be.nokorbis.spigot.commandsigns.api.menu.MenuNavigationResult;
import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import be.nokorbis.spigot.commandsigns.utils.Messages;
import org.bukkit.entity.Player;


public class CoreMenuDisable extends EditionLeaf<CommandBlock> {

	public CoreMenuDisable(EditionMenu<? extends MenuEditable> parent) {
		super(Messages.get("menu.disabled"), parent);
	}

	@Override
	public String getDataString(CommandBlock data) {
		return name.replace("{VALUE}", String.valueOf(data.isDisabled()));
	}

	@Override
	public void display(final Player editor, CommandBlock data, int page) {
		String msg = Messages.get("menu.disabled_edit");
		editor.sendMessage(msg);
	}

	@Override
	public void input(final Player player, final CommandBlock data, final String message, final MenuNavigationResult navigationResult) {
		try
		{
			String[] args = message.split(" ");
			String val = args[0].toLowerCase();
			if (val.equals("yes") || val.equals("true")) {
				data.setDisabled(true);
			}
			else {
				data.setDisabled(false);
			}
		}
		catch (Exception ignored)
		{
		}
		finally {
			navigationResult.setMenu(getParent());
		}
	}
}
