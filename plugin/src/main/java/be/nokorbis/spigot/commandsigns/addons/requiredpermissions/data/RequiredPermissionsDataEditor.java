package be.nokorbis.spigot.commandsigns.addons.requiredpermissions.data;

import be.nokorbis.spigot.commandsigns.addons.requiredpermissions.RequiredPermissionsAddon;
import be.nokorbis.spigot.commandsigns.api.DisplayMessages;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationData;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationDataEditorBase;
import be.nokorbis.spigot.commandsigns.model.CommandSignsCommandException;
import com.google.gson.internal.$Gson$Preconditions;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RequiredPermissionsDataEditor extends AddonConfigurationDataEditorBase {

    private static final List<String> SUB_COMMANDS = Arrays.asList("add", "edit", "remove");
    private static final DisplayMessages messages = DisplayMessages.getDisplayMessages("messages/addons");

    public RequiredPermissionsDataEditor(final RequiredPermissionsAddon addon) {
        super(addon);
    }

    @Override
    public void editValue(AddonConfigurationData configurationData, List<String> args) throws CommandSignsCommandException {
        if (args.size() < 2) {
            throw new CommandSignsCommandException(messages.get("error.command.require_args"));
        }

        try {
            RequiredPermissionsConfigurationData data = (RequiredPermissionsConfigurationData) configurationData;
            List<String> permissions = data.getRequiredPermissions();
            String command = args.remove(0);
            if ("add".equals(command)) {
                String toAdd = String.join(" ", args);
                permissions.add(toAdd);
            }
            else {
                int index = Integer.parseUnsignedInt(args.remove(0));
                if (index > permissions.size()) {
                    throw new CommandSignsCommandException(messages.get("error.command.index_too_large"));
                }

                if ("remove".equals(command)) {
                    permissions.remove(index-1);
                }
                else if ("edit".equals(command)) {
                    if (args.isEmpty()) {
                        throw new CommandSignsCommandException(messages.get("error.command.require_args"));
                    }

                    String toChange = String.join(" ", args);
                    permissions.set(index-1, toChange);
                }
            }

        }
        catch (NumberFormatException e) {
            throw new CommandSignsCommandException(messages.get("error.command.require_number"));
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, AddonConfigurationData configurationData, List<String> args) {
        if (args.isEmpty()) {
            return SUB_COMMANDS;
        }
        if (args.size() > 1) {
            return null;
        }
        if (configurationData == null) {
            return getDefaultIndexes();
        }

        RequiredPermissionsConfigurationData data = (RequiredPermissionsConfigurationData) configurationData;
        int limit = data.getRequiredPermissions().size();

        if (limit == 0) {
            return getDefaultIndexes();
        }

        return IntStream.range(1, limit).mapToObj(Integer::toString).collect(Collectors.toList());
    }

    private List<String> getDefaultIndexes() {
        return Arrays.asList("1", "2", "3");
    }
}