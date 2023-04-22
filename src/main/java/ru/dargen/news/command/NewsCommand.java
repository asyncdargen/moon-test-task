package ru.dargen.news.command;

import lombok.val;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.dargen.news.VkNewsPlugin;

public class NewsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cThis command allowed only for players!");
            return true;
        }

        val player = (Player) sender;
        val holder = VkNewsPlugin.instance().getNewsHolder();

        if (holder.hasNews()) holder.getLastNews().openBook(player);
        else player.sendMessage("§cПоследние новости - не найдены!");

        return true;
    }

}
