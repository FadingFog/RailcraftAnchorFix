package me.fadingfog.anchorfix.listeners;

import me.fadingfog.anchorfix.util.AnchorStorage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

@SuppressWarnings("deprecation")
public class AnchorPlaceListener implements Listener {
    private final AnchorStorage anchorStorage;

    public AnchorPlaceListener(AnchorStorage anchorStorage) {
        this.anchorStorage = anchorStorage;
    }

    @EventHandler
    public void onPlaceAnchor(BlockPlaceEvent event) {
        String type;
        Block block = event.getBlock();
        Material material = block.getType();


        if (material.toString().equals("RAILCRAFT_MACHINEALPHA")){
            if (block.getData() == 2) {
                type = "personal";
            } else if (block.getData() == 13) {
                type = "passive";
            } else return;

            Player player = event.getPlayer();
            Location location = block.getLocation();

            anchorStorage.setAnchor(player, type, location);
        }
    }

    @EventHandler
    public void onBreakAnchor(BlockBreakEvent event) {
        String type;
        Block block = event.getBlock();
        Material material = block.getType();


        if (material.toString().equals("RAILCRAFT_MACHINEALPHA")){
            if (block.getData() == 2) {
                type = "personal";
            } else if (block.getData() == 13) {
                type = "passive";
            } else return;

            Player player = event.getPlayer();
            Location location = block.getLocation();

            anchorStorage.removeAnchor(player, type, location);
        }
    }

}
