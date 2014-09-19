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
		if(args.length == 0){
			sender.sendMessage("[raid] <<placeholder>>");
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
			case "join":
				partyHandler.join(sender);
			default:
				sender.sendMessage("[raid] <<placeholder>>");
			}
		}else{
			switch(args[0].toLowerCase()){
			case "party":
				party(sender, args);
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
		return false;
	}

	private void help() {
		
	}
	
	private void party(CommandSender sender) {
		if(partyHandler.inParty(sender)){
			sender.sendMessage("[Raid] You are a member of " + partyHandler.getParty(sender).getName());
		}else{

			sender.sendMessage("[Raid] you are not in a party :(");
		}
	}
	private void party(CommandSender sender, String[] args) {
		/*
		 * --party
		 * ---make <x> ###{y}###
		 * ---disband {y}
		 * ---members <x>
		 * ---invite <x>
		 * ###promote <x> {y}####
		 * ---kick <x>
		 */
		if(args.length == 2){
			switch(args[1].toLowerCase()){
			case "make":
				sender.sendMessage("[Raid Party] <<placeholder>>");
				break;
			case "disband":
				partyHandler.disbandParty(partyHandler.getParty(sender));
				break;
			case "invite":
				sender.sendMessage("[Raid Party] <<placeholder>>");
				break;
			case "members":
				sender.sendMessage("[Raid Party] <<placeholder>>");
				break;
			case "kick": 
				sender.sendMessage("[Raid Party] <<placeholder>>");
				break;
			}
		}else{
			switch(args[1].toLowerCase()){
			case "make":
				partyHandler.makeParty(sender, args[2]);
				break;
			case "disband":
				partyHandler.disbandParty(partyHandler.getParty(args[2]));
				break;
			case "invite":
				if(sender instanceof Player){
					partyHandler.invitePlayer((Player) sender, args[2]);
					sender.sendMessage("[Raid] " + args[2] + " invited to your party.");
				}
				break;
			case "members":
				if(partyHandler.partyExists(args[2])){
					sender.sendMessage("[Raid] Party members:");
					Party party = partyHandler.getParty(args[2]);
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
				List<Player> players = new ArrayList<Player>(Bukkit.getServer().getOnlinePlayers());
				for(Player p: players){
					if(p.getName() == args[2]){
						if(partyHandler.kickPlayer(p)){
							sender.sendMessage("[Raid] Cannot kick player");
						}
						break;
					}else{
						sender.sendMessage("[Raid] Player is not online");
					}
					
				}
				break;
			}
		}
	}
}
