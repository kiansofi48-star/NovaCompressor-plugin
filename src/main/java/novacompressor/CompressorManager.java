package ir.novapixel.novacompressor;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class CompressorManager {

    private final NovaCompressor plugin;

    private final Map<String, Compressor> compressors = new HashMap<>();

    public CompressorManager(NovaCompressor plugin) {
        this.plugin = plugin;
    }

    public void load() {

        compressors.clear();

        ConfigurationSection section =
                plugin.getConfig().getConfigurationSection("compressors");

        if (section == null) {
            return;
        }

        for (String name : section.getKeys(false)) {

            Compressor compressor = new Compressor(name);

            compressor.setBeforeAmount(
                    section.getInt(name + ".before.amount")
            );

            compressor.setAfterAmount(
                    section.getInt(name + ".after.amount")
            );

            ItemStack before =
                    section.getItemStack(name + ".before.item");

            ItemStack after =
                    section.getItemStack(name + ".after.item");

            compressor.setBeforeItem(before);
            compressor.setAfterItem(after);

            compressors.put(name.toLowerCase(), compressor);
        }
    }

    public void save() {

        plugin.getConfig().set("compressors", null);

        for (Compressor compressor : compressors.values()) {

            String path =
                    "compressors." + compressor.getName();

            plugin.getConfig().set(
                    path + ".before.amount",
                    compressor.getBeforeAmount()
            );

            plugin.getConfig().set(
                    path + ".before.item",
                    compressor.getBeforeItem()
            );

            plugin.getConfig().set(
                    path + ".after.amount",
                    compressor.getAfterAmount()
            );

            plugin.getConfig().set(
                    path + ".after.item",
                    compressor.getAfterItem()
            );
        }

        plugin.saveConfig();
    }

    public void add(Compressor compressor) {
        compressors.put(
                compressor.getName().toLowerCase(),
                compressor
        );
    }

    public void remove(String name) {
        compressors.remove(name.toLowerCase());
    }

    public Compressor get(String name) {
        return compressors.get(name.toLowerCase());
    }

    public Collection<Compressor> getAll() {
        return compressors.values();
    }

    public Compressor findByInput(ItemStack item) {

        for (Compressor compressor : compressors.values()) {

            if (compressor.getBeforeItem() == null) {
                continue;
            }

            if (!compressor.getBeforeItem().isSimilar(item)) {
                continue;
            }

            return compressor;
        }

        return null;
    }
}