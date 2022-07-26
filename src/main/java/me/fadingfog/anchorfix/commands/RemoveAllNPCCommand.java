package me.fadingfog.anchorfix.commands;

import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unchecked"})
public class RemoveAllNPCCommand extends SubCommand {

    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public void perform(Player player, String[] args) {

        List<World> worlds = Bukkit.getWorlds();

        for (World world : worlds) {
            List<EntityPlayer> entitiesToRemove = new ArrayList<>();  // ConcurrentModificationException fix
            WorldServer nmsWorld = ((CraftWorld) world).getHandle();

            List<EntityPlayer> entities = nmsWorld.players;

            for (EntityPlayer entity : entities) {
                if (entity.getBukkitEntity().hasMetadata("NPC")) {
                    entitiesToRemove.add(entity);
                }
            }

            for (EntityPlayer entity : entitiesToRemove) {
                nmsWorld.removeEntity(entity);
            }

            System.out.println(nmsWorld.players);
        }

    }
}
