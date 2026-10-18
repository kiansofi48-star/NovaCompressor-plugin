package ir.novapixel.novacompressor;

import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CompressorCommand implements CommandExecutor, TabCompleter {

    private final NovaCompressor plugin;

    public CompressorCommand(NovaCompressor plugin) {
        this.plugin = plugin;
    }

    private String color(String text) {
        if (text == null) {
            return "";
        }

        return ChatColor.translateAlternateColorCodes('&', text);
    }

    private boolean admin(CommandSender sender) {

        if (!sender.hasPermission("novacompressor.admin")) {

            sender.sendMessage(
                    color("&c✘ &fYou don't have permission to manage Compressors.")
            );

            return false;
        }

        return true;
    }

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {

        if (args.length == 0) {

            sendHelp(sender);

            return true;
        }

        String sub = args[0].toLowerCase();

        // =========================
        // CREATE
        // =========================

        if (sub.equals("create")) {

            if (!admin(sender)) return true;

            if (!(sender instanceof Player player)) {

                sender.sendMessage("Only players can use this command.");

                return true;
            }

            if (args.length < 2) {

                player.sendMessage(
                        color("&cUsage: /novacompressor create <name>")
                );

                return true;
            }

            String name = args[1];

            if (plugin.getCompressorManager().get(name) != null) {

                player.sendMessage(
                        color("&c✘ &fThis Compressor already exists.")
                );

                return true;
            }

            Compressor compressor = new Compressor(name);

            plugin.getCompressorManager().add(compressor);
            plugin.getCompressorManager().save();

            String message = plugin.getConfig()
                    .getString("settings.created");

            player.sendMessage(
                    color(message.replace("{name}", name))
            );

            return true;
        }

        // =========================
        // BEFOREBLOCK
        // =========================

        if (sub.equals("beforeblock")) {

            if (!admin(sender)) return true;

            if (!(sender instanceof Player player)) {
                return true;
            }

            if (args.length < 3) {

                player.sendMessage(
                        color("&cUsage: /novacompressor beforeblock <name> <amount>")
                );

                return true;
            }

            String name = args[1];

            Compressor compressor =
                    plugin.getCompressorManager().get(name);

            if (compressor == null) {

                player.sendMessage(
                        color("&c✘ &fThis Compressor does not exist.")
                );

                return true;
            }

            int amount;

            try {

                amount = Integer.parseInt(args[2]);

            } catch (NumberFormatException e) {

                player.sendMessage(
                        color("&c✘ &fAmount must be a number.")
                );

                return true;
            }

            if (amount <= 0) {

                player.sendMessage(
                        color("&c✘ &fAmount must be greater than 0.")
                );

                return true;
            }

            ItemStack hand =
                    player.getInventory().getItemInMainHand();

            if (hand.getType().isAir()) {

                player.sendMessage(
                        color("&c✘ &fYou must hold an item in your hand.")
                );

                return true;
            }

            compressor.setBeforeItem(hand.clone());
            compressor.setBeforeAmount(amount);

            plugin.getCompressorManager().save();

            player.sendMessage(
                    color("&a✔ &fInput item set successfully for &e" + name)
            );

            return true;
        }

        // =========================
        // AFTERBLOCK
        // =========================

        if (sub.equals("afterblock")) {

            if (!admin(sender)) return true;

            if (!(sender instanceof Player player)) {
                return true;
            }

            if (args.length < 3) {

                player.sendMessage(
                        color("&cUsage: /novacompressor afterblock <name> <amount>")
                );

                return true;
            }

            String name = args[1];

            Compressor compressor =
                    plugin.getCompressorManager().get(name);

            if (compressor == null) {

                player.sendMessage(
                        color("&c✘ &fThis Compressor does not exist.")
                );

                return true;
            }

            int amount;

            try {

                amount = Integer.parseInt(args[2]);

            } catch (NumberFormatException e) {

                player.sendMessage(
                        color("&c✘ &fAmount must be a number.")
                );

                return true;
            }

            if (amount <= 0) {

                player.sendMessage(
                        color("&c✘ &fAmount must be greater than 0.")
                );

                return true;
            }

            ItemStack hand =
                    player.getInventory().getItemInMainHand();

            if (hand.getType().isAir()) {

                player.sendMessage(
                        color("&c✘ &fYou must hold the output item in your hand.")
                );

                return true;
            }

            compressor.setAfterItem(hand.clone());
            compressor.setAfterAmount(amount);

            plugin.getCompressorManager().save();

            player.sendMessage(
                    color("&a✔ &fOutput item set successfully for &e" + name)
            );

            return true;
        }

        // =========================
        // DELETE
        // =========================

        if (sub.equals("delete")) {

            if (!admin(sender)) return true;

            if (args.length < 2) {

                sender.sendMessage(
                        color("&cUsage: /novacompressor delete <name>")
                );

                return true;
            }

            String name = args[1];

            if (plugin.getCompressorManager().get(name) == null) {

                sender.sendMessage(
                        color("&c✘ &fThis Compressor does not exist.")
                );

                return true;
            }

            plugin.getCompressorManager().remove(name);
            plugin.getCompressorManager().save();

            String message = plugin.getConfig()
                    .getString("settings.deleted");

            sender.sendMessage(
                    color(message.replace("{name}", name))
            );

            return true;
        }

        // =========================
        // LIST
        // =========================

        if (sub.equals("list")) {

            if (!admin(sender)) return true;

            sender.sendMessage(
                    color("&6&lNovaCompressor &8- &fCompressors:")
            );

            if (plugin.getCompressorManager()
                    .getAll()
                    .isEmpty()) {

                sender.sendMessage(
                        color("&7No Compressors found.")
                );

                return true;
            }

            for (Compressor compressor :
                    plugin.getCompressorManager().getAll()) {

                String input = "Not set";
                String output = "Not set";

                if (compressor.getBeforeItem() != null) {

                    input =
                            compressor.getBeforeItem()
                                    .getType()
                                    .name()
                                    + " x"
                                    + compressor.getBeforeAmount();
                }

                if (compressor.getAfterItem() != null) {

                    output =
                            compressor.getAfterItem()
                                    .getType()
                                    .name()
                                    + " x"
                                    + compressor.getAfterAmount();
                }

                sender.sendMessage(
                        color(
                                "&e• &f" +
                                        compressor.getName() +
                                        " &8» &7" +
                                        input +
                                        " &f→ &a" +
                                        output
                        )
                );
            }

            return true;
        }

        // =========================
        // RELOAD
        // =========================

        if (sub.equals("reload")) {

            if (!admin(sender)) return true;

            plugin.reloadConfig();
            plugin.getCompressorManager().load();

            String message = plugin.getConfig()
                    .getString("settings.reloaded");

            sender.sendMessage(
                    color(message)
            );

            return true;
        }

        sendHelp(sender);

        return true;
    }

    private void sendHelp(CommandSender sender) {

        sender.sendMessage(
                color(
                        "&6&lNovaCompressor\n" +
                                "&e/novacompressor create <name>\n" +
                                "&e/novacompressor beforeblock <name> <amount>\n" +
                                "&e/novacompressor afterblock <name> <amount>\n" +
                                "&e/novacompressor delete <name>\n" +
                                "&e/novacompressor list\n" +
                                "&e/novacompressor reload"
                )
        );
    }

    @Override
    public List<String> onTabComplete(
            CommandSender sender,
            Command command,
            String alias,
            String[] args
    ) {

        if (args.length == 1) {

            return filter(
                    List.of(
                            "create",
                            "beforeblock",
                            "afterblock",
                            "delete",
                            "list",
                            "reload"
                    ),
                    args[0]
            );
        }

        if (args.length == 2 &&
                (
                        args[0].equalsIgnoreCase("beforeblock") ||
                                args[0].equalsIgnoreCase("afterblock") ||
                                args[0].equalsIgnoreCase("delete")
                )) {

            List<String> names = new ArrayList<>();

            for (Compressor compressor :
                    plugin.getCompressorManager().getAll()) {

                names.add(compressor.getName());
            }

            return filter(names, args[1]);
        }

        return List.of();
    }

    private List<String> filter(
            List<String> list,
            String input
    ) {

        List<String> result = new ArrayList<>();

        for (String value : list) {

            if (value.toLowerCase()
                    .startsWith(input.toLowerCase())) {

                result.add(value);
            }
        }

        return result;
    }
}