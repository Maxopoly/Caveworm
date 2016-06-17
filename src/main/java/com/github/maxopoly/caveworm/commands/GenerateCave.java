package com.github.maxopoly.caveworm.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.maxopoly.caveworm.CaveWormAPI;

import vg.civcraft.mc.civmodcore.command.PlayerCommand;

public class GenerateCave extends PlayerCommand {

	public GenerateCave(String name) {
		super(name);
		setIdentifier("cwgen");
		setDescription("Begins spawning a cave at your current location");
		setUsage("/cwgen <length>");
		setArguments(1, 1);
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED
					+ "Generating caves inside the console seems like a bad idea, let's not do that");
			return true;
		}
		int length;
		try {
			length = Integer.parseInt(args [0]);
		}
		 catch (NumberFormatException e) {
			 sender.sendMessage(ChatColor.RED + args [0] + " is not a valid integer");
			 return true;
		 }
		CaveWormAPI.spawnCaveAt(((Player) sender).getLocation(), length);
		sender.sendMessage(ChatColor.GREEN + "Done");
		return true;
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String[] args) {
		// TODO Auto-generated method stub
		return null;
	}

}
