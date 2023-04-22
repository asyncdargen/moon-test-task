package ru.dargen.news.player;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.SneakyThrows;
import lombok.val;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class NewsViewHistory {

    private final MySQLSimpleExecutor database;
    private final LoadingCache<String, Integer> historyCache = CacheBuilder.newBuilder()
            .expireAfterAccess(15, TimeUnit.MINUTES)
            .build(new CacheLoader<String, Integer>() {
                @Override
                @SneakyThrows
                public Integer load(String player) throws Exception {
                    try (val rs = database.query(
                            "SELECT `last_news_id` FROM `news_view_history` WHERE `player` = ?;", player)) {
                        return rs.next() ? rs.getInt("last_news_id") : -1;
                    }
                }
            });

    public NewsViewHistory(Configuration config) {
        database = new MySQLSimpleExecutor(config);

        database.update(//language=mysql
                "CREATE TABLE IF NOT EXISTS `news_view_history` (`player` VARCHAR(16) NOT NULL, `last_news_id` INT NOT NULL, PRIMARY KEY (`player`))"
        );
    }

    public int lookupLastNewsView(Player player) {
        return lookupLastNewsView(player.getName());
    }

    public int lookupLastNewsView(String player) {
        try {
            return historyCache.get(player);
        } catch (Throwable t) {
            t.printStackTrace();
            return -1;
        }
    }

    public void updateLastNewsView(Player player, int newsId) {
        updateLastNewsView(player.getName(), newsId);
    }

    public void updateLastNewsView(String player, int newsId) {
        historyCache.put(player, newsId);
        database.updateAsync(
                "INSERT INTO `news_view_history` VALUES (?, ?) ON DUPLICATE KEY UPDATE `last_news_id` = ?;",
                player, newsId, newsId
        );
    }

}
