package ir.novapixel.novacompressor;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;

public class CompressorListener implements Listener {

    private final NovaCompressor plugin;

    public CompressorListener(NovaCompressor plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {

        if (!event.isSneaking()) {
            return;
        }

        Player player = event.getPlayer();

        if (!player.hasPermission("novacompressor.use")) {
            return;
        }

        ItemStack hand =
                player.getInventory().getItemInMainHand();

        if (hand.getType().isAir()) {
            return;
        }

        Compressor compressor =
                plugin.getCompressorManager().findByInput(hand);

        if (compressor == null) {
            return;
        }

        if (compressor.getAfterItem() == null) {
            return;
        }

        int required = compressor.getBeforeAmount();

        if (hand.getAmount() < required) {

            player.sendMessage(
                    color(plugin.getConfig().getString(
                            "settings.not-enough-items"
                    ))
            );

            return;
        }

        /*
         * تعداد Compressorهایی که می‌توان اجرا کرد.
         */
        int times = hand.getAmount() / required;

        /*
         * برای جلوگیری از تبدیل ناخواسته تعداد خیلی زیاد،
         * در هر Shift فقط یک تبدیل انجام می‌دهیم.
         *
         * اگر بخواهی تمام 64تایی‌ها همزمان تبدیل شوند،
         * این قسمت را می‌توان تغییر داد.
         */
        times = 1;

        int removeAmount =
                required * times;

        int giveAmount =
                compressor.getAfterAmount() * times;

        /*
         * کم کردن آیتم ورودی
         */
        hand.setAmount(hand.getAmount() - removeAmount);

        /*
         * ساخت خروجی
         */
        ItemStack output =
                compressor.getAfterItem().clone();

        output.setAmount(giveAmount);

        /*
         * اضافه کردن خروجی به Inventory
         */
        player.getInventory().addItem(output);

        player.sendMessage(
                color(plugin.getConfig().getString(
                        "settings.success-message"
                ))
        );
    }

    private String color(String text) {

        if (text == null) {
            return "";
        }

        return ChatColor.translateAlternateColorCodes(
                '&',
                text
        );
    }
}