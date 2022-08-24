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

    private static AnchorStorage instance;
    private static File configFile;
    private static FileConfiguration config;

    public boolean LOAD_DEFAULT = false;

    public static AnchorStorage getInstance() {
        return AnchorStorage.instance;
    }

    public void setup() {
        AnchorStorage.instance = this;
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

    public List<Map<String, String>> getAnchorList() {
        reload();

        List<Map<String, String>> anchorList = new ArrayList();
        Set<String> playerNames = config.getConfigurationSection("anchor").getKeys(false);

        for (String playerName : playerNames) {
            String path = MessageFormat.format("anchor.{0}", playerName);
            Map<String, Object> anchorListPerType = config.getConfigurationSection(path).getValues(true);

            for (Map.Entry<String, Object> pair : anchorListPerType.entrySet()) {
                anchorList.addAll((Collection) pair.getValue());
            }
        }

        return anchorList;
    }

    public List<Map<String, String>> getAnchorList(Player player) {
        reload();

        List<Map<String, String>> anchorList = new ArrayList();
        String playerName = player.getName();
        String path = MessageFormat.format("anchor.{0}", playerName);

        if (config.isSet(path)) {
            Map<String, Object> anchorListPerType = config.getConfigurationSection(path).getValues(true);

            for (Map.Entry<String, Object> pair : anchorListPerType.entrySet()) {
                anchorList.addAll((Collection) pair.getValue());
            }
        }

        return anchorList;
    }

    public void setAnchor(Player player, String type, Location location) {
        reload();

        String playerName = player.getName();
        HashMap<String, String> loc = parseLocation(location);
        String path = MessageFormat.format("anchor.{0}.{1}", playerName, type);

        List<Map<?, ?>> anchorList = config.isSet(path) ? config.getMapList(path) : new ArrayList<>();
        if (!anchorList.contains(loc)) {
            anchorList.add(loc);
            config.set(path, anchorList);
        }

        this.save();
    }

    public void removeAnchor(Location location) {
        reload();

        String path = null;

        HashMap<String, String> loc = parseLocation(location);
        Set<String> playerNames = config.getConfigurationSection("anchor").getKeys(false);

        search: {
            for (String playerName : playerNames) {
                path = MessageFormat.format("anchor.{0}", playerName);
                Map<String, Object> anchorListPerType = config.getConfigurationSection(path).getValues(true);

                for (Map.Entry<String, Object> pair : anchorListPerType.entrySet()) {
                    if (((ArrayList) pair.getValue()).contains(loc)) {
                        String type = pair.getKey();
                        path = MessageFormat.format("anchor.{0}.{1}", playerName, type);
                        break search;
                    }
                }
            }
        }

        if (path != null && config.isSet(path)) {
            List<Map<?, ?>> anchorList = config.getMapList(path);
            if (anchorList.remove(loc)) {
                config.set(path, anchorList);
                this.save();
            }
        }

    }

    public void removeAnchor(String type, Location location) {
        reload();

        String path;

        HashMap<String, String> loc = parseLocation(location);
        Set<String> playerNames = config.getConfigurationSection("anchor").getKeys(false);

        for (String playerName : playerNames) {
            path = MessageFormat.format("anchor.{0}.{1}", playerName, type);
            List<Map<?, ?>> anchorList = config.getMapList(path);

            if (anchorList.contains(loc) && anchorList.remove(loc)) {
                config.set(path, anchorList);
                this.save();
                break;
            }
        }

    }

    public void removeAnchor(Player player, String type, Location location) {
        reload();

        String playerName = player.getName();
        HashMap<String, String> loc = parseLocation(location);
        String path = MessageFormat.format("anchor.{0}.{1}", playerName, type);  // Create path to anchor

        if (config.isSet(path)) {  // If path exists
            List<Map<?, ?>> anchorList = config.getMapList(path);  // Get list of all anchors of current type
            if (anchorList.remove(loc)) {  // If anchor location successfully deleted from list
                config.set(path, anchorList);  // Set modified list
                this.save();
            }
        }

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

    public static HashMap<String, String> parseLocation(Location location) {

        String world = location.getWorld().getName();
        String xCoord = Integer.toString((int) location.getX());
        String zCoord = Integer.toString((int) location.getZ());
        String yCoord = Integer.toString((int) location.getY());

        return new HashMap<String, String>() {{
            put("world", world);
            put("x", xCoord);
            put("y", yCoord);
            put("z", zCoord);
        }};
    }

}
