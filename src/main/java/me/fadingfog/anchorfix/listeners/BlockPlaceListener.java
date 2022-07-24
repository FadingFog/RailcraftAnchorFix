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

public class BlockPlaceListener implements Listener {
    private final AnchorStorage anchorStorage;

    public BlockPlaceListener(AnchorStorage anchorStorage) {
        this.anchorStorage = anchorStorage;
    }

    @EventHandler
    public void onPlaceTNT(BlockPlaceEvent event) {
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
            anchorStorage.save();
            player.sendMessage("You are placed " + type + " anchor at " + location.toString());
        }
    }

    @EventHandler
    public void onBreakTNT(BlockBreakEvent event) {
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
            anchorStorage.save();
            player.sendMessage("You are removed  " + type + " anchor at " + location.toString());
        }
    }

}
