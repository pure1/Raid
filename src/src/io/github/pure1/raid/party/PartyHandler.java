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

package io.github.pure1.raid.party;

import io.github.pure1.raid.Party;
import io.github.pure1.raid.Raid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PartyHandler {

	private Raid raid;
	
	private List<Party> parties;
	private Map<Player, Party> invites;
	private Map<Player, Boolean> partyChat;
	
	public PartyHandler(Raid raid){
		this.raid = raid;
		parties = new ArrayList<Party>();
		invites = new HashMap<Player, Party>();
		partyChat = new HashMap<Player, Boolean>();
	}
	
	public Boolean makeParty(CommandSender sender, String name){
		if(inParty(sender)){
			sender.sendMessage("[Raid] you are already in a party.");
		}
		if(partyExists(name)){
			sender.sendMessage("[Raid] Party already exists...");
			return false;
		}
		if(sender instanceof Player){
			parties.add(new Party(raid, name, (Player) sender));
			sender.sendMessage("[Raid] You are now the leader of: " + name);
		}else{
			sender.sendMessage("[Raid] The server cannot make a party...");
		}
		return true;
	}

	public boolean partyExists(String name) {

		for(Party party: parties){
			if(party.getName().equalsIgnoreCase(name)){
				return true;
			}
		}
		return false;
	}

	public boolean inParty(CommandSender sender) {
		if(sender instanceof Player){
			for(Party party: parties){
				if(party.getMembers().contains((Player) sender)){
					return true;
				}
			}
		}
		return false;
	}

	public Party getParty(CommandSender sender) {
		if(sender instanceof Player){
			for(Party party: parties){
				if(party.getMembers().contains((Player) sender)){
					return party;
				}
			}
		}
		return null;
	}

	public boolean disbandParty(Party p) {
		//TODO link to RaidHandler
		//TODO add permissions...
		p.sendMessage(p.getName() + " has been disbanded.");
		return parties.remove(p);
	}

	public Party getParty(String name) {
		for(Party party: parties){
			if(party.getName().equalsIgnoreCase(name)){
				return party;
			}
		}
		return null;
	}

	public boolean kickPlayer(Player p) {
		if(inParty(p) && getParty(p).getLeader() != p){
			getParty(p).removeMember(p);
			return true;
		}
		return false;
	}

	public void leaveParty(Player p){
		if(inParty(p)){
			if(getParty(p).getLeader().equals(p)){
				disbandParty(getParty(p));
			}else{
				getParty(p).sendMessage( p.getName() + " has left the party.");
				getParty(p).removeMember(p);
			}
		}
	}
	public void invitePlayer(Player sender, String name) {
		if(!inParty(sender))return;
		List<Player> players = new ArrayList<Player>(Bukkit.getServer().getOnlinePlayers());
		Player player = null;
		for(Player p: players){
			if(p.getName().equalsIgnoreCase(name)){
				player = p;
				break;
			}
			
		}
		if(inParty(player)){
			sender.sendMessage("[Raid] " + name + " is already in a party.");
			return;
		}
		if(player != null){
			invites.put(player, getParty(sender));
			player.sendMessage("[Raid] " + sender.getName() + " has invited you to join " + getParty(sender).getName());
			player.sendMessage("type '/raid join' to accept.");
			sender.sendMessage("[Raid] you have invited " + name + "to your party.");
		}else{
			sender.sendMessage("[Raid] " + name + " is not online.");
		}	
	}

	public void join(CommandSender sender) {
		if(sender instanceof Player){
			Party party = invites.get((Player) sender);
			if(party != null){
				party.addMember((Player) sender);
				invites.remove((Player) sender);
				sender.sendMessage("[Raid] You have Joined " + party.getName());
			}else{
				sender.sendMessage("[Raid] You have not been invited to a party");
			}
		}
	}

	public boolean inChat(Player p) {
		if(partyChat.containsKey(p)){
			return partyChat.get(p);
		}
		return false;
	}

	public void partyMessage(Party party, String msg) {
		party.sendMessage(msg);
	}
	
	public boolean togglePartyChat(Player p){
		if(partyChat.containsKey(p)){
			partyChat.put(p, !partyChat.get(p));
		}else{
			partyChat.put(p, true);
		}
		return partyChat.get(p);
	}
}
