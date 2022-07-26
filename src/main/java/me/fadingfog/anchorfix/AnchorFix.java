package me.fadingfog.anchorfix;


import me.fadingfog.anchorfix.commands.CommandManager;
import me.fadingfog.anchorfix.listeners.AnchorPlaceListener;
import me.fadingfog.anchorfix.listeners.PlayerJoinListener;
import me.fadingfog.anchorfix.util.AnchorStorage;
import org.bukkit.plugin.java.JavaPlugin;

public final class AnchorFix extends JavaPlugin{
    private static AnchorFix instance;
    AnchorStorage anchorStorage = new AnchorStorage();

    public static AnchorFix getInstance() {
        return AnchorFix.instance;
    }

    public void onEnable() {
        AnchorFix.instance = this;
        loadAnchorStorage();


        getServer().getPluginManager().registerEvents(new AnchorPlaceListener(anchorStorage), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(anchorStorage), this);
        getCommand("anchorfix").setExecutor(new CommandManager());

    }

    // In the onDisable can be added removal of all nps, if such exist

    public void loadAnchorStorage() {
        anchorStorage.LOAD_DEFAULT = true;
        anchorStorage.setup();
    }

    public AnchorStorage getAnchorStorage() {
        return this.anchorStorage;
    }

}