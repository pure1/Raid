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

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Party {

	private Raid raid;
	
	private String name;
	private Player leader;
	private List<Player> members;
	
	
	public Party(Raid raid, String name, Player leader){
		this.setRaid(raid);
		this.setName(name);
		this.setLeader(leader);
		members = new ArrayList<Player>();
		members.add(leader);
	}
	
	public List<Player> getMembers(){
		return members;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Player getLeader() {
		return leader;
	}

	public void setLeader(Player leader) {
		this.leader = leader;
	}

	public void removeMember(Player p) {
		if(p != leader){
			members.remove(p);
		}
	}

	public void addMember(Player sender) {
		members.add(sender);
	}

	public Raid getRaid() {
		return raid;
	}

	public void setRaid(Raid raid) {
		this.raid = raid;
	}

	public void sendMessage(String msg) {
		for(Player player: members){
			player.sendMessage(ChatColor.GREEN + "[" + name + "]"+ msg);
			raid.logger.info("[" + name + "]"+ msg);
		}
	}

}
