package me.fadingfog.anchorfix.commands;

import net.minecraft.server.v1_7_R4.*;
import net.minecraft.util.com.mojang.authlib.GameProfile;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R4.CraftServer;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.UUID;

@SuppressWarnings("unchecked")
public class LoadChunkCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        String message = "Something went wrong";
        Player p = null;

        if (sender instanceof Player) {
            p = (Player) sender;
        }

//        World world = ((Player) sender).getLocation().getWorld(); // command work only if sender is player
        World world = Bukkit.getWorlds().get(0);

        if (args.length == 0) {
            message = "Provide command args!";
        } else if (args.length >= 3) {
            int x = Integer.parseInt(args[1]) >> 4;
            int z = Integer.parseInt(args[2]) >> 4;


            if (Objects.equals(args[0], "isloaded")) {

                if (world.isChunkLoaded(x, z)) message = "Chunk is loaded"; else message = "Chunk is not loaded";

            } else if (Objects.equals(args[0], "load")) {
                world.loadChunk(x, z);
                world.refreshChunk(x, z);

                message = "Chunk successfully loaded";

            } else if (Objects.equals(args[0], "unload")) {
                boolean res = false;
                if (args.length == 4) {
                    if (Objects.equals(args[3], "force")) {
                        res = world.unloadChunk(x, z, true, false);
                    }
                } else {
                    res = world.unloadChunk(x, z);
                }

                if (res) message = "Chunk successfully unloaded"; else message = "Chunk was unloaded but still loaded (WHAT)?";

            } else if (Objects.equals(args[0], "npc")) {
                MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
                WorldServer nmsWorld = ((CraftWorld)Bukkit.getWorld("world")).getHandle();
                GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "Tobi");
                EntityPlayer npc = new EntityPlayer(nmsServer, nmsWorld, gameProfile, new PlayerInteractManager(nmsWorld));
                npc.playerConnection = new PlayerConnection(npc.server, new NetworkManager(true), npc);
                npc.playerInteractManager.setGameMode(EnumGamemode.CREATIVE);

                npc.spawnIn(nmsWorld);
                npc.setLocation(x * 16, 66, z * 16, 1, 1);
                npc.world.players.add(npc);
//                nmsWorld.getChunkAt(x, z).a(npc);  // Unnecessary
//                nmsWorld.getPlayerChunkMap().addPlayer(npc);  // Unnecessary


                message = "Npc created";
            }
//            else if (Objects.equals(args[0], "ent")) { // Not working
//                Entity[] ents = world.getChunkAt(x, z).getEntities();
//                System.out.println( Arrays.toString(ents) );
//                message = "Entities printed";
//            }


        } else {
            message = "Wrong command. Try again";
        }

        if (p != null) {
            p.sendMessage(message);
        } else {
            System.out.println(message);
        }

        return true;
    }
}
