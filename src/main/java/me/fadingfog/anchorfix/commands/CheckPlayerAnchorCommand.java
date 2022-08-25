package me.fadingfog.anchorfix.commands;

import me.fadingfog.anchorfix.util.AnchorStorage;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
import java.util.*;

@SuppressWarnings("unchecked")
public class CheckPlayerAnchorCommand extends SubCommand {
    private final AnchorStorage anchorStorage = AnchorStorage.getInstance();
    private String resultMessage;

    @Override
    public String getName() {
        return "check";
    }

    @Override
    public String getResultMessage() {
        return resultMessage;
    }

    @Override
    public void perform(Player player, String[] args) {

        if (args.length > 0) {
            String target = args[0];
            Map<String,List<Map<String,String>>> playerAnchors = anchorStorage.getPlayerAnchors(target);

            resultMessage = ChatColor.DARK_GREEN + MessageFormat.format(
                    "{0} has: {1} personal, {2} passive anchors",
                    target,
                    playerAnchors.get("personal") != null ? playerAnchors.get("personal").size() : 0,
                    playerAnchors.get("passive") != null ? playerAnchors.get("passive").size() : 0
            );

            if (args.length > 1 && Objects.equals(args[1], "-i")) {
                Map<String, List<Collection<String>>> anchorListPerType = new HashMap<>();

                for (Map.Entry<String,List<Map<String,String>>> pair : playerAnchors.entrySet()) {  // for each type of anchor
                    List<Collection<String>> anchorList = new ArrayList<>();

                    for (Map<String, String> anchorInfo : pair.getValue()) {  // for each anchor in map
                        anchorList.add(anchorInfo.values());
                    }

                    anchorListPerType.put(pair.getKey(), anchorList);
                }

                resultMessage += MessageFormat.format(
                        "\nPersonal: {0}\nPassive: {1}",
                        StringUtils.join(anchorListPerType.get("personal"), ", "),
                        StringUtils.join(anchorListPerType.get("passive"), ", ")
                        );
            }
        } else {
            resultMessage = ChatColor.RED + "You didn't provide the name of the target player";
        }


//        List<Map<String, String>> anchorList = anchorStorage.getAnchorList();
//
//        for (Map<String, String> anchorData : anchorList) {
//            String worldType = anchorData.get("world");
//            int x = Integer.parseInt(anchorData.get("x"));
//            int y = Integer.parseInt(anchorData.get("y"));
//            int z = Integer.parseInt(anchorData.get("z"));
//
//            World world = Bukkit.getWorld(worldType);
//            Block block = world.getBlockAt(x, y, z);
//            Material material = block.getType();
//
//            if (!material.toString().equals("RAILCRAFT_MACHINEALPHA")) {
//                Location loc = new Location(world, x, y, z);
//                anchorStorage.removeAnchor(loc);
//            }
//        }

    }
}
