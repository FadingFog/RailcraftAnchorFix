package me.fadingfog.anchorfix.listeners;

import me.fadingfog.anchorfix.AnchorFix;
import me.fadingfog.anchorfix.util.AnchorStorage;
import net.minecraft.server.v1_7_R4.*;
import net.minecraft.util.com.mojang.authlib.GameProfile;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_7_R4.CraftServer;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings({"unchecked", "FieldCanBeLocal"})
public class PlayerJoinListener implements Listener {
    private final AnchorStorage anchorStorage;
    private final AnchorFix plugin = AnchorFix.getInstance();
    private final BukkitScheduler scheduler = Bukkit.getScheduler();

    private final long NPC_REMOVE_DELAY = 2L;

    public PlayerJoinListener(AnchorStorage anchorStorage) {
        this.anchorStorage = anchorStorage;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        List<Map<String, String>> anchorList = anchorStorage.getAnchorList(player);
        loadAnchorChunks(anchorList);
    }

    public void loadAnchorChunks(List<Map<String, String>> anchorList) {
        for (Map<String, String> anchorData : anchorList) {
            String worldType = anchorData.get("world");
            int x = Integer.parseInt(anchorData.get("x"));
            int z = Integer.parseInt(anchorData.get("z"));
            int xChunk = x >> 4;
            int zChunk = z >> 4;

            World world = Bukkit.getWorld(worldType);

            if (world.isChunkLoaded(xChunk, zChunk)) {
                continue;
            }

            WorldServer nmsWorld = ((CraftWorld) world).getHandle();
            MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
            GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "Tobi");
            EntityPlayer npc = new EntityPlayer(nmsServer, nmsWorld, gameProfile, new PlayerInteractManager(nmsWorld));
            npc.playerConnection = new PlayerConnection(npc.server, new NetworkManager(true), npc);
            npc.playerInteractManager.setGameMode(EnumGamemode.CREATIVE);
            npc.getBukkitEntity().setMetadata("NPC", new FixedMetadataValue(plugin, Boolean.TRUE));

            world.loadChunk(xChunk, zChunk);
            world.refreshChunk(xChunk, zChunk);

            npc.spawnIn(nmsWorld);
            npc.setLocation(x, 60, z, 1, 1);
            npc.world.players.add(npc);
            npc.world.getChunkAt(xChunk, zChunk).a(npc);  // Add npc to chunk entity list

            scheduler.runTaskLater(plugin, () -> npc.world.removeEntity(npc), 20L * NPC_REMOVE_DELAY );
        }

    }

}
