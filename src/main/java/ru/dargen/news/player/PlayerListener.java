package ru.dargen.news.player;

import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import ru.dargen.news.VkNewsPlugin;

public class PlayerListener implements Listener {

    @EventHandler
    void on(PlayerJoinEvent event) {
        val player = event.getPlayer();
        val holder = VkNewsPlugin.instance().getNewsHolder();

        if (holder.hasNews()) Bukkit.getScheduler().runTaskLaterAsynchronously(VkNewsPlugin.instance(), () -> {
            val news = holder.getLastNews();

            if (!news.isViewedBy(player))
                news.openBook(player);
        }, 15);
    }

}
