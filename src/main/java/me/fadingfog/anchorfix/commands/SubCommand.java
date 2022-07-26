package me.fadingfog.anchorfix.commands;

import org.bukkit.entity.Player;

public abstract class SubCommand {

    public abstract String getName();

    public abstract String getSuccessMessage();

    public abstract void perform(Player player, String[] args);

}
