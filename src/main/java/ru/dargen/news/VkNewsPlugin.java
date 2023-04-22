package ru.dargen.news;

import lombok.Getter;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import ru.dargen.news.command.NewsCommand;
import ru.dargen.news.player.NewsViewHistory;
import ru.dargen.news.player.PlayerListener;
import ru.dargen.news.vk.VkNewsHolder;

@Getter
public class VkNewsPlugin extends JavaPlugin {

    private static VkNewsPlugin INSTANCE;

    private VkNewsHolder newsHolder;
    private NewsViewHistory viewHistory;

    @Override
    public void onEnable() {
        INSTANCE = this;

        saveDefaultConfig();
        val config = getConfig();

        newsHolder = new VkNewsHolder(this, config);
        viewHistory = new NewsViewHistory(config);

        getCommand("news").setExecutor(new NewsCommand());
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
    }

    public static VkNewsPlugin instance() {
        return INSTANCE;
    }

}
