package me.fadingfog.anchorfix;


import me.fadingfog.anchorfix.commands.LoadChunkCommand;
import me.fadingfog.anchorfix.listeners.BlockPlaceListener;
import me.fadingfog.anchorfix.util.AnchorStorage;
import org.bukkit.plugin.java.JavaPlugin;

public final class AnchorFix extends JavaPlugin{
    AnchorStorage anchorStorage = new AnchorStorage();

    public void onEnable() {
        loadAnchorStorage();


        getServer().getPluginManager().registerEvents(new BlockPlaceListener(anchorStorage), this);
        getCommand("mk").setExecutor(new LoadChunkCommand());

    }

    public void loadAnchorStorage() {
        anchorStorage.LOAD_DEFAULT = true;
        anchorStorage.setup();
    }

    public AnchorStorage getAnchorStorage() {
        return this.anchorStorage;
    }

}