package com.github.maxopoly.caveworm.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.github.maxopoly.caveworm.CaveWormAPI;
import com.github.maxopoly.caveworm.distribution.GlobalDistributor;

import vg.civcraft.mc.civmodcore.command.PlayerCommand;

public class SeedArea extends PlayerCommand {
	public SeedArea(String name) {
		super(name);
		setIdentifier("cwseed");
		setDescription("Begins distributing caves around the map as specified in the config");
		setUsage("/cwseed");
		setArguments(0, 0);
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		GlobalDistributor dist = CaveWormAPI.getDistributer();
		if (dist == null) {
			sender.sendMessage(ChatColor.RED + "Could not initiate seeding, check console log for more information");
			return true;
		}
		sender.sendMessage(ChatColor.GOLD + "Beginning to seed. This may take a while");
		dist.distribute();
		return true;
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String[] args) {
		// TODO Auto-generated method stub
		return null;
	}
}
