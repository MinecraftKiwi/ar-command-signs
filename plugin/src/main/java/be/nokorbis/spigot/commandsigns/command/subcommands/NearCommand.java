package be.nokorbis.spigot.commandsigns.command.subcommands;

import be.nokorbis.spigot.commandsigns.controller.Container;
import be.nokorbis.spigot.commandsigns.controller.NCommandSignsManager;
import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import be.nokorbis.spigot.commandsigns.model.CommandSignsCommandException;
import be.nokorbis.spigot.commandsigns.utils.CommandSignUtils;
import be.nokorbis.spigot.commandsigns.utils.Messages;
import be.nokorbis.spigot.commandsigns.command.Command;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by nokorbis on 1/20/16.
 */
public class NearCommand extends Command
{
    private NCommandSignsManager manager;

    public NearCommand(NCommandSignsManager manager)
    {
        super("near", new String[0]);
        this.manager = manager;
        this.basePermission = "commandsign.admin.near";
    }

    @Override
    public boolean execute(CommandSender sender, List<String> args) throws CommandSignsCommandException
    {
        if (!(sender instanceof Player))
        {
            throw new CommandSignsCommandException(Messages.get("error.player_command"));
        }

        if (args.isEmpty())
        {
            throw new CommandSignsCommandException(Messages.get("error.command_needs_radius"));
        }

        Player player = (Player) sender;

        try
        {
            int radius = Integer.parseInt(args.get(0));

            LinkedList<CommandBlock> cmds = new LinkedList<>();
            for (Location loc : CommandSignUtils.getLocationsAroundPoint(player.getLocation(), radius)) {
                if (Container.getContainer().getCommandBlocks().containsKey(loc))  {
                    cmds.add(Container.getContainer().getCommandBlocks().get(loc));
                }
            }
            for (CommandBlock cmd : cmds) {
                sender.sendMessage(formatCommand(cmd));
            }
        }
        catch (NumberFormatException ex)
        {
            throw new CommandSignsCommandException(Messages.get("error.number_argument"));
        }

        return true;
    }

    /**
     * Format the message so that it can be easily read by a player
     * @param cmd
     *      The command block whose String needs to be formatted
     * @return
     *      A formatted String
     */
    private String formatCommand(CommandBlock cmd)
    {
        String msg = Messages.get("info.near_format");
        msg = msg.replace("{NAME}", cmd.getName())
                    .replace("{ID}", String.valueOf(cmd.getId()))
                    .replace("{POSITION}", cmd.blockSummary());
        return msg;
    }

    @Override
    public void printUsage(CommandSender sender)
    {
        sender.sendMessage("/commandsign near <radius>");
    }
}