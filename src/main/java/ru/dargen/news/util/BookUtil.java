package ru.dargen.news.util;

import io.netty.buffer.Unpooled;
import lombok.experimental.UtilityClass;
import lombok.val;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@UtilityClass
public class BookUtil {

    public void openBook(Player player, ItemStack itemStack) {
        val handle = ((CraftPlayer) player).getHandle();
        val connection = handle.playerConnection;

        connection.sendPacket(buildBookUpdatePacket(handle, itemStack));
        connection.sendPacket(buildBookOpenPacket());
        player.updateInventory();
    }

    private PacketPlayOutSetSlot buildBookUpdatePacket(EntityPlayer player, ItemStack itemStack) {
        int index = player.getBukkitEntity().getInventory().getHeldItemSlot();

        if (index < net.minecraft.server.v1_12_R1.PlayerInventory.getHotbarSize()) {
            index += 36;
        } else if (index > 39) {
            index += 5;
        } else if (index > 35) {
            index = 8 - (index - 36);
        }

        return new PacketPlayOutSetSlot(
                player.defaultContainer.windowId,
                index,
                CraftItemStack.asNMSCopy(itemStack)
        );
    }

    private PacketPlayOutCustomPayload buildBookOpenPacket() {
        val serializer = new PacketDataSerializer(Unpooled.buffer());
        serializer.a(EnumHand.MAIN_HAND);
        return new PacketPlayOutCustomPayload("MC|BOpen", serializer);
    }

}
