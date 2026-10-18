package ir.novapixel.novacompressor;

import org.bukkit.plugin.java.JavaPlugin;

public final class NovaCompressor extends JavaPlugin {

    private CompressorManager compressorManager;

    @Override
    public void onEnable() {

        saveDefaultConfig();

        compressorManager = new CompressorManager(this);
        compressorManager.load();

        CompressorCommand command = new CompressorCommand(this);

        getCommand("novacompressor").setExecutor(command);
        getCommand("novacompressor").setTabCompleter(command);

        getServer().getPluginManager().registerEvents(
                new CompressorListener(this),
                this
        );

        getLogger().info("NovaCompressor enabled!");
    }

    @Override
    public void onDisable() {

        if (compressorManager != null) {
            compressorManager.save();
        }

        getLogger().info("NovaCompressor disabled!");
    }

    public CompressorManager getCompressorManager() {
        return compressorManager;
    }
}