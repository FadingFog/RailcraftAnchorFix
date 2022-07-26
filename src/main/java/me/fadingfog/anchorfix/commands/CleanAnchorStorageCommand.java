package me.fadingfog.anchorfix.commands;

import me.fadingfog.anchorfix.util.AnchorStorage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class CleanAnchorStorageCommand extends SubCommand {
    private final AnchorStorage anchorStorage = AnchorStorage.getInstance();
    private int COUNTER = 0;

    @Override
    public String getName() {
        return "clean";
    }

    @Override
    public String getSuccessMessage() {
        return "Successfully removed " + COUNTER + " anchor(s) from config";
    }

    @Override
    public void perform(Player player, String[] args) {

        List<Map<String, String>> anchorList = anchorStorage.getAnchorList();

        for (Map<String, String> anchorData : anchorList) {
            String worldType = anchorData.get("world");
            int x = Integer.parseInt(anchorData.get("x"));
            int y = Integer.parseInt(anchorData.get("y"));
            int z = Integer.parseInt(anchorData.get("z"));

            World world = Bukkit.getWorld(worldType);
            Block block = world.getBlockAt(x, y, z);
            Material material = block.getType();

            if (!material.toString().equals("RAILCRAFT_MACHINEALPHA")) {
                Location loc = new Location(world, x, y, z);
                anchorStorage.removeAnchor(loc);
                COUNTER++;
            }
        }

    }
}
