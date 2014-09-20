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

package io.github.pure1.raid.listeners;

import io.github.pure1.raid.Raid;
import io.github.pure1.raid.party.PartyHandler;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerListener implements Listener{
	
	public Raid raid;
	
	public PlayerListener(Raid instance) {
		raid = instance;
	}
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event){
		Player p = event.getPlayer();
		PartyHandler party = raid.getPartyHandler();
		if(party.inChat(p)){
			raid.getPartyHandler().partyMessage(party.getParty(p), "<" +  p.getName() + "> " + event.getMessage());
			event.setCancelled(true);
		}
	}
}
