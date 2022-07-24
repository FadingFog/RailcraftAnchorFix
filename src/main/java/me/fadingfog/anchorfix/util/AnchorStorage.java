package me.fadingfog.anchorfix.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;


@SuppressWarnings({"unchecked", "rawtypes"})
public class AnchorStorage {

    private static File configFile;
    private static FileConfiguration config;

    public boolean LOAD_DEFAULT = false;

    public void setup(){
        configFile = new File(Bukkit.getServer().getPluginManager().getPlugin("AnchorFix").getDataFolder(), "anchors.yml");

//        if (!configFile.exists()){  // Not necessary
//            try{
//                configFile.createNewFile();
//            }catch (IOException ignored){
//                System.out.println("Can't create storage file");
//            }
//        }

        config = YamlConfiguration.loadConfiguration(configFile);
        if (LOAD_DEFAULT) loadDefaults();
        save();
    }

    public FileConfiguration getConfig(){
        return config;
    }

    public void setAnchor(Player player, String type, Location location) {
        List<Object> preparedData = preModifyAnchor(player, type, location);
        HashMap<String, String> loc = (HashMap) preparedData.get(0);
        String path = (String) preparedData.get(1);

        List<Map<?, ?>> anchorList = config.isSet(path) ? config.getMapList(path) : new ArrayList<>();
        if (!anchorList.contains(loc)) {
            anchorList.add(loc);
            config.set(path, anchorList);
            System.out.println("Anchor at " + loc.toString() + " added to storage");
        }
    }

    public void removeAnchor(Player player, String type, Location location) {
        List<Object> preparedData = preModifyAnchor(player, type, location);
        HashMap<String, String> loc = (HashMap) preparedData.get(0);
        String path = (String) preparedData.get(1);

        if (config.isSet(path)){
            List<Map<?, ?>> anchorList = config.getMapList(path);
            if (anchorList.remove(loc)) {
                config.set(path, anchorList);
                System.out.println("Anchor at " + loc.toString() + " removed from storage");
            }
        }
    }

    private List<Object> preModifyAnchor(Player player, String type, Location location) {
        reload();

        String playerName = player.getName();
        String xCoord = Integer.toString((int) location.getX());
        String zCoord = Integer.toString((int) location.getZ());
        String world = location.getWorld().getName();

        HashMap<String, String> loc = new HashMap<String, String>() {{
            put("world", world);
            put("x", xCoord);
            put("z", zCoord);
        }};

        String path = MessageFormat.format("anchor.{0}.{1}", playerName, type);

        return Arrays.asList(loc, path);
    }

    public void save(){
        try{
            config.save(configFile);
        }catch (IOException e){
            System.out.println("Can't save storage file");
        }
    }

    public static void reload(){
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public void loadDefaults() {
        config.addDefault("anchor", "");
        config.options().copyDefaults(true);
    }

}
