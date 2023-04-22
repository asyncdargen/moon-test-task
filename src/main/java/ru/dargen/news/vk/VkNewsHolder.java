package ru.dargen.news.vk;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.ServiceActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import ru.dargen.news.VkNewsPlugin;

public class VkNewsHolder {

    @Getter
    private VkNews lastNews;

    private final int groupId;
    private final VkApiClient client;
    private final ServiceActor actor;

    public VkNewsHolder(VkNewsPlugin plugin, ConfigurationSection config) {
        config = config.getConfigurationSection("vk");

        groupId = config.getInt("group-id");

        client = new VkApiClient(new HttpTransportClient());
        actor = new ServiceActor(config.getInt("application-id"), config.getString("application-token"));

        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::fetchNews, 0, 20 * 60);
    }

    @SneakyThrows
    private void fetchNews() {
        val wall = client.wall()
                .get(actor).ownerId(groupId)
                .count(1).offset(1)
                .execute().getItems();

        if (!wall.isEmpty()) {
            val rawNews = wall.get(0);
            lastNews = new VkNews(rawNews.getId(), rawNews.getText());
        }
    }

    public boolean hasNews() {
        return lastNews != null;
    }

}
