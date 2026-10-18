package ir.novapixel.novacompressor;

import org.bukkit.inventory.ItemStack;

public class Compressor {

    private final String name;

    private ItemStack beforeItem;
    private int beforeAmount;

    private ItemStack afterItem;
    private int afterAmount;

    public Compressor(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ItemStack getBeforeItem() {
        return beforeItem;
    }

    public void setBeforeItem(ItemStack beforeItem) {
        this.beforeItem = beforeItem;
    }

    public int getBeforeAmount() {
        return beforeAmount;
    }

    public void setBeforeAmount(int beforeAmount) {
        this.beforeAmount = beforeAmount;
    }

    public ItemStack getAfterItem() {
        return afterItem;
    }

    public void setAfterItem(ItemStack afterItem) {
        this.afterItem = afterItem;
    }

    public int getAfterAmount() {
        return afterAmount;
    }

    public void setAfterAmount(int afterAmount) {
        this.afterAmount = afterAmount;
    }
}