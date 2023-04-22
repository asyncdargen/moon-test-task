package ru.dargen.news.vk;

import lombok.Data;
import lombok.val;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import ru.dargen.news.VkNewsPlugin;
import ru.dargen.news.util.BookUtil;

@Data
public class VkNews {

    protected final int id;
    protected final String content;

    protected ItemStack book;

    public VkNews(int id, String content) {
        this.id = id;
        this.content = content;

        book = new ItemStack(Material.WRITTEN_BOOK);

        val meta = (BookMeta) book.getItemMeta();
        meta.setAuthor("Vk News");
        meta.addPage(content);

        book.setItemMeta(meta);
    }

    public void openBook(Player player) {
        BookUtil.openBook(player, book);

        VkNewsPlugin.instance().getViewHistory().updateLastNewsView(player, id);
    }

    public boolean isViewedBy(Player player) {
        return VkNewsPlugin.instance().getViewHistory().lookupLastNewsView(player) == id;
    }

}
