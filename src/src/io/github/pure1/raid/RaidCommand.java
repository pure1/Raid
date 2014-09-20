/*  
 * Copyright 2014 Oliver Strik <pure1.github.io>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 * 
 */

package io.github.pure1.raid;

import java.util.ArrayList;
import java.util.List;

import io.github.pure1.raid.party.PartyHandler;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RaidCommand implements CommandExecutor{

	private Raid plugin;
	private PartyHandler partyHandler;

	public RaidCommand(Raid raid) {
		plugin = raid;
		partyHandler = plugin.getPartyHandler();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		/*
		 * -raid
		 * 
		 * --party
		 * ---make <x> {y}
		 * ---disband {y}
		 * ---members <x>
		 * ---invite <x>
		 * ===promote <x> {y}
		 * ---kick <x>
		 * #####################
		 * 
		 * 
		 * --dungeon
		 * ---add <x>
		 * ---set <x>
		 * ---remove <x>
		 * 
		 * --tools
		 * 
		 */
		if (cmd.getName().equalsIgnoreCase("raid")){
			Raid(sender, args);
		}else{
			rParty(sender, args);
		}

		return false;
	}

	private void rParty(CommandSender sender, String[] args) {
		if(args.length == 0){
			party(sender);
		}else{
			party(sender, args, 0);
		}
		
	}

	private void Raid(CommandSender sender, String[] args) {
		if(args.length == 0){
			sender.sendMessage("[raid] <<placeholder>>");
			help();
		}else if(args.length == 1){
			switch(args[0].toLowerCase()){
			case "party":
				party(sender);
				break;
			case "dungeon":
				sender.sendMessage("[raid] <<placeholder>>");
				break;
			case "tools":
				sender.sendMessage("[raid) <<placeholder>>");
				break;
			default:
				sender.sendMessage("[raid] <<placeholder>>");
			}
		}else{
			switch(args[0].toLowerCase()){
			case "party":
				party(sender, args, 1);
				break;
			case "dungeon":
				sender.sendMessage("[raid] <<placeholder>>");
				break;
			case "tools":
				sender.sendMessage("[raid) <<placeholder>>");
				break;
			default:
				sender.sendMessage("[raid] <<placeholder>>");
			}
		}
		
	}

	private void help() {
		
	}
	
	private void party(CommandSender sender) {
		if(!sender.hasPermission("party.chat"))return;
		if(partyHandler.inParty(sender)){
			sender.sendMessage("[Raid] Party chat " + partyHandler.togglePartyChat((Player) sender));
		}else{

			sender.sendMessage("[Raid] you are not in a party :(");
		}
	}
	private void party(CommandSender sender, String[] args, int l) {
		/*
		 * --party
		 * ---make <x> ###{y}###
		 * ---disband {y}
		 * ---leave
		 * ---members <x>
		 * ---invite <x>
		 * ###promote <x> {y}####
		 * ---kick <x>
		 */
		if(args.length == 2-l){
			switch(args[1-l].toLowerCase()){
			case "make":
				if(!sender.hasPermission("party.make"))return;
				sender.sendMessage("[Raid Party] <<placeholder>>");
				break;
			case "disband":
				if(!sender.hasPermission("party.disband"))return;
				partyHandler.disbandParty(partyHandler.getParty(sender));
				break;
			case "leave":
				if(!sender.hasPermission("party.leave"))return;
				if(sender instanceof Player){
					partyHandler.leaveParty((Player) sender);
				}
				break;
			case "join":
				if(!sender.hasPermission("party.join"))return;
				partyHandler.join(sender);
			case "invite":
				if(!sender.hasPermission("party.invite"))return;
				sender.sendMessage("[Raid Party] <<placeholder>>");
				break;
			case "members":
				if(!sender.hasPermission("party.members"))return;
				if(partyHandler.inParty(sender)){
					sender.sendMessage("[Raid] Party members:");
					Party party = partyHandler.getParty(sender);
					for(Player player: party.getMembers()){
						if(player == party.getLeader()){
							sender.sendMessage(player.getName() + " <leader>");
						}else{
							sender.sendMessage(player.getName());
						}
					}
				}else{
					sender.sendMessage("[Raid] You are not in a party :(");
				}
				break;
			case "kick":
				if(!sender.hasPermission("party.kick"))return;
				sender.sendMessage("[Raid Party] <<placeholder>>");
				break;
			}
		}else{
			switch(args[1-l].toLowerCase()){
			case "make":
				if(!sender.hasPermission("party.make"))return;
				partyHandler.makeParty(sender, args[2-l]);
				break;
			case "disband":
				if(!sender.hasPermission("party.disband.other"))return;
				partyHandler.disbandParty(partyHandler.getParty(args[-l]));
				break;
			case "invite":
				if(!sender.hasPermission("party.invite"))return;
				if(sender instanceof Player){
					partyHandler.invitePlayer((Player) sender, args[2-l]);
					sender.sendMessage("[Raid] " + args[2-l] + " invited to your party.");
				}
				break;
			case "members":
				if(!sender.hasPermission("party.members"))return;
				if(partyHandler.partyExists(args[2-l])){
					sender.sendMessage("[Raid] Party members:");
					Party party = partyHandler.getParty(args[2-l]);
					for(Player player: party.getMembers()){
						if(player == party.getLeader()){
							sender.sendMessage(player.getName() + " <leader>");
						}else{
							sender.sendMessage(player.getName());
						}
					}
				}else{
					sender.sendMessage("[Raid] Party doesn't exist.");
				}
				break;
			case "kick":
				if(!sender.hasPermission("party.kick"))return;
				List<Player> players = new ArrayList<Player>(Bukkit.getServer().getOnlinePlayers());
				boolean kicked = true;
				for(Player p: players){
					if(p.getName().equalsIgnoreCase(args[2-l])){
						if(!partyHandler.kickPlayer(p)){
							sender.sendMessage("[Raid] Cannot kick player");
							kicked = true;
						}
						break;
					}
				}
				if(!kicked){
					sender.sendMessage("[Raid] Player is not online");
				}
				break;
			}
		}
	}
}
