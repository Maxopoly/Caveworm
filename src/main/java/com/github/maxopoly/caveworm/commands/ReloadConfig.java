package com.github.maxopoly.caveworm.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import vg.civcraft.mc.civmodcore.command.PlayerCommand;

import com.github.maxopoly.caveworm.Caveworm;

public class ReloadConfig extends PlayerCommand {

	public ReloadConfig(String name) {
		super(name);
		setIdentifier("cwreload");
		setDescription("Reloads the config");
		setUsage("/cwreload");
		setArguments(0, 0);
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		Caveworm.getInstance().refreshConfig();
		sender.sendMessage(ChatColor.GREEN + "Reloaded config");
		return true;
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String[] args) {
		// TODO Auto-generated method stub
		return null;
	}

}
